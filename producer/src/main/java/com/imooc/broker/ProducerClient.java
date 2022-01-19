package com.imooc.broker;

import com.google.common.base.Preconditions;
import com.imooc.Message;
import com.imooc.MessageProducer;
import com.imooc.MessageType;
import com.imooc.SendCallback;
import com.imooc.exception.MessageRunTimeException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author afu */
@Component
public class ProducerClient implements MessageProducer {
  @Autowired RabbitBroker rabbitBroker;

  @Override
  public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {
    Preconditions.checkNotNull(message.getTopic());
    String messageType = message.getMessageType();
    switch (messageType) {
      case MessageType.RAPID:
        rabbitBroker.rapidSend(message);
        break;
      case MessageType.CONFIRM:
        rabbitBroker.confirmSend(message);
        break;
      case MessageType.RELIANT:
        rabbitBroker.reliantSend(message);
        break;
      default:
        break;
    }
  }

  @Override
  public void send(Message message) throws MessageRunTimeException {}

  @Override
  public void send(List<Message> messages) throws MessageRunTimeException {}
}
