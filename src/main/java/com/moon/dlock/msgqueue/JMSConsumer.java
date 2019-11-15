/**
 * projectName: dlock
 * fileName: JMSConsumer.java
 * packageName: com.moon.dlock.msgqueue
 * date: 2019-11-14 18:02
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.msgqueue;

import com.alibaba.fastjson.JSON;
import com.moon.dlock.config.Constant;
import com.moon.dlock.core.block.BlockManager;
import com.moon.dlock.core.block.ServerBlock;
import com.moon.dlock.entity.DLockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: JMSConsumer
 * @packageName: com.moon.dlock.msgqueue
 * @description: 消息消费者，用于接收其他服务发送的释放锁的消息，该服务接收到释放锁的消息之后，将会查找在该锁上等待的线程，然后全部唤醒
 * @data: 2019-11-14 18:02
 **/
@Service
public class JMSConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(JMSConsumer.class);

    /**
     * 订阅其他服务发送的释放分布式锁的消息
     * @param message
     */
    @JmsListener(destination= Constant.ActiveMQ.MQ_DLOCK_RELEASE_TOPIC, containerFactory="topicListenerContainer")
    public void receiveWebsocketQueue(String message) {
        //反序列化锁的信息
        DLockInfo dLockInfo = JSON.parseObject(message, DLockInfo.class);
        LOGGER.info("收到释放分布式锁的消息，锁名称[" + dLockInfo.getName() + "]");
        //开始唤醒在该锁上所有等待的线程
        ServerBlock serverBlock = BlockManager.getServerBlock(dLockInfo.getName());
        if (serverBlock != null){
            //唤醒线程去竞争锁，这里的锁是非公平锁，所有服务平等竞争
            serverBlock.ready();
        }
    }
}