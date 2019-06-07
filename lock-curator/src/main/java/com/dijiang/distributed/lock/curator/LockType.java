package com.dijiang.distributed.lock.curator;

/**
 * 锁类型
 *
 * @author ginger
 * @create 2019-06-07 13:39
 */
public enum LockType {

  /**
   * 可重入锁
   */
  REENTRANT_LOCK,

  /**
   * 主从锁
   */
  LEADER_LOCK,

}
