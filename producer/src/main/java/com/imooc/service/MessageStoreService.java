package com.imooc.service;

import com.imooc.constant.BrokerMessageStatus;
import com.imooc.entity.BrokerMessage;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 数据表操作接口
 *
 * @author afu
 */
@Service
public interface MessageStoreService {
  int insert(BrokerMessage brokerMessage);

  BrokerMessage selectByMessageId(String messageId);

  void succuess(String messageId);

  void failure(String messageId);

  int updateTryCount(String brokerMessageId);

  List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus);
}
