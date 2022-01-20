package com.imooc.service;

import com.imooc.constant.BrokerMessageStatus;
import com.imooc.entity.BrokerMessage;
import com.imooc.mapper.BrokerMessageMapper;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author afu */
@Service
public class MessageStoreServiceImpl implements MessageStoreService {

  @Autowired private BrokerMessageMapper brokerMessageMapper;

  @Override
  public int insert(BrokerMessage brokerMessage) {
    return this.brokerMessageMapper.insert(brokerMessage);
  }

  @Override
  public BrokerMessage selectByMessageId(String messageId) {
    return this.brokerMessageMapper.selectByPrimaryKey(messageId);
  }

  @Override
  public void succuess(String messageId) {
    this.brokerMessageMapper.changeBrokerMessageStatus(
        messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
  }

  @Override
  public void failure(String messageId) {
    this.brokerMessageMapper.changeBrokerMessageStatus(
        messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
  }

  @Override
  public int updateTryCount(String brokerMessageId) {
    return this.brokerMessageMapper.update4TryCount(brokerMessageId, new Date());
  }

  @Override
  public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
    return this.brokerMessageMapper.queryBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
  }
}
