package com.gosec.customrpc.server.service;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestDynamicProxy {
    public static void main(String[] args) {
        Class<HelloService> interfaceClass = HelloService.class;
        InvocationHandler handler = new HelloServiceImpl(interfaceClass);
        HelloService helloService = (HelloService) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, handler);
        System.out.println(helloService.sayHello("throwable"));


    }
    private static class HelloServiceImpl implements InvocationHandler{
        private final Class<?> interfaceKlass;

        private HelloServiceImpl(Class<?> interfaceKlass) {
            this.interfaceKlass = interfaceKlass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return String.format("[%s#%s]方法被调用， 参数列表：%s", interfaceKlass.getName(), method.getName(),
                    JSON.toJSONString(args));
        }
    }
}
