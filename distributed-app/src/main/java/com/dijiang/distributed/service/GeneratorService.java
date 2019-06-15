package com.dijiang.distributed.service;

import com.dijiang.distributed.generator.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 信号发生器业务层
 *
 * @author ginger
 * @create 2019-06-15 16:24
 */
@Slf4j
@Service
public class GeneratorService {

  @Autowired
  private IdGenerator idGenerator;

  public Long nextId() {
    return idGenerator.nextId();
  }

}
