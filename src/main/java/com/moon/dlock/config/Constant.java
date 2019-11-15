/**
 * projectName: dlock
 * fileName: Constant.java
 * packageName: com.moon.dlock.config
 * date: 2019-11-12 10:48
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.config;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: Constant
 * @packageName: com.moon.dlock.config
 * @description: 系统常量定义
 * @data: 2019-11-12 10:48
 **/
public class Constant {

    /**
     * 分布式锁的实现策略
     */
    public static class DLockStrategy{
        /**使用redis的方式实现*/
        public final static String REDIS = "redis";

        /**使用mysql的方式实现*/
        public final static String MYSQL = "mysql";
    }

    /**
     * activemq 相关配置
     */
    public static class ActiveMQ{
        public final static String MQ_DLOCK_RELEASE_TOPIC = "DLockRelease";
    }
}