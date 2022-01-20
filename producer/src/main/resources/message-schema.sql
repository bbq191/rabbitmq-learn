# 表 broker_message.broker_message 结构
use rabbitmq;
CREATE TABLE IF NOT EXISTS `broker_message`
(
    `message_id`  varchar(128) NOT NULL comment '消息主键',
    `message`     varchar(4000) comment '消息本体',
    `try_count`   int(4)                DEFAULT 0 comment '最大重试数',
    `status`      varchar(10)           DEFAULT '消息投递状态',
    `next_retry`  timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00' comment '下次重试时间',
    `create_time` timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00',
    `update_time` timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00',
    PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 comment '消息投递可靠性记录表';