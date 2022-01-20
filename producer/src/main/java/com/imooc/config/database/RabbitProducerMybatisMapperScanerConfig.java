package com.imooc.config.database;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author afu */
@Configuration
@AutoConfigureAfter(RabbitProducerDataSourceConfiguration.class)
public class RabbitProducerMybatisMapperScanerConfig {

  @Bean(name = "rabbitProducerMapperScannerConfigurer")
  public MapperScannerConfigurer rabbitProducerMapperScannerConfigurer() {
    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    mapperScannerConfigurer.setSqlSessionFactoryBeanName("rabbitProducerSqlSessionFactory");
    mapperScannerConfigurer.setBasePackage("com.imooc.mapper");
    return mapperScannerConfigurer;
  }
}
