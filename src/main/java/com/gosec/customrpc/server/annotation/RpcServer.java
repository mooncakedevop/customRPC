package com.gosec.customrpc.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Component
public @interface RpcServer {
    /**
     * 接口类 用于服务注册
     */
    Class<?> cls();
}
