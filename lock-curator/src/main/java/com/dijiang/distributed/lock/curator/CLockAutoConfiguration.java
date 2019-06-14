package com.dijiang.distributed.lock.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Curator锁自动配置类
 *
 * @author ginger
 * @create 2019-06-13 20:15
 */
@Configuration
public class CLockAutoConfiguration {

  @Bean
  @ConditionalOnProperty(value = "zookeeper.address")
  public CuratorConfig createCuratorConfig() {
    return new CuratorConfig();
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(CuratorConfig.class)
  public CuratorFramework createClient(CuratorConfig config) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(config.getRetryWaitMillis(),
        config.getMaxTryCount());

    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString(config.getConnectStr())
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(config.getSessionTimeout())
        .connectionTimeoutMs(config.getConnectionTimeout())
        .build();
    client.start();

    return client;
  }

  @Bean
  @ConditionalOnBean(CuratorConfig.class)
  public CuratorLockContext createCuratorLockContext(CuratorConfig config, CuratorFramework client) {
    return new CuratorLockContext(config, client);
  }

  @Bean
  @ConditionalOnBean(CuratorConfig.class)
  public LockAspect createLockAspect(CuratorLockContext lockContext) {
    return new LockAspect(lockContext);
  }
}
