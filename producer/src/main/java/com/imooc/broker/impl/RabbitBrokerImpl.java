package com.imooc.broker.impl;

import com.imooc.Message;
import com.imooc.broker.AsyncBaseQueue;
import com.imooc.broker.RabbitBroker;
import com.imooc.broker.RabbitTemplateContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author afu */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {
  @Autowired private RabbitTemplateContainer rabbitTemplateContainer;

  @Override
  public void rapidSend(Message message) {}

  @Override
  public void confirmSend(Message message) {}

  @Override
  public void reliantSend(Message message) {}

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
