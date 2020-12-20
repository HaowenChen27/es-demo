package com.chenhaowen.esdemo.zklock;


import org.I0Itec.zkclient.ZkClient;

/**
 * @author chenhaowen
 * @Description:
 * @date 2020/12/20 下午9:03
 */
public abstract class ZookeeperAbstractLock implements ExtLock {
    /**
     * zookeeper 地址
     */
    private static final String CONNECT_STRING = "10.211.55.3:2181";

    protected ZkClient zkClient = new ZkClient(CONNECT_STRING);


    /**
     * 等待获取锁
     */
    abstract void waitLock();

    /**
     * 获取锁
     */
    abstract boolean tryLock();

    @Override
    public void unLock() {
        if (zkClient != null) {
            zkClient.close();
            System.out.println("####释放锁完毕####");
        }
    }

    @Override
    public void getLock() {
        if (tryLock()) {
            System.out.println("####成功获取锁####");
        } else {
            waitLock();
        }

    }
}
