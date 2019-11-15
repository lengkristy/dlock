/**
 * projectName: dlock
 * fileName: LockAop.java
 * packageName: com.moon.dlock.core.aops
 * date: 2019-11-12 10:11
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core.aops;

import com.moon.dlock.annots.ServerSync;
import com.moon.dlock.config.DLockProperties;
import com.moon.dlock.core.DLockServiceImpl;
import com.moon.dlock.ecps.DLockException;
import com.moon.dlock.entity.DLockInfo;
import com.moon.util.UUIDUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: LockAop
 * @packageName: com.moon.dlock.core.aops
 * @description: aop方式加锁处理，在需要微服务同步的方法上面加上ServerSync注解，即可实现方法级别的同步
 * @data: 2019-11-12 10:11
 **/
@Aspect
@Component
public class LockAop {

    private static Logger LOGGER = LoggerFactory.getLogger(LockAop.class);

    /**
     * 表示该AOP只适用于该注解的方法
     */
    private static final String execution = "@annotation(com.moon.dlock.annots.ServerSync)";

    /**
     * 项目配置，读取yml配置
     */
    @Autowired
    private DLockProperties dLockProperties;

    /**
     * 分布式锁服务
     */
    @Autowired
    private DLockServiceImpl dLockService;


    /**
     * 切入点
     */
    @Pointcut(LockAop.execution)
    public void lock(){

    }

    @Around("LockAop.lock()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        //创建锁的信息
        DLockInfo dLockInfo = new DLockInfo();
        //读取注解信息
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //获取当前类的对象
        Class<?> clazz = pjp.getTarget().getClass();
        //获取当前类有ServerSync注解的方法
        method = clazz.getMethod(method.getName(), method.getParameterTypes());
        ServerSync serverSync = method.getAnnotation(ServerSync.class);
        if (serverSync.expireTime() > 0){
            dLockInfo.setExpireTime(serverSync.expireTime());
        }else{
            dLockInfo.setExpireTime(Integer.valueOf(dLockProperties.getExpireTime()));
        }
        dLockInfo.setObtainLockModel(serverSync.syncModel());
        dLockInfo.setName(method.getDeclaringClass().getName() + "." + method.getName());
        dLockInfo.setWait(true);
        dLockInfo.setRequestId(UUIDUtil.create32UUID());
        Object result = null;
        if (!dLockService.tryGetDistributedLock(dLockInfo)){
            throw new DLockException("无法获取分布式锁",dLockInfo);
        }
        try {
            // 执行进程
            result = pjp.proceed();
        }catch (Exception e){
            throw e;
        }finally {
            dLockService.releaseDistributedLock(dLockInfo);
        }
        return result;
    }
}