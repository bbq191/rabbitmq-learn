package com.imooc.serializer;

/** @author afu */
public interface SerializerFactory {

  /**
   * 创建序列化
   *
   * @return 序列化对象
   */
  Serializer create();
}
