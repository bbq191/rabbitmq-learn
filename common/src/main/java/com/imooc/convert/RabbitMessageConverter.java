package com.imooc.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 装饰者模式处理messageProperties
 *
 * @author afu
 */
public class RabbitMessageConverter implements MessageConverter {
  private final GenericMessageConverter delegate;

  public RabbitMessageConverter(GenericMessageConverter g) {
    Preconditions.checkNotNull(g);
    this.delegate = g;
  }

  @Override
  public Message toMessage(Object o, MessageProperties messageProperties)
      throws MessageConversionException {
    return this.delegate.toMessage(o, messageProperties);
  }

  @Override
  public Object fromMessage(Message message) throws MessageConversionException {
    return this.delegate.fromMessage(message);
  }
}
