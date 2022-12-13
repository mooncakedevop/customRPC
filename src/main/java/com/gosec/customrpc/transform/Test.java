package com.gosec.customrpc.transform;

import com.gosec.customrpc.annotation.RPCService;
import com.gosec.customrpc.client.Client;
import com.gosec.customrpc.client.ClientChannelHolder;
import com.gosec.customrpc.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Test {
    private static boolean flag;
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, InterruptedException {
        String path = "/Users/mooncake/IdeaProjects/customRPC/target/classes";
        if (!flag) {
            ClassUtils.FieldAnnotationScan("com.gosec.customrpc.client", RPCService.class);
            flag = true;
        }
        MyClassLoader myClassLoader = new MyClassLoader(path + "com.gosec.customrpc.server.service.HelloServiceImpl");
        // load 实现类
        Class<?> impl = myClassLoader.loadClass("com.gosec.customrpc.server.service.HelloServiceImpl");

        // load clint
        Class<?> client = myClassLoader.loadClass("com.gosec.customrpc.client.Client");

        Client proxy =  (Client)client.newInstance();
        proxy.hello();

        System.out.println("类加载器是:" + impl.getClassLoader());
//        HelloService helloService = (HelloService) (impl.newInstance());
//        Bootstrap bootstrap = new Bootstrap();
//        ChannelFuture future = bootstrap.connect("localhost", 10000).sync();
//        ClientChannelHolder.CHANNEL_ATOMIC_REFERENCE.set(future.channel());
////         利用反射获取main方法
//        helloService.sayHello("1213");
//
////        HelloServiceImplTransform t = new HelloServiceImplTransform();
////        t.sayHello("1213");
//        Method method = Log.getDeclaredMethod("sayHello", String.class);
//        System.out.println(method.invoke("123"));

    }
}
