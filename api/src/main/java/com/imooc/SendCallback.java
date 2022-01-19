package com.imooc;

/**
 * 回调函数处理
 *
 * @author afu
 */
public interface SendCallback {

  /**
   * 成功
   */
  void onSuccess();

  /**
   * 失败
   */
  void onFailure();
}
