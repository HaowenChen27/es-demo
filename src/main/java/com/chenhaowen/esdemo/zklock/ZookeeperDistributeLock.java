package com.chenhaowen.esdemo.zklock;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenhaowen
 * @Description:
 * @date 2020/12/20 下午9:15
 */
public class ZookeeperDistributeLock extends ZookeeperAbstractLock {

    /**
     * path
     */
    private static final String lockPath = "/zoo/lock01";

    /**
     * 倒数器
     */
    private CountDownLatch countDownLatch;

    @Override
    void waitLock() {

        IZkDataListener iZkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        };
        //监听通知事件
        zkClient.subscribeDataChanges(lockPath, iZkDataListener);
        // 控制程序的等待
        if (zkClient.exists(lockPath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //删除掉监听事件
        zkClient.unsubscribeDataChanges(lockPath, iZkDataListener);

    }

    @Override
    boolean tryLock() {
        try {
            zkClient.createEphemeral(lockPath);
            return true;
        } catch (RuntimeException e) {
            //创建临时节点失败 直接返回失败
            return false;
        }
    }
}
