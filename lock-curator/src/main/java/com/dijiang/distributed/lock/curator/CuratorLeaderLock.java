package com.dijiang.distributed.lock.curator;

import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

/**
 * Curator主从锁
 *
 * @author ginger
 * @create 2019-06-07 14:12
 */
@Slf4j
public class CuratorLeaderLock implements CLock {
  /**
   * 锁路径
   */
  private String lockPath;

  /**
   * 是否为leader节点标志位
   */
  private volatile Boolean isLeader = false;

  /**
   * curator客户端
   */
  private CuratorFramework client;

  CuratorLeaderLock(CuratorFramework client, String lockPath) {
    this.client = client;
    this.lockPath = lockPath;
  }

  /**
   * 构建锁
   *
   * @return 构建好的锁
   */
  CuratorLeaderLock buildLock() throws Exception {
    final String hostName = InetAddress.getLocalHost().getHostName();
    LeaderLatch leaderLatch = new LeaderLatch(this.client, this.lockPath, hostName,
        LeaderLatch.CloseMode.NOTIFY_LEADER);
    leaderLatch.addListener(new LeaderLatchListener() {
      @Override
      public void isLeader() {
        if (log.isDebugEnabled()) {
          log.debug("{} becomes leader, lockPath is {}", hostName, lockPath);
        }
        isLeader = true;
      }

      @Override
      public void notLeader() {
        if (log.isDebugEnabled()) {
          log.debug("{} loses of leader, lockPath is {}", hostName, lockPath);
        }
        isLeader = false;
      }
    });
    leaderLatch.start();
    return this;
  }

  @Override
  public boolean isHeldByProcess() {
    if (log.isDebugEnabled()) {
      log.debug("is{}leader", this.isLeader ? " " : " not ");
    }
    return this.isLeader;
  }
}
