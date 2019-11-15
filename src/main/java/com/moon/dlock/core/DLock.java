/**
 * projectName: dlock
 * fileName: DLock.java
 * packageName: com.moon.dlock.core
 * date: 2019-11-15 15:11
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core;

import com.moon.dlock.ecps.DLockException;
import com.moon.dlock.entity.DLockInfo;
import com.moon.dlock.enums.ObtainLockModel;
import com.moon.util.UUIDUtil;
import org.springframework.stereotype.Component;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLock
 * @packageName: com.moon.dlock.core
 * @description: 分布式锁，在需要做分布式代码块同步的地方加上该锁即可
 * @data: 2019-11-15 15:11
 **/
@Component
public class DLock {

    /**
     * 分布式锁的服务
     */
    private DLockService dLockService;

    public DLock(DLockServiceImpl dLockService){
        this.dLockService = dLockService;
    }

    /**
     * 该方法是加锁方法，在需要分布式同步的代码块位置进行加锁，如果加锁成功，则返回加锁信息，
     * 该对象信息在释放锁的时候进行传入。锁的实现方式默认为轮询锁
     * 如果失败则抛出DLockException异常。
     * 在使用完锁之后，应该立即释放，调用unLock方法。
     * @param expireTime 锁的过期时间，分布式锁必须设置一个过期时间，为了防止死锁的情况，如果超过了这个时间，那么
     *                   锁将会被自动释放。
     * @return 成功则返回锁对象信息
     */
    public DLockInfo lock(int expireTime)throws DLockException,InterruptedException{
        DLockInfo dLockInfo = new DLockInfo();
        dLockInfo.setWait(true);
        dLockInfo.setExpireTime(expireTime);
        dLockInfo.setObtainLockModel(ObtainLockModel.polling);//采用轮询锁
        dLockInfo.setRequestId(UUIDUtil.create32UUID());
        //创建分布式锁名称，此处通过调用堆栈来创建分布式锁名称，需要确保所有服务的同步代码块是一致的，不然创建锁的名称
        //会存在问题
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < stackTrace.length;i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            if ("lock".equals(stackTraceElement.getMethodName())){
                stackTraceElement = stackTrace[i+1];
                String dlockName = stackTraceElement.getClassName() + ".";
                dlockName += stackTraceElement.getMethodName() + "_";
                dlockName += stackTraceElement.getLineNumber();
                dLockInfo.setName(dlockName);
                break;
            }
        }
        this.dLockService.tryGetDistributedLock(dLockInfo);
        return dLockInfo;
    }

    /**
     * 该方法是释放锁的方法，与lock方法应该成对出现，在用完锁之后应该立即释放。
     * @param dLockInfo
     */
    public void unLock(DLockInfo dLockInfo){
        this.dLockService.releaseDistributedLock(dLockInfo);
    }

}