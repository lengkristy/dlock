/**
 * projectName: dlock
 * fileName: JMSProducer.java
 * packageName: com.moon.dlock.msgqueue
 * date: 2019-11-15 10:54
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.msgqueue;

import com.moon.dlock.entity.DLockInfo;

/**
 * @version: V1.0
 * @author: 代浩然
 * @interfaceName: JMSProducer
 * @packageName: com.moon.dlock.msgqueue
 * @description: 消息生产者，往消息队列发送消息
 *               比如：释放分布式锁的消息
 * @data: 2019-11-15 10:54
 **/
public interface JMSProducer {

    /**
     * 发送释放分布式锁的消息
     * @param dLockInfo
     * @throws Exception
     */
    void SendReleaseDLockMsg(DLockInfo dLockInfo)throws Exception;
}