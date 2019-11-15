/**
 * projectName: dlock
 * fileName: DLockService.java
 * packageName: com.moon.dlock.core
 * date: 2019-11-12 9:51
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core;

import com.moon.dlock.entity.DLockInfo;

/**
 * @version: V1.0
 * @author: 代浩然
 * @interfaceName: DLockService
 * @packageName: com.moon.dlock.core
 * @description: 分布式锁服务的接口定义
 * @data: 2019-11-12 9:51
 **/
public interface DLockService {

    /**
     * 尝试获取分布式锁
     * @param dLockInfo 锁信息
     * @return 获取成功则返回true，失败则返回false
     */
    boolean tryGetDistributedLock(DLockInfo dLockInfo)throws InterruptedException;

    /**
     * 释放分布式锁
     * @param dLockInfo 锁信息
     * @return 释放成功返回true，失败则返回false
     */
    boolean releaseDistributedLock(DLockInfo dLockInfo);
}