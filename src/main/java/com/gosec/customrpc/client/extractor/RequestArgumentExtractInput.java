package com.gosec.customrpc.client.extractor;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RequestArgumentExtractInput {
    private Class<?> interfaceKlass;
    private Method method;
}