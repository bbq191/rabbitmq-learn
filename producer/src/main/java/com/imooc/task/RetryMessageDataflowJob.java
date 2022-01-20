package com.imooc.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.imooc.annotation.ElasticJobConfig;
import com.imooc.broker.RabbitBroker;
import com.imooc.constant.BrokerMessageStatus;
import com.imooc.entity.BrokerMessage;
import com.imooc.service.MessageStoreService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author afu */
@Component
@ElasticJobConfig(
    name = "com.imooc.task.RetryMessageDataflowJob",
    cron = "0/10 * * * * ?",
    description = "可靠性投递消息补偿任务",
    overwrite = true,
    shardingTotalCount = 1)
@Slf4j
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {
  @Autowired private MessageStoreService messageStoreService;
  @Autowired private RabbitBroker rabbitBroker;

  private static final int MAX_RETRY_COUNT = 3;

  /**
   * 从数据库里抓出已经超时的任务
   *
   * @param shardingContext 任务数据
   * @return 消息列表
   */
  @Override
  public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
    List<BrokerMessage> list =
        messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
    log.info("--------@@@@@ 抓取数据集合, 数量：	{} 	@@@@@@-----------", list.size());
    return list;
  }

  /**
   * 将上面抓出已经超时的任务重发回去
   *
   * @param shardingContext 任务数据
   * @param dataList 数据列表
   */
  @Override
  public void processData(ShardingContext shardingContext, List<BrokerMessage> dataList) {
    dataList.forEach(
        brokerMessage -> {
          String messageId = brokerMessage.getMessageId();
          if (brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
            this.messageStoreService.failure(messageId);
            log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
          } else {
            // 每次重发的时候要更新一下try count字段
            this.messageStoreService.updateTryCount(messageId);
            // 重发消息
            this.rabbitBroker.reliantSend(brokerMessage.getMessage());
          }
        });
  }
}
