/**
 * projectName: dlock
 * fileName: DLockException.java
 * packageName: com.moon.dlock.ecps
 * date: 2019-11-13 17:26
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.ecps;

import com.moon.dlock.entity.DLockInfo;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLockException
 * @packageName: com.moon.dlock.ecps
 * @description: 分布式锁异常
 * @data: 2019-11-13 17:26
 **/
public class DLockException extends Exception{

    /**分布式锁信息*/
    private DLockInfo dLockInfo;

    /**
     * 分布式锁异常
     * @param errmsg
     * @param dLockInfo
     */
    public DLockException(String errmsg, DLockInfo dLockInfo){
        super(errmsg);
        this.dLockInfo = dLockInfo;
    }

    public String getMessage() {
        String msg = super.getMessage();
        if (dLockInfo != null) {
            msg += "\r\n";
            msg += "分布式锁：[";
            msg += dLockInfo.getName();
            msg += "],模式：[";
            msg += dLockInfo.getObtainLockModel().toString();
            msg += "],";
            msg += "超时时间：[";
            msg += dLockInfo.getExpireTime();
            msg += "]";
        }
        return msg;
    }
}