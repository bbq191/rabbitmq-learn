package com.imooc.convert;

import com.google.common.base.Preconditions;
import com.imooc.serializer.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * amqp 与 自定义消息互转
 *
 * @author afu
 */
public class GenericMessageConverter implements MessageConverter {
  private final Serializer serializer;

  public GenericMessageConverter(Serializer serializer) {
    Preconditions.checkNotNull(serializer);
    this.serializer = serializer;
  }

  @Override
  public Message toMessage(Object o, MessageProperties messageProperties)
      throws MessageConversionException {
    return new Message(this.serializer.serializeRaw(o), messageProperties);
  }

  @Override
  public Object fromMessage(Message message) throws MessageConversionException {
    return new Message(this.serializer.deserialize(message.getBody()));
  }
}
