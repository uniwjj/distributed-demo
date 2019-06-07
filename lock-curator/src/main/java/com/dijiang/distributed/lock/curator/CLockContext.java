package com.dijiang.distributed.lock.curator;

/**
 * Curator锁上下文
 *
 * @author ginger
 * @create 2019-06-07 14:22
 */
public interface CLockContext {

  /**
   * 获取可重入锁
   *
   * @param key 锁定的资源
   * @return 获取到的锁
   */
  CLock getReentrantLock(String key);

  /**
   * 获取主从锁
   *
   * @param key 锁定的资源
   * @return 获取到的锁
   */
  CLock getLeaderLock(String key);

  /**
   * 获取锁全路径
   *
   * @param lockType 锁类型
   * @param key 要锁定的资源键
   * @return 锁全路径
   */
  String getLockPath(LockType lockType, String key);

}
