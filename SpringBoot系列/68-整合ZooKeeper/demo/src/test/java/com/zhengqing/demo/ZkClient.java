package com.zhengqing.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

public class ZkClient {
    private final static String CONNECTION_STRING = "localhost:2181";
    // 超时时间，60s
    private final static int SESSION_TIMEOUT = 60000;
    private ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        ZkClient client = new ZkClient();
        try {
            client.connect();

            String path = "/myNode-" + DateUtil.now();

            // 创建节点
            client.createNode(path, "Hello Zookeeper".getBytes());
            System.out.println("节点是否存在：" + client.exists(path));

            // 创建临时节点 特性：临时节点在其创建者会话结束时自动删除。因此，如果客户端断开连接且不再重连，则该节点将被自动删除。
            client.createNodeEphemeral("/test_tmp_node", "Hello".getBytes());
            System.out.println("临时节点是否存在：" + client.exists("/test_tmp_node"));

            // 读取节点数据
            byte[] data = client.readNode(path);
            System.out.println("Node data: " + new String(data));

            // 更新节点数据
            client.updateNode(path, "Hello Zookeeper Updated".getBytes());

            ThreadUtil.sleep(2, TimeUnit.SECONDS);

            // 删除节点
            client.deleteNode(path);
            System.out.println("节点是否存在：" + client.exists(path));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client.zooKeeper != null) {
                client.zooKeeper.close();
            }
        }
    }

    public void connect() throws Exception {
        zooKeeper = new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, event -> {
        });
    }

    public void createNode(String path, byte[] data) throws Exception {
        String result = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("Node created with path: " + result);
    }

    public void createNodeEphemeral(String path, byte[] data) throws Exception {
        String result = zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Node created with path: " + result);
    }

    public byte[] readNode(String path) throws Exception {
        return zooKeeper.getData(path, false, null);
    }

    public void updateNode(String path, byte[] data) throws Exception {
        zooKeeper.setData(path, data, -1);
        System.out.println("Node data updated successfully");
    }

    public void deleteNode(String path) throws Exception {
        zooKeeper.delete(path, -1);
        System.out.println("Node deleted successfully");
    }

    public boolean exists(String path) throws Exception {
        Stat stat = zooKeeper.exists(path, false);
        System.out.println("Node exists: " + stat);
        return stat != null;
    }

}