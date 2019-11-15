/**
 * projectName: dlock
 * fileName: BlockManager.java
 * packageName: com.moon.dlock.core.block
 * date: 2019-11-14 14:00
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core.block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: BlockManager
 * @packageName: com.moon.dlock.core.block
 * @description: 阻塞管理器，管理所有的阻塞对象
 * @data: 2019-11-14 14:00
 **/
public class BlockManager {

    /**
     * 阻塞服务集合,用于缓存以便查找相同的阻塞块
     */
    private static Map<String,ServerBlock> serverBlockMap = new ConcurrentHashMap<>();

    /**
     * 通过名称获取分布式锁
     * @param dLockName 分布式锁的名称
     * @return 查询到则返回具体的阻塞服务，没有查询到则返回null
     */
    public static ServerBlock getServerBlock(String dLockName){
        if (serverBlockMap.containsKey(dLockName)){
            return serverBlockMap.get(dLockName);
        }
        return null;
    }

    /**
     * 添加服务阻塞
     * @param serverBlock
     */
    public static void addServerBlock(ServerBlock serverBlock){
        if (serverBlockMap.containsKey(serverBlock.getBlockName())){
            serverBlockMap.remove(serverBlock.getBlockName());
            serverBlockMap.put(serverBlock.getBlockName(),serverBlock);
            return;
        }
        serverBlockMap.put(serverBlock.getBlockName(),serverBlock);
    }
}