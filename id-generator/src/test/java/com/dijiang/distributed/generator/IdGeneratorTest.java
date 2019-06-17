package com.dijiang.distributed.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 信号发生器测试类
 *
 * @author ginger
 * @create 2019-06-17 09:36
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IdGeneratorTest {

  @Autowired
  private IdGenerator idGenerator;

  @Test
  public void parallelTest() {
    Set<String> ids = Sets.newConcurrentHashSet();
    List<Thread> threads = Lists.newArrayList();
    for (int i = 0; i < 500; i++) {
      threads.add(new Thread(() -> {
        for (int j = 0; j < 100; j++) {
          String id = idGenerator.nextStr();
          log.info("id is {}", id);
          if (ids.contains(id)) {
            log.error("duplicate id, id is {}", id);
          }
          ids.add(id);
          try {
            Thread.sleep(100);
          } catch (InterruptedException ignored) {}
        }
      }, "thread-" + i));
    }
    threads.forEach(Thread::start);
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException ignored) {}
    }
    log.info("main exit");
  }

}
