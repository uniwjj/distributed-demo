package com.dijiang.distributed.controller;

import com.dijiang.distributed.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * id生成器控制器
 *
 * @author ginger
 * @create 2019-06-15 16:24
 */
@RestController
@RequestMapping("/generator")
public class GeneratorController {

  @Autowired
  private GeneratorService generatorService;

  @GetMapping("/nextId")
  private Long nextId() {
    return generatorService.nextId();
  }

}
