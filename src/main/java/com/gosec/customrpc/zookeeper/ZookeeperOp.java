package com.gosec.customrpc.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ZookeeperOp {
    private static final String zkAddress = "localhost:2181";
    private static final ZkClient zkClient = new ZkClient(zkAddress);

    public static void register(String serviceName, String serviceAddress) {
        if (zkClient.exists(serviceName)) {
            zkClient.createPersistent(serviceName);
        }
        zkClient.createEphemeral(serviceName + "/" + serviceAddress);
        log.info("create node %s \n", serviceName + "/" + serviceAddress);
    }

    public static String discover(String serviceName) {
        List<String> children = zkClient.getChildren(serviceName);
        if (CollectionUtils.isEmpty(children)) {
            return "";
        }
        return children.get(ThreadLocalRandom.current().nextInt(children.size()));
    }
}