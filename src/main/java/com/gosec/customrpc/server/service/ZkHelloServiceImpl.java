package com.gosec.customrpc.server.service;

import com.gosec.customrpc.server.annotation.RpcServer;

@RpcServer(cls = HelloService.class)
public class ZkHelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String name) {
        return "zooKeeper say hello";
    }
}
