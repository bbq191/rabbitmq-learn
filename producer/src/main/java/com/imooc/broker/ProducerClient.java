package com.imooc.broker;

import com.imooc.Message;
import com.imooc.MessageProducer;
import com.imooc.SendCallback;
import com.imooc.exception.MessageRunTimeException;
import java.util.List;

/**
 * @author afu
 */
public class ProducerClient implements MessageProducer {

  @Override
  public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {}

  @Override
  public void send(Message message) throws MessageRunTimeException {}

  @Override
  public void send(List<Message> messages) throws MessageRunTimeException {}
}
