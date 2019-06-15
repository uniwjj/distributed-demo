package com.dijiang.distributed.generator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Snowflake配置文件
 *
 * @author ginger
 * @create 2019-06-15 15:21
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "snowflake")
public class SnowflakeConfig {

  /**
   * 保留位比特位数（默认1位）
   */
  private Integer signBits = 1;

  /**
   * 时间戳比特位数（默认41位，支持70年的毫秒值）
   */
  private Integer timestampBits = 41;

  /**
   * 工作机器比特位数（默认10位，支持1023台机器）
   */
  private Integer instanceBits = 10;

  /**
   * 序列比特位数（默认12位，单机每毫秒可以产生4095个序列）
   */
  private Integer sequenceBits = 12;

  /**
   * 发号器起始时间纪元
   */
  private Long timestampEpoch = 0L;

  /**
   * 示例id
   */
  private Integer instanceId;

}
