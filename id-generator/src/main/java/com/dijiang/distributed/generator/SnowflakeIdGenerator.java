package com.dijiang.distributed.generator;

import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * id生成器（以Twitter的Snowflake算法思路实现）
 *
 * @author ginger
 * @create 2019-06-15 15:06
 */
@Slf4j
public class SnowflakeIdGenerator implements IdGenerator {

  /**
   * 起始时间戳
   */
  private final long TIMESTAMP_EPOCH;

  /**
   * 序列比特位数
   */
  private final int SEQUENCE_BITS;

  /**
   * 示例比特位数
   */
  private final int INSTANCE_BITS;

  /**
   * 实例需要移动的位数
   */
  private final int INSTANCE_SHIFT;

  /**
   * 时间需要移动的位数
   */
  private final int TIMESTAMP_SHIFT;

  /**
   * 序列掩码
   */
  private final int SEQUENCE_MASK;

  /**
   * 分配的示例id
   */
  private final int INSTANCE_ID;

  public SnowflakeIdGenerator(SnowflakeConfig config) {
    this.TIMESTAMP_EPOCH = config.getTimestampEpoch();
    this.SEQUENCE_BITS = config.getSequenceBits();
    this.INSTANCE_BITS = config.getInstanceBits();
    this.INSTANCE_SHIFT = this.SEQUENCE_BITS;
    this.TIMESTAMP_SHIFT = this.SEQUENCE_BITS + this.INSTANCE_BITS;
    this.SEQUENCE_MASK = ~(-1 << this.SEQUENCE_BITS);
    this.INSTANCE_ID = config.getInstanceId();
  }

  @Setter
  @Getter
  private class Sequence {

    /**
     * 序列值
     */
    private final Integer value;

    /**
     * 序列所在的时间戳
     */
    private final Long timestamp;

    public Sequence(Integer value, Long timestamp) {
      this.value = value;
      this.timestamp = timestamp;
    }

    long getId() {
      return ((this.timestamp - TIMESTAMP_EPOCH) << TIMESTAMP_SHIFT)
          | (INSTANCE_ID << INSTANCE_SHIFT)
          | value;
    }
  }

  /**
   * 序列对象原子引用
   */
  private final AtomicReference<Sequence> sequence = new AtomicReference<>();

  /**
   * 等待下一毫秒的时间戳
   *
   * @return 下一毫秒的时间戳
   */
  private long waitForNextTimestamp() {
    long currentTimestamp;
    while ((currentTimestamp = System.currentTimeMillis()) <= sequence.get().getTimestamp()) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ignored) {}
    }

    return currentTimestamp;
  }

  @Override
  public Long nextId() {
    Sequence currentSequence, nextSequence;
    do {
      currentSequence = sequence.get();
      long currentTimestamp = System.currentTimeMillis();

      if (currentSequence == null || currentSequence.getTimestamp() < currentTimestamp) {
        nextSequence = new Sequence(0, currentTimestamp);
      } else if (currentSequence.getTimestamp() == currentTimestamp) {
        int nextValue = (currentSequence.getValue()) + 1 & SEQUENCE_MASK;
        if (nextValue == 0) {
          currentTimestamp = waitForNextTimestamp();
        }
        nextSequence = new Sequence(nextValue, currentTimestamp);
      } else {
        log.error("clock moves backward, request is rejected, sequence timestamp is {}, "
            + "current timestamp is {}", currentSequence.getTimestamp(), currentTimestamp);
        throw new IllegalStateException("Clock moves backward. Request is rejected.");
      }
    } while (!sequence.compareAndSet(currentSequence, nextSequence));

    return nextSequence.getId();
  }

  @Override
  public String nextStr() {
    long id = nextId();
    char[] chars = new char[13];
    for (int i = 0; i < 13; i++) {
      char c = getChar((int) id & 0x1f);
      chars[12 - i] = c;
      id = id >> 5;
    }
    StringBuilder sb = new StringBuilder();
    for (char c : chars) {
      sb.append(c);
    }
    return sb.toString();
  }

  private char getChar(int num) {
    if (num <= 9) {
      return (char) (num + '0');
    }
    return (char) (num - 10 + 'A');
  }
}
