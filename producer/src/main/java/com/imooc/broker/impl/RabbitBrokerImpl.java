package com.imooc.broker.impl;

import com.imooc.Message;
import com.imooc.MessageType;
import com.imooc.broker.AsyncBaseQueue;
import com.imooc.broker.RabbitBroker;
import com.imooc.broker.RabbitTemplateContainer;
import com.imooc.constant.BrokerMessageConst;
import com.imooc.constant.BrokerMessageStatus;
import com.imooc.entity.BrokerMessage;
import com.imooc.service.MessageStoreService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author afu */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {
  @Autowired private RabbitTemplateContainer rabbitTemplateContainer;
  @Autowired private MessageStoreService messageStoreService;

  @Override
  public void rapidSend(Message message) {
    message.setMessageType(MessageType.RAPID);
    sendKernel(message);
  }

  @Override
  public void confirmSend(Message message) {
    message.setMessageType(MessageType.CONFIRM);
    sendKernel(message);
  }

  @Override
  public void reliantSend(Message message) {
    message.setMessageType(MessageType.RELIANT);
    BrokerMessage bm = messageStoreService.selectByMessageId(message.getMessageId());
    if (bm == null) {
      // 记录发送日志
      Date now = new Date();
      BrokerMessage brokerMessage = new BrokerMessage();
      brokerMessage.setMessageId(message.getMessageId());
      brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
      brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
      brokerMessage.setCreateTime(now);
      brokerMessage.setUpdateTime(now);
      brokerMessage.setMessage(message);
      messageStoreService.insert(brokerMessage);
    }
    // 执行发送消息
    sendKernel(message);
  }

  @Override
  public void sendMessages() {}

  /**
   * 发送消息的核心方法 使用异步线程池进行发送消息
   *
   * @param message 消息体
   */
  private void sendKernel(Message message) {
    AsyncBaseQueue.submit(
        () -> {
          CorrelationData correlationData =
              new CorrelationData(
                  String.format(
                      "%s#%s#%s",
                      message.getMessageId(),
                      System.currentTimeMillis(),
                      message.getMessageType()));
          String topic = message.getTopic();
          String routingKey = message.getRoutingKey();
          RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
          rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
          log.info(
              "#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}",
              message.getMessageId());
        });
  }
}
