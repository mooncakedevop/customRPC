package com.gosec.customrpc.transform;

import io.github.classgraph.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mooncake
 * @Description 类工具
 * @create 2022/11/25 20:50
 */
public class ClassUtils {

    /**
     * 扫描指定方法注解
     *
     * @param pkg        扫描包
     * @param annotation 获取的注解类型
     * @return 返回注解参数 [{name:name,value:value}]
     */
    public static List<AnnotationParameterValueList> methodAnnotationScan(String pkg, Annotation annotation) {
        try (ScanResult scanResult =                // Assign scanResult in try-with-resources
                     new ClassGraph()                    // Create a new ClassGraph instance
                             .enableAllInfo()                // Scan classes, methods, fields, annotations
                             .acceptPackages(pkg)      // Scan com.xyz and subpackages
                             .scan()) {                      // Perform the scan and return a ScanResult
            // 获取类里指定方法注解
            ClassInfoList ciList = scanResult.getClassesWithMethodAnnotation(annotation.getClass());
            // 指定方法注解内容提取,提取流程: ClassInfoList -> ClassInfo -> MethodInfo -> AnnotationInfo -> ParameterValues -> AnnotationParameterValue
            return ciList.stream().flatMap(ci -> ci.getMethodInfo().stream().filter(me -> me.getAnnotationInfo(annotation.getClass()) != null)
                    .map(me -> me.getAnnotationInfo(annotation.getClass()).getParameterValues())).collect(Collectors.toList());
        }
    }

    public static void FieldAnnotationScan(String pkg, Class annotation) {
        try {
            ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(pkg).scan();
            ClassInfoList infoList = scanResult.getClassesWithFieldAnnotation(annotation);
            for (ClassInfo c : infoList) {
                for (FieldInfo info : c.getFieldInfo()) {
                    for (AnnotationInfo fieldAnnotation : info.getAnnotationInfo()) {
                        if (fieldAnnotation.getName().equals("com.duckgo.agent.RPCService")) {
                            System.out.println("准备注入该方法");
                            Host host = new Host();
                            host.className = c.getName();
                            host.fieldName = info.getName();
                            host.fieldClassName = info.getClassName();
                            host.method = info.loadClassAndGetField().getDeclaringClass().getMethods()[0];
                            // 生成实现类
                            injectServiceImpl(host);
                            System.out.println("生成实现类: " + host.fieldClassName + "Impl");
                            System.out.println("实现方法：" + host.method.getName());

                            //注入属性
                            injectField(host.fieldName, host.fieldClassName + "Impl");

                            System.out.println("完成注入");


                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class Host {
        String className;
        String fieldName;

        String fieldClassName;

        Method method;


    }

    public static void injectServiceImpl(Host host) {
        String serviceName = host.fieldClassName;
        String methodName = host.method.getName();
        Object[] args = host.method.getParameters();
    }

    public static void injectField(String fieldName, String serviceImpl) {
        newField(fieldName, serviceImpl);
    }

    public static void newField(String fieldName, String serviceImpl) {
        String content = "this." + fieldName + " = " + "new " + serviceImpl + "();";
    }

}
