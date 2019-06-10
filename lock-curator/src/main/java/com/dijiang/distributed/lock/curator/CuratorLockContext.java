package com.dijiang.distributed.lock.curator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.net.InetAddress;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * Curator锁上下文
 *
 * @author ginger
 * @create 2019-06-07 14:28
 */
@Slf4j
@Component
@ConditionalOnBean(CuratorConfig.class)
public class CuratorLockContext implements CLockContext {

  @Autowired
  private CuratorConfig curatorConfig;

  @Autowired
  private CuratorFramework client;

  /**
   * 锁池
   * 注意：不存在内存泄漏问题，如果线程被回收，entity会变成（null, value），下次set时value会被清除
   * 如果是线程池，线程有可能不会被回收，entity相应也不会被回收，lock后续可以继续使用
   */
  private ThreadLocal<Map<String, CuratorReentrantLock>> lockPool = ThreadLocal.withInitial(Maps::newHashMap);

  /**
   * 主从锁Map
   * key：锁全路径，value：主从锁
   * 注意：资源不会回收
   */
  private final Map<String, CuratorLeaderLock> leaderLockPool = Maps.newConcurrentMap();


  @Override
  public CLock getReentrantLock(String key) {
    Preconditions.checkArgument(null != key);
    String lockPath = this.getLockPath(LockType.REENTRANT_LOCK, key);

    try {
      Map<String, CuratorReentrantLock> lockPool = this.lockPool.get();
      CuratorReentrantLock curatorReentrantLock = lockPool.get(lockPath);
      if (null != curatorReentrantLock) {
        return curatorReentrantLock;
      }

      InterProcessMutex ipm = new InterProcessMutex(client, lockPath);
      curatorReentrantLock = new CuratorReentrantLock(ipm, lockPath);
      if (log.isDebugEnabled()) {
        log.debug("create reentrant lock, key is {}", key);
      }
      lockPool.put(lockPath, curatorReentrantLock);
      return curatorReentrantLock;
    } catch (Exception e) {
      log.error("fail to get lock, key is {}", key, e);
      throw new RuntimeException("this should not happen", e);
    }
  }

  @Override
  public CLock getLeaderLock(String key) {
    Preconditions.checkArgument(null != key);
    String lockPath = this.getLockPath(LockType.LEADER_LOCK, key);

    CuratorLeaderLock leaderLock = leaderLockPool.get(lockPath);
    if (null != leaderLock) {
      return leaderLock;
    }

    synchronized (CuratorLockContext.class) {
      leaderLock = leaderLockPool.get(lockPath);
      if (null != leaderLock) {
        return leaderLock;
      }

      try {
        final String hostName = InetAddress.getLocalHost().getHostName();
        LeaderLatch leaderLatch = new LeaderLatch(this.client, lockPath, hostName,
            LeaderLatch.CloseMode.NOTIFY_LEADER);
        leaderLatch.addListener(new LeaderLatchListener() {
          @Override
          public void isLeader() {
            if (log.isDebugEnabled()) {
              log.debug("{} becomes leader, lockPath is {}", hostName, lockPath);
            }
          }

          @Override
          public void notLeader() {
            if (log.isDebugEnabled()) {
              log.debug("{} loses of leader, lockPath is {}", hostName, lockPath);
            }
          }
        });
        leaderLatch.start();

        leaderLock = new CuratorLeaderLock(leaderLatch, lockPath);
        leaderLockPool.put(lockPath, leaderLock);
        if (log.isDebugEnabled()) {
          log.debug("create leader lock, key is {}", key);
        }
        return leaderLock;
      } catch (Exception e) {
        log.error("fail to get leader lock, key is {}", key, e);
        throw new RuntimeException("this should not happen", e);
      }
    }
  }

  /**
   * 获取锁在zk上的全路径
   *
   * @param lockType 锁类型
   * @param key 要锁定的资源
   * @return zk上的全路径
   */
  @Override
  public String getLockPath(LockType lockType, String key) {
    Preconditions.checkArgument(null != key);
    return new StringBuilder(curatorConfig.getBasePath())
        .append("/")
        .append(lockType.toString())
        .append("/")
        .append(key)
        .toString();
  }

}
