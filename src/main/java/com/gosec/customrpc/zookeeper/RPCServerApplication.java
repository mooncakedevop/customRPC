package com.gosec.customrpc.zookeeper;

import com.gosec.customrpc.server.ServerApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RPCServerApplication {
    public static void main(String[] args) {
//        new SpringApplicationBuilder(RPCServerApplication.class)
//                .web(WebApplicationType.NONE)
//                .run(args);
        new SpringApplicationBuilder(ServerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
