/**
 * projectName: dlock
 * fileName: JMSProducerActiveMqImpl.java
 * packageName: com.moon.dlock.msgqueue.impl
 * date: 2019-11-15 10:57
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.msgqueue.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moon.dlock.entity.DLockInfo;
import com.moon.dlock.msgqueue.JMSProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: JMSProducerActiveMqImpl
 * @packageName: com.moon.dlock.msgqueue.impl
 * @description: 发送消息队列消息，使用activemq中间件实现
 * @data: 2019-11-15 10:57
 **/
@Service
public class JMSProducerActiveMqImpl implements JMSProducer {


    @Resource(name = "topicDLockReleaseMsg")
    private Destination topicDLockReleaseMQ;

    private JmsTemplate jmsTemplate;

    @Autowired
    public JMSProducerActiveMqImpl(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }


    @Override
    public void SendReleaseDLockMsg(DLockInfo dLockInfo) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String resultJson = mapper.writeValueAsString(dLockInfo);
        this.jmsTemplate.convertAndSend(topicDLockReleaseMQ,resultJson);
    }
}