package com.imooc.serializer.impl;

import com.imooc.Message;
import com.imooc.serializer.Serializer;
import com.imooc.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory {
  public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

  @Override
  public Serializer create() {
    return JacksonSerializer.createParametricType(Message.class);
  }
}
