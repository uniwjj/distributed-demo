package com.dijiang.distributed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello控制器
 *
 * @author ginger
 * @create 2019-06-04 21:37
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

  @GetMapping("/say")
  public String sayHello(@RequestParam String name) {
    return "Hello, " + name + ".";
  }

}
