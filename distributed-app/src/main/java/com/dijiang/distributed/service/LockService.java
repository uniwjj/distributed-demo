package com.dijiang.distributed.service;

import com.dijiang.distributed.lock.curator.LockAction;
import com.dijiang.distributed.lock.curator.LockType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 锁业务层
 *
 * @author ginger
 * @create 2019-06-07 23:16
 */
@Slf4j
@Service
public class LockService {

  @LockAction("'expression'")
  public void simpleName() {
    log.info("enter simpleName method, lock name is expression");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {}
    log.info("leave simpleName method, lock name is expression");
  }

  @LockAction("#param")
  public void simpleParam(String param) {
    log.info("enter simpleParam method, lock name is {}", param);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {}
    log.info("leave simpleParam method, lock name is {}", param);
  }

  @LockAction(value = "'expression'", lockType = LockType.LEADER_LOCK)
  public void leader() {
    log.info("enter simpleName method, lock name is expression");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {}
    log.info("leave simpleName method, lock name is expression");
  }

}
