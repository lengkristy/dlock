/**
 * projectName: dlock
 * fileName: ActivemqConfig.java
 * packageName: com.moon.dlock.config
 * date: 2019-11-15 14:04
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: ActivemqConfig
 * @packageName: com.moon.dlock.config
 * @description: 消息队列配置
 * @data: 2019-11-15 14:04
 **/
@Configuration
public class ActivemqConfig {
    @Bean
    public Destination topicDLockReleaseMsg() {
        return new ActiveMQTopic(Constant.ActiveMQ.MQ_DLOCK_RELEASE_TOPIC);
    }

    /**
     * JmsListener注解默认只接收queue消息,如果要接收topic消息,需要设置containerFactory
     */
    @Bean
    public JmsListenerContainerFactory<?> topicListenerContainer(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory topicListenerContainer = new DefaultJmsListenerContainerFactory();
        topicListenerContainer.setPubSubDomain(true);
        topicListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        return topicListenerContainer;
    }
}