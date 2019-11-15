/**
 * projectName: dlock
 * fileName: ServerBlock.java
 * packageName: com.moon.dlock.core.block
 * date: 2019-11-14 11:49
 * copyright(c) 2017-2020 同方赛威讯信息技术公司
 */
package com.moon.dlock.core.block;

/**
 * @version: V1.0
 * @author: 代浩然
 * @className: ServerBlock
 * @packageName: com.moon.dlock.core.block
 * @description: 实现服务阻塞类
 * @data: 2019-11-14 11:49
 **/
public class ServerBlock {

    private Object lock;

    private String blockName;/**阻塞代码块的名称，如果相同的代码块进行阻塞，该名称应该一致*/

    /**
     *
     * @param object
     */
    public ServerBlock(Object object,String blockName){
        this.lock = object;
        this.blockName = blockName;
    }

    /**
     * 等待阻塞
     */
    public void block(int expireTime)throws InterruptedException{
        synchronized (this.lock) {
            this.lock.wait(expireTime);
        }
    }

    /**
     * 线程就绪状态
     */
    public void ready(){
        synchronized (this.lock){
            /**
             * 唤醒该对象上的所有等待的线程
             */
            this.lock.notifyAll();

            //并且向其他服务发送唤醒线程的通知，实现方式：使用消息队列
            
        }
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}