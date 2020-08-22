package com.idea4j.apache.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ConnectionWatch {
    public static void main(String[] args) {
        String zkAddress = "127.0.0.1:2181";

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        client.start();

        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                switch (connectionState) {
                    case CONNECTED:
                        // 第一次成功连接到ZooKeeper之后会进入该状态。
                        // 对于每个CuratorFramework对象，此状态仅出现一次
                        break;
                    case SUSPENDED: //   ZooKeeper的连接丢失
                        break;
                    case RECONNECTED: // 丢失的连接被重新建立
                        break;
                    case LOST:
                        // 当Curator认为会话已经过期时，则进入此状态
                        break;
                    case READ_ONLY: // 连接进入只读模式
                        break;
                }
            }
        });

    }
}
