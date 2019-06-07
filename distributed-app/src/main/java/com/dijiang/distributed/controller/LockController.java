package com.dijiang.distributed.controller;

import com.dijiang.distributed.service.LockService;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 锁控制器
 *
 * @author ginger
 * @create 2019-06-07 23:17
 */
@Slf4j
@RestController
@RequestMapping("/lock")
public class LockController {

  @Autowired
  private LockService lockService;

  @RequestMapping("/simpleName")
  public void simpleName() throws InterruptedException {
    List<Thread> threads = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      threads.add(new Thread(() -> lockService.simpleName(), "thread-" + i));
    }

    threads.forEach(Thread::start);
    for (Thread thread : threads) {
      thread.join();
    }
  }

  @RequestMapping("/simpleParam")
  public void simpleParam(@RequestParam String param) throws InterruptedException {
    List<Thread> threads = Lists.newArrayList();
    for (int i = 0; i < 5; i++) {
      threads.add(new Thread(() -> lockService.simpleParam(param), "thread-" + i));
    }

    threads.forEach(Thread::start);
    for (Thread thread : threads) {
      thread.join();
    }

  }


}
