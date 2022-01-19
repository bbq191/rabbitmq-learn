package com.imooc.broker;

import com.imooc.Message;

/**
 * 具体发送不同种类型消息的接口
 *
 * @author afu
 */
public interface RabbitBroker {

  /**
   * 迅速消息
   *
   * @param message 消息体
   */
  void rapidSend(Message message);

  /**
   * 确认消息
   *
   * @param message 消息体
   */
  void confirmSend(Message message);

  /**
   * 可靠性消息
   *
   * @param message 消息体
   */
  void reliantSend(Message message);

  /** 批量消息 */
  void sendMessages();
}
