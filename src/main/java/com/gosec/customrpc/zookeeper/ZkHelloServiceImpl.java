package com.gosec.customrpc.zookeeper;

import com.gosec.customrpc.server.annotation.RpcServer;
import com.gosec.customrpc.server.service.HelloService;
import org.springframework.stereotype.Service;

@RpcServer(cls = ZkHelloServiceImpl.class)
@Service
public class ZkHelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "zooKeeper say hello";
    }
}
