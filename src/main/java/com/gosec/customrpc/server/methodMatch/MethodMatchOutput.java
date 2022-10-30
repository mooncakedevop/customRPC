package com.gosec.customrpc.server.methodMatch;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class MethodMatchOutput {
    private Method targetMethod;
    private Class<?> targetKlass;
    private Class<?> targetUserKlass;
    private Object target;
    private List<Class<?>> parameterTypes;
}
