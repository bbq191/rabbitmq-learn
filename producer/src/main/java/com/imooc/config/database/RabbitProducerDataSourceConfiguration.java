package com.imooc.config.database;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * 注入数据源
 *
 * @author afu
 */
@Configuration
@PropertySource({"classpath:message.properties"})
public class RabbitProducerDataSourceConfiguration {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RabbitProducerDataSourceConfiguration.class);

  @Value("${rabbit.producer.druid.type}")
  private Class<? extends DataSource> dataSourceType;

  /**
   * 配置主数据源，注入 rabbit.producer.druid.jdbc 开头的所有属性
   *
   * @return 数据注入信息
   */
  @Bean(name = "rabbitProducerDataSource")
  @Primary
  @ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")
  public DataSource rabbitProducerDataSource() {
    DataSource rabbitProducerDataSource = DataSourceBuilder.create().type(dataSourceType).build();
    LOGGER.info(
        "============= rabbitProducerDataSource : {} ================", rabbitProducerDataSource);
    return rabbitProducerDataSource;
  }

  public DataSourceProperties primaryDataSourceProperties() {
    return new DataSourceProperties();
  }

  public DataSource primaryDataSource() {
    return primaryDataSourceProperties().initializeDataSourceBuilder().build();
  }
}
