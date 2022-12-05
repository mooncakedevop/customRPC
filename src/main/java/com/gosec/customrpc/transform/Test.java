package com.gosec.customrpc.transform;

import com.gosec.customrpc.annotation.RPCService;
import com.gosec.customrpc.server.service.HelloService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Test {
    private static boolean flag;
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
//        String path = "/Users/mooncake/IdeaProjects/customRPC/target/classes";
//        if (!flag) {
//            ClassUtils.FieldAnnotationScan("com.gosec.customrpc.client", RPCService.class);
//            flag = true;
//        }
//        MyClassLoader myClassLoader = new MyClassLoader(path + "com.gosec.customrpc.server.service.HelloServiceImpl");
//        Class<?> Log = myClassLoader.loadClass("com.gosec.customrpc.server.service.HelloServiceImpl");
//        System.out.println("类加载器是:" + Log.getClassLoader());
//        HelloService helloService = (HelloService) (Log.newInstance());

        // 利用反射获取main方法
//        helloService.sayHello("1213");
        HelloServiceImplTransform t = new HelloServiceImplTransform();
        t.sayHello("1213");
//        Method method = Log.getDeclaredMethod("sayHello", String.class);
//        System.out.println(method.invoke("123"));

    }
}
