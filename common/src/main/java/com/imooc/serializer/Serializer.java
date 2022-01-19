package com.imooc.serializer;

/**
 * 序列化和反序列化的接口
 *
 * @author afu
 */
public interface Serializer {
  /**
   * 序列化为byte字节码
   *
   * @param data 对象
   * @return 字节码
   */
  byte[] serializeRaw(Object data);

  /**
   * 序列化为 string
   *
   * @param data 对象
   * @return string
   */
  String serialize(Object data);

  /**
   * 反序列化为期望的类型
   *
   * @param content string 数据
   * @param <T> 期望类型
   * @return 期望类型
   */
  <T> T deserialize(String content);

  /**
   * 反序列化为期望的类型
   *
   * @param content 字节码
   * @param <T> 期望类型
   * @return 期望类型
   */
  <T> T deserialize(byte[] content);
}
