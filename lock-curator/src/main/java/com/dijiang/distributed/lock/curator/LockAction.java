package com.dijiang.distributed.lock.curator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AliasFor;

/**
 * 锁行为
 *
 * 注意：
 *    ①扫描包时，一定要扫描到com.netease.edu100.common.util.lock包；
 *    ②因注解使用了AOP，注意AOP生效条件；
 *    ③注意需要增加zk配置才会生成相应bean；
 *    ④获取锁超时或者不为主节点时，均会抛出异常；
 *    ⑤不使用注解的方式参照AOP实现。
 *
 * @author ginger
 * @create 2019-06-07 13:38
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LockAction {

  /**
   * 锁资源，支持Spring EL表达式
   */
  @AliasFor("key")
  String value() default "'default'";

  /**
   * 锁资源，支持Spring EL表达式
   */
  @AliasFor("value")
  String key() default "'default'";

  /**
   * 锁类型，默认可重入锁
   */
  LockType lockType() default LockType.REENTRANT_LOCK;

  /**
   * 获取锁的等待时间，默认10秒
   */
  long waitTime() default 10000L;

  /**
   * 时间单位，默认毫秒
   */
  TimeUnit unit() default TimeUnit.MILLISECONDS;

}
