package com.moon.dlock.enums;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: ServerSync
 * @packageName: com.moon.dlock.enums
 * @description: 获取锁模式的枚举，分为阻塞、轮询
 *               如果是阻塞，那么该服务获取不到将会被阻塞，等待锁被释放掉之后通知唤醒。
 *               如果是轮询，那么该服务获取不到锁将会一直轮询获取锁，直到超时
 * @data: 2019-11-11 14:46
 **/
public enum ObtainLockModel {
    /**阻塞*/
    block,
    /**轮询*/
    polling
}
