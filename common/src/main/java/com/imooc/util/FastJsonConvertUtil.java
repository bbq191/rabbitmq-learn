package com.imooc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.ArrayList;
import java.util.List;

/**
 * java对象与json进行转换的通用工具类
 *
 * @author afu
 */
public class FastJsonConvertUtil {
  private static final SerializerFeature[] FEATURES_WITH_NULL_VALUE = {
    SerializerFeature.WriteMapNullValue,
    SerializerFeature.WriteNullBooleanAsFalse,
    SerializerFeature.WriteNullListAsEmpty,
    SerializerFeature.WriteNullNumberAsZero,
    SerializerFeature.WriteNullStringAsEmpty
  };

  /**
   * <B>方法名称：</B>将JSON字符串转换为实体对象<br>
   * <B>概要说明：</B>将JSON字符串转换为实体对象<br>
   *
   * @param data JSON字符串
   * @param clzss 转换对象
   * @return T
   */
  public static <T> T convertJSONToObject(String data, Class<T> clzss) {
    try {
      return JSON.parseObject(data, clzss);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B>将JSONObject对象转换为实体对象<br>
   * <B>概要说明：</B>将JSONObject对象转换为实体对象<br>
   *
   * @param data JSONObject对象
   * @param clzss 转换对象
   * @return T
   */
  public static <T> T convertJSONToObject(JSONObject data, Class<T> clzss) {
    try {
      return JSONObject.toJavaObject(data, clzss);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B>将JSON字符串数组转为List集合对象<br>
   * <B>概要说明：</B>将JSON字符串数组转为List集合对象<br>
   *
   * @param data JSON字符串数组
   * @param clzss 转换对象
   * @return List<T>集合对象
   */
  public static <T> List<T> convertJSONToArray(String data, Class<T> clzss) {
    try {
      List<T> t = JSON.parseArray(data, clzss);
      return t;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B>将List<JSONObject>转为List集合对象<br>
   * <B>概要说明：</B>将List<JSONObject>转为List集合对象<br>
   *
   * @param data List<JSONObject>
   * @param clzss 转换对象
   * @return List<T>集合对象
   */
  public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clzss) {
    try {
      List<T> t = new ArrayList<T>();
      for (JSONObject jsonObject : data) {
        t.add(convertJSONToObject(jsonObject, clzss));
      }
      return t;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B>将对象转为JSON字符串<br>
   * <B>概要说明：</B>将对象转为JSON字符串<br>
   *
   * @param obj 任意对象
   * @return JSON字符串
   */
  public static String convertObjectToJSON(Object obj) {
    try {
      return JSON.toJSONString(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B>将对象转为JSONObject对象<br>
   * <B>概要说明：</B>将对象转为JSONObject对象<br>
   *
   * @param obj 任意对象
   * @return JSONObject对象
   */
  public static JSONObject convertObjectToJSONObject(Object obj) {
    try {
      return (JSONObject) JSONObject.toJSON(obj);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * <B>方法名称：</B><br>
   * <B>概要说明：</B><br>
   *
   * @param obj
   * @return
   */
  public static String convertObjectToJSONWithNullValue(Object obj) {
    try {
      return JSON.toJSONString(obj, FEATURES_WITH_NULL_VALUE);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    System.err.println(System.getProperties());
  }
}
