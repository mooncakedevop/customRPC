package com.gosec.customrpc.server.methodMatch;

import lombok.Data;

@Data
public class HostClassMethodInfo {
    private Class<?> hostKlass;
    private Class<?> hostUserklass;
    private Object hostTarget;


}
