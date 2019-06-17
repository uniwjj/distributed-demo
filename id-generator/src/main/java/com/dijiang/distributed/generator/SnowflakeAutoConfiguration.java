package com.dijiang.distributed.generator;

import com.google.common.base.Preconditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Snowflake自动配置类
 *
 * @author ginger
 * @create 2019-06-15 16:14
 */
@Configuration
public class SnowflakeAutoConfiguration {

  @Bean
  @ConditionalOnProperty(value = "snowflake.instanceId")
  public SnowflakeConfig createCuratorConfig() {
    return new SnowflakeConfig();
  }

  @Bean
  @ConditionalOnBean(SnowflakeConfig.class)
  public SnowflakeIdGenerator createSnowflakeIdGenerator(SnowflakeConfig config) {
    checkSnowflakeConfig(config);
    return new SnowflakeIdGenerator(config);
  }

  /**
   * 检查配置文件是否合法
   *
   * @param config 参数配置
   */
  private void checkSnowflakeConfig(SnowflakeConfig config) {
    Preconditions.checkArgument(config.getSequenceBits() > 0, "sequence bits should more than 0");
    Preconditions.checkArgument(config.getInstanceBits() > 0, "instance bits should more than 0");
    Preconditions.checkArgument(config.getTimestampBits() > 0, "timestamp bits should more than 0");
    Preconditions.checkArgument(config.getSignBits() > 0, "sign bits should be than 0");
    Preconditions.checkArgument(config.getInstanceId() >= 0 && config.getInstanceId() < Math.pow(2, config.getInstanceBits()), "instance id should not less than 0 and less than 2^instanceBits");
    Preconditions.checkArgument(config.getTimestampEpoch() >= 0, "timestamp epoch bits should not less than 0");
    int bits = config.getSequenceBits() + config.getInstanceBits() + config.getTimestampBits() + config.getSignBits();
    Preconditions.checkArgument(bits == 64, "all bits should be 64");
  }

}
