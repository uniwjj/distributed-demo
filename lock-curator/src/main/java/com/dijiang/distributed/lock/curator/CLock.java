package com.dijiang.distributed.lock.curator;

import java.util.concurrent.TimeUnit;

/**
 * Curator锁
 *
 * @author ginger
 * @create 2019-06-07 14:04
 */
public interface CLock {

  /**
   * 加锁
   *
   * @param timeout 超时时间
   * @param timeUnit 时间单位
   * @return 是否加锁成功
   */
  default boolean lock(long timeout, TimeUnit timeUnit) {
    throw new RuntimeException("unimplemented function");
  }

  /**
   * 释放锁
   */
  default void unlock() {
    throw new RuntimeException("unimplemented function");
  }

  /**
   * 线程是否拥有锁
   *
   * @return 是否拥有锁
   */
  default boolean isHeldByThread() {
    throw new RuntimeException("unimplemented function");
  }

  /**
   * 进程是否拥有锁
   *
   * @return 是否拥有锁
   */
  default boolean isHeldByProcess() {
    throw new RuntimeException("unimplemented function");
  }

}
