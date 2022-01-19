package com.imooc.serializer.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.imooc.serializer.Serializer;
import java.io.IOException;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author afu */
public class JacksonSerializer implements Serializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonSerializer.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
    MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    MAPPER.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
  }

  private final JavaType type;

  private JacksonSerializer(JavaType type) {
    this.type = type;
  }

  public JacksonSerializer(Type type) {
    this.type = MAPPER.getTypeFactory().constructType(type);
  }

  public static JacksonSerializer createParametricType(Class<?> cls) {
    return new JacksonSerializer(MAPPER.getTypeFactory().constructType(cls));
  }

  @Override
  public byte[] serializeRaw(Object data) {
    try {
      return MAPPER.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      LOGGER.error("序列化出错", e);
    }
    return null;
  }

  @Override
  public String serialize(Object data) {
    try {
      return MAPPER.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      LOGGER.error("序列化出错", e);
    }
    return null;
  }

  @Override
  public <T> T deserialize(String content) {
    try {
      return MAPPER.readValue(content, type);
    } catch (IOException e) {
      LOGGER.error("反序列化出错", e);
    }
    return null;
  }

  @Override
  public <T> T deserialize(byte[] content) {
    try {
      return MAPPER.readValue(content, type);
    } catch (IOException e) {
      LOGGER.error("反序列化出错", e);
    }
    return null;
  }
}
