package com.chenhaowen.esdemo.zkdemo;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author chenhaowen
 * @Description:
 * @date 2020/12/19 下午9:01
 */
public class WatchOne {

    private static final Logger logger = Logger.getLogger(WatchOne.class);

    private static final String CONNECT_STRING = "10.211.55.3:2181";

    private static final String PATH = "/zoo";

    private static final int SESSION_TIMEOUT = 50 * 1000;

    private ZooKeeper zk = null;

    public ZooKeeper startZk() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, event -> {

        });
    }

    public void stopZk() throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }

    public void createZNodes(String path, String nodeValue) throws KeeperException, InterruptedException {
        zk.create(path, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public String getZNode(String path) throws KeeperException, InterruptedException {
        byte[] byteArray = zk.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    triggerValue(path);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());
        return new String(byteArray);
    }

    public String triggerValue(String path) throws KeeperException, InterruptedException {
        byte[] byteArray = zk.getData(path, false, new Stat());
        String retValue = new String(byteArray);
        System.out.println("*****triggerValue:" + retValue);
        return retValue;
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        WatchOne watchOne = new WatchOne();
        watchOne.setZk(watchOne.startZk());
        if (watchOne.getZk().exists(PATH, false) == null) {
            watchOne.createZNodes(PATH, "AAAA");
            System.out.println("******" + watchOne.getZNode(PATH));
            Thread.sleep(Long.MAX_VALUE);
        } else {
            System.out.println("I have znode");
        }

    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
