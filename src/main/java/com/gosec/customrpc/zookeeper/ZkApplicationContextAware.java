package com.gosec.customrpc.zookeeper;

import com.google.common.collect.Maps;
import com.gosec.customrpc.server.annotation.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.logging.Logger;
@Slf4j
public class ZkApplicationContextAware implements ApplicationContextAware {

    @Value("${zk.address}")
    private String zookeeperAddress;
    @Value("${zk.port}")
    private int zookeeperPort;
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> rpcBeanMap = Maps.newHashMap();
        for (Object obj: ctx.getBeansWithAnnotation(RpcServer.class).values()) {
            rpcBeanMap.put("/" + obj.getClass().getAnnotation(RpcServer.class).cls().getName(), obj);
        }
        try {
            ZkServer.start(zookeeperAddress,zookeeperPort, rpcBeanMap);
        } catch (Exception e) {
            log.error("register error !", e);
        }
    }
}
