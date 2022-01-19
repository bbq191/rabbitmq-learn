package com.imooc;

import com.imooc.exception.MessageRunTimeException;
import java.util.List;

/***
 * @author afu
 */
public interface MessageProducer {

  /**
   * 消息的发送 附带SendCallback回调执行响应的业务逻辑处理
   *
   * @param message 消息的发送
   * @param sendCallback 响应的业务逻辑处理
   * @throws MessageRunTimeException 运行时错误
   */
  void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

  /**
   * 不带业务回调的消息发送
   *
   * @param message 消息的发送
   * @throws MessageRunTimeException 运行时错误
   */
  void send(Message message) throws MessageRunTimeException;

  /**
   * 消息的批量发送
   *
   * @param messages 消息的发送
   * @throws MessageRunTimeException 运行时错误
   */
  void send(List<Message> messages) throws MessageRunTimeException;
}
