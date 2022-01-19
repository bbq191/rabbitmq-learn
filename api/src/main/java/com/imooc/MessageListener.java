package com.imooc;

/**
 * 消费者监听消息
 *
 * @author afu
 */
public interface MessageListener {

  /**
   * 接收消息
   *
   * @param message 消息本体
   */
  void onMessage(Message message);
}
