package com.dijiang.distributed.lock.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.leader.LeaderLatch;

/**
 * Curator主从锁
 *
 * @author ginger
 * @create 2019-06-07 14:12
 */
@Slf4j
public class CuratorLeaderLock implements CLock {

  /**
   * 锁对象
   */
  private LeaderLatch leaderLatch;

  /**
   * 锁路径
   */
  private String lockPath;

  CuratorLeaderLock(LeaderLatch leaderLatch, String lockPath) {
    this.leaderLatch = leaderLatch;
    this.lockPath = lockPath;
  }

  @Override
  public boolean isHeldByProcess() {
    if (log.isDebugEnabled()) {
      log.debug("is{}leader", leaderLatch.hasLeadership() ? " " : " not ");
    }
    return leaderLatch.hasLeadership();
  }
}
