package com.idea4j.apache.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.time.LocalDateTime;
import java.util.List;

public class Cache {
    public static void main(String[] args) throws Exception {
        String zkAddress = "127.0.0.1:2181";
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        client.start();

        NodeCache nodeCache = new NodeCache(client, "/user");
        nodeCache.start(true);

        if (nodeCache.getCurrentData() != null) { 
            System.out.println("NodeCache节点初始化数据为："
                    + new String(nodeCache.getCurrentData().getData())); 
        } else { 
            System.out.println("NodeCache节点数据为空"); 
        }

        nodeCache.getListenable().addListener(() ->{
            String data = new String(nodeCache.getCurrentData().getData()); 
            System.out.println("NodeCache节点路径：" + nodeCache.getCurrentData().getPath()
                                        + "，节点数据为：" + data);
        });

        PathChildrenCache childrenCache = new PathChildrenCache(client, "/user", true);

        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        List<ChildData> children = childrenCache.getCurrentData();
        System.out.println("获取子节点列表：");

        children.forEach(childData -> {
            System.out.println(new String(childData.getData()));
        });

        childrenCache.getListenable().addListener((client1, event) ->{
            System.out.println(LocalDateTime.now() + "  " + event.getType());
            if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                System.out.println("PathChildrenCache:子节点初始化成功..."); 
            } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) { 
                String path = event.getData().getPath(); 
                System.out.println("PathChildrenCache添加子节点:" + event.getData().getPath()); 
                System.out.println("PathChildrenCache子节点数据:" + new String(event.getData().getData())); 
            } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) { 
                System.out.println("PathChildrenCache删除子节点:" + event.getData().getPath()); 
            } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) { 
                System.out.println("PathChildrenCache修改子节点路径:" + event.getData().getPath()); 
                System.out.println("PathChildrenCache修改子节点数据:" + new String(event.getData().getData())); 
            }
        });

        TreeCache cache = TreeCache.newBuilder(client, "/user").setCacheData(false).build();
        cache.getListenable().addListener((c,event)->{
            if (event.getData() != null) { 
                System.out.println("TreeCache,type=" + event.getType() + " path=" + event.getData().getPath()); 
            } else { 
                System.out.println("TreeCache,type=" + event.getType()); 
            }
        });

        cache.start();
        System.in.read();

    }
}
