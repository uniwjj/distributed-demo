package com.dijiang.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 分布式应用启动类
 *
 * @author ginger
 * @create 2019-06-04 20:55
 */
@SpringBootApplication
public class DistributedApplication {

  public static void main(String[] args) {
    SpringApplication.run(DistributedApplication.class, args);
  }

}
