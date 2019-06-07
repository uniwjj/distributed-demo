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
