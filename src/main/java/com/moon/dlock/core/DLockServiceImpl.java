/**
 * projectName: dlock
 * fileName: DLockServiceImpl.java
 * packageName: com.moon.dlock.core
 * date: 2019-11-12 10:29
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core;

import com.moon.dlock.config.Constant;
import com.moon.dlock.config.DLockProperties;
import com.moon.dlock.core.block.BlockManager;
import com.moon.dlock.core.block.ServerBlock;
import com.moon.dlock.entity.DLockInfo;
import com.moon.dlock.enums.ObtainLockModel;
import com.moon.dlock.msgqueue.JMSProducer;
import com.moon.dlock.msgqueue.impl.JMSProducerActiveMqImpl;
import com.moon.dlock.redis.DLockServiceRedisImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLockServiceImpl
 * @packageName: com.moon.dlock.core
 * @description: 锁服务实现
 * @data: 2019-11-12 10:29
 **/
@Service
public class DLockServiceImpl implements DLockService{

    /**
     * 日志对象
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DLockServiceImpl.class);

    /**配置*/
    private DLockProperties dLockProperties;

    private DLockService dLockServiceRedis;

    private JMSProducer jmsProducer;


    @Autowired
    public DLockServiceImpl(DLockProperties dLockProperties, DLockServiceRedisImpl dLockServiceRedis,
                            JMSProducerActiveMqImpl jmsProducerActiveMq){
        this.dLockProperties = dLockProperties;
        this.dLockServiceRedis = dLockServiceRedis;
        this.jmsProducer = jmsProducerActiveMq;
    }

    @Override
    public boolean tryGetDistributedLock(DLockInfo dLockInfo) throws InterruptedException{
        DLockService dLockService = this.getService();
        Boolean ret = dLockService.tryGetDistributedLock(dLockInfo);
        if (!ret){
            if(!dLockInfo.isWait()){
                return ret;
            }
            //判断获取锁的模式
            if (ObtainLockModel.polling.equals(dLockInfo.getObtainLockModel())){//如果是轮询的方式获取锁
                long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
                while (true){
                    try {
                        LOGGER.info("获取分布式锁中...");
                        Thread.sleep(10);
                        ret = dLockService.tryGetDistributedLock(dLockInfo);
                        if(ret){
                            return ret;
                        }
                        long endMili = System.currentTimeMillis();
                        long timeDifference = endMili - startMili;
                        if (timeDifference > dLockInfo.getExpireTime()){
                            return ret;
                        }
                    }catch (Exception e){
                        return ret;
                    }
                }
            }else if(ObtainLockModel.block == dLockInfo.getObtainLockModel()){//如果是阻塞的方式获取锁
                //通过调用对象的wait方法实现线程挂起，然后等待被通知唤醒
                long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
                ServerBlock serverBlock = BlockManager.getServerBlock(dLockInfo.getName());
                if (serverBlock == null) {
                    serverBlock = new ServerBlock(this, dLockInfo.getName());
                }
                //将阻塞服务加入管理
                BlockManager.addServerBlock(serverBlock);
                //唤醒之后继续竞争分布式锁资源
                while (true){
                    try {
                        //将服务所对象
                        LOGGER.info("等待获取分布式锁:[" + dLockInfo.getName() + "]");
                        serverBlock.block(dLockInfo.getExpireTime());//进行阻塞
                        ret = dLockService.tryGetDistributedLock(dLockInfo);
                        if(ret){
                            LOGGER.info("获取到分布式锁:[" + dLockInfo.getName() + "]");
                            return ret;
                        }
                        long endMili = System.currentTimeMillis();
                        long timeDifference = endMili - startMili;
                        //如果等到超时了，那么返回失败，不能够获取分布式锁
                        if (timeDifference > dLockInfo.getExpireTime()){
                            return ret;
                        }
                    }catch (Exception e){
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean releaseDistributedLock(DLockInfo dLockInfo) {
        DLockService dLockService = this.getService();
        LOGGER.info("释放分布式锁:[" + dLockInfo.getName() + "]");
        Boolean ret = dLockService.releaseDistributedLock(dLockInfo);
        if (ObtainLockModel.block == dLockInfo.getObtainLockModel()){ //如果是阻塞锁，那么需要唤醒其他等待锁的服务
            try {
                this.jmsProducer.SendReleaseDLockMsg(dLockInfo);
            }catch (Exception e){
                LOGGER.error("发送释放分布式锁的消息失败",e);
            }
            ServerBlock serverBlock = BlockManager.getServerBlock(dLockInfo.getName());
            if (serverBlock != null){
                serverBlock.ready();
            }
        }
        return ret;
    }

    /**
     * 获取实现的具体服务
     */
    private DLockService getService(){
        if (Constant.DLockStrategy.REDIS.equals(dLockProperties.getStrategy())){
            return this.dLockServiceRedis;
        }
        return null;
    }
}