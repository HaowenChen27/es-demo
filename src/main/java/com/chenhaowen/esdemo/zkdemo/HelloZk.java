package com.chenhaowen.esdemo.zkdemo;


import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author chenhaowen
 * @Description:
 * @date 2020/12/19 下午6:10
 */
public class HelloZk {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(HelloZk.class);

    private static final String CONNECT_STRING = "10.211.55.3:2181";

    private static final String PATH = "/zookeeper";

    private static final int SESSION_TIMEOUT = 50 * 1000;


    public ZooKeeper startZK() throws IOException {
        return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });
    }

    public void stopZK(ZooKeeper zk) throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }

    public void createZNode(ZooKeeper zk, String path, String nodeValue) throws KeeperException, InterruptedException {
        zk.create(path, nodeValue.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public String getZNode(ZooKeeper zk, String path) throws KeeperException, InterruptedException {
        byte[] data = zk.getData(path, false, new Stat());
        return new String(data);

    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        HelloZk hello = new HelloZk();
        ZooKeeper zk = hello.startZK();
        Stat stat = zk.exists(PATH, false);
        if (stat == null) {
            hello.createZNode(zk, PATH, "zk1014");
            String result = hello.getZNode(zk, PATH);
            System.out.println("result :" + result);
        } else {
            System.out.println("znode has already ok");
            //在 zookeeper 下新建一个新的节点 barry
            hello.createZNode(zk, "/zookeeper/barry", "321");
        }

    }

}
