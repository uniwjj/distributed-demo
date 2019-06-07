package com.dijiang.distributed.lock.curator;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Curator配置
 *
 * @author ginger
 * @create 2019-06-05 12:38
 */
@Setter
@Getter
@Component
@Configuration
@ConfigurationProperties(prefix = "zookeeper")
@ConditionalOnProperty(value = "zookeeper.connectStr")
public class CuratorConfig {

  /**
   * zk连接地址
   */
  private String connectStr;

  /**
   * 重试等待试卷
   */
  private Integer retryWaitMillis;

  /**
   * 最大重试次数
   */
  private Integer maxTryCount;

  /**
   * 回话超时试卷
   */
  private Integer sessionTimeout;

  /**
   * 链接超时时间
   */
  private Integer connectionTimeout;

  /**
   * 基础路径
   */
  private String basePath;

  @Bean
  public CuratorFramework createClient() {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(retryWaitMillis, maxTryCount);

    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString(connectStr)
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(sessionTimeout)
        .connectionTimeoutMs(connectionTimeout)
        .build();
    client.start();

    return client;
  }

}
