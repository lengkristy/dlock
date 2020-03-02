/**
 * projectName: dlock
 * fileName: DLockProperties.java
 * packageName: com.moon.dlock.config
 * date: 2019-11-12 10:34
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: DLockProperties
 * @packageName: com.moon.dlock.config
 * @description: 分布式锁的配置类，需要在application配置文件中配置，参照如下：
 *               dlock:
 *                 expireTime: 10000
 *
 * @data: 2019-11-12 10:34
 **/
@Component
@ConfigurationProperties(prefix = "dlock")
public class DLockProperties {

    /**
     * 过期时间
     */
    private String expireTime;

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}