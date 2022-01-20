package com.imooc.constant;

/**
 * 消息的发送状态
 *
 * @author afu
 */
public enum BrokerMessageStatus {
  /** 发送中 */
  SENDING("0"),
  /** 发送成功 */
  SEND_OK("1"),
  /** 发送失败 */
  SEND_FAIL("2"),
  /** 发送失败，根据业务重试 */
  SEND_FAIL_A_MOMENT("3");

  private final String code;

  BrokerMessageStatus(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
