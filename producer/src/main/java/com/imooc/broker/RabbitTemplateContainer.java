package com.imooc.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.imooc.Message;
import com.imooc.MessageType;
import com.imooc.convert.GenericMessageConverter;
import com.imooc.convert.RabbitMessageConverter;
import com.imooc.exception.MessageRunTimeException;
import com.imooc.serializer.Serializer;
import com.imooc.serializer.SerializerFactory;
import com.imooc.serializer.impl.JacksonSerializerFactory;
import com.imooc.service.MessageStoreService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitTemplate 池化封装
 *
 * <p>每一个topic 对应一个RabbitTemplate
 *
 * <p>1. 提高发送的效率
 *
 * <p>2. 可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 *
 * @author afu
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {
  private final Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();
  private final Splitter splitter = Splitter.on("#");
  private final SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

  @Autowired private ConnectionFactory connectionFactory;
  @Autowired private MessageStoreService messageStoreService;

  public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
    Preconditions.checkNotNull(message);
    String topic = message.getTopic();
    RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
    if (rabbitTemplate != null) {
      return rabbitTemplate;
    }
    log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

    RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
    newTemplate.setExchange(topic);
    newTemplate.setRoutingKey(message.getRoutingKey());
    newTemplate.setRetryTemplate(new RetryTemplate());

    //	添加序列化反序列化和converter对象
    Serializer serializer = serializerFactory.create();
    GenericMessageConverter gmc = new GenericMessageConverter(serializer);
    RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
    newTemplate.setMessageConverter(rmc);

    String messageType = message.getMessageType();
    if (!MessageType.RAPID.equals(messageType)) {
      newTemplate.setConfirmCallback(this);
    }

    rabbitMap.putIfAbsent(topic, newTemplate);

    return rabbitMap.get(topic);
  }

  /** 无论是 confirm 消息 还是 reliant 消息 ，发送消息以后 broker都会去回调confirm */
  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    // 	具体的消息应答
    assert correlationData != null;
    List<String> strings = splitter.splitToList(correlationData.getId());
    String messageId = strings.get(0);
    long sendTime = Long.parseLong(strings.get(1));
    String messageType = strings.get(2);
    if (ack) {
      // 当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK
      //  如果当前消息类型为reliant 我们就去数据库查找并进行更新
      if (MessageType.RELIANT.endsWith(messageType)) {
        this.messageStoreService.succuess(messageId);
      }
      log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
    } else {
      log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
    }
  }
}
