package com.moon.dlock.annots;

import com.moon.dlock.enums.ObtainLockModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: ServerSync
 * @packageName: com.moon.dlock.annots
 * @description: 服务同步的注解，在方法上面加注解之后，该方法将会参与到服务调用的同步中，
 *               使用该注解的同步方式为轮询的方式获取分布式锁
 * @data: 2019-11-11 14:46
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerSync {

    /**
     * 过期时间
     * @return
     */
    int expireTime() default 0;

    /**
     * 获取锁的模式，默认为轮询的方式
     * @return
     */
    ObtainLockModel syncModel() default ObtainLockModel.polling;
}
