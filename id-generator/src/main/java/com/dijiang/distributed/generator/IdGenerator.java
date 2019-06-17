package com.dijiang.distributed.generator;

/**
 * id生成器
 *
 * @author ginger
 * @create 2019-06-15 15:05
 */
public interface IdGenerator {

  /**
   * 下一个id
   *
   * @return 下一个id
   */
  Long nextId();

  /**
   * 下一个字符串
   *
   * @return 下一个字符串
   */
  String nextStr();

}