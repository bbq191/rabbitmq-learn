package com.imooc.autoconfigure;

import com.imooc.annotation.EnableElasticJob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 *
 * @author afu
 */
@EnableElasticJob
@Configuration
@ComponentScan({"com.imooc.*"})
public class RabbitProducerAutoConfiguration {}
