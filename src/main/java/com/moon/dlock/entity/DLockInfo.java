/**
 * projectName: dlock
 * fileName: DLockInfo.java
 * packageName: com.moon.dlock.entity
 * date: 2019-11-12 9:54
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.entity;

import com.moon.dlock.enums.ObtainLockModel;

import java.io.Serializable;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLockInfo
 * @packageName: com.moon.dlock.entity
 * @description: 分布式锁定义，存放锁的相关信息
 * @data: 2019-11-12 9:54
 **/
public class DLockInfo implements Serializable{


    private static final long serialVersionUID = -1102572694299939716L;
    /**
     * 分布式锁的名称，该名称应该具有全局唯一性，
     * 如果是方法级别同步：用包名+类名+方法名
     * 如果是代码块级别同步：包名+类名+同步变量
     */
    private String name;

    /**请求者ID，请求获取分布式的ID，谁请求、谁释放*/
    private String requestId;

    /**过期时间，毫秒，为了防止获取到锁之后，服务挂掉，导致死锁，可以让redis自动释放锁*/
    private int expireTime;

    /**未获取到锁是否继续等待锁，默认为true，如果不等待那么立即返回，如果等待则会一直获取直到超时返回*/
    private boolean wait = true;

    /**获取锁的模式*/
    private ObtainLockModel obtainLockModel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public ObtainLockModel getObtainLockModel() {
        return obtainLockModel;
    }

    public void setObtainLockModel(ObtainLockModel obtainLockModel) {
        this.obtainLockModel = obtainLockModel;
    }
}