package com.chenhaowen.esdemo.zklock;

/**
 * @author chenhaowen
 * @Description:
 * @date 2020/12/20 下午9:01
 */
public interface ExtLock {

    /**
     * 获取锁
     */
    void getLock();


    /**
     * 释放锁
     */
    void unLock();
}
