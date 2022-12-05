package com.gosec.customrpc.transform;

import io.github.classgraph.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
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
                        if (fieldAnnotation.getName().equals("com.gosec.customrpc.annotation.RPCService")) {
                            System.out.println("准备注入该方法");
                            // 生成实现类
                            String interfaceName = info.getTypeDescriptor().toString().replace(".","/");

                            String implClassName = interfaceName + "Impl";

                            String implClassPath = c.getClasspathElementFile().getAbsolutePath()+ File.separator + implClassName + ".class" ;
                            File implClassFile = new File(implClassPath);
                            implClassFile.createNewFile();
                            byte[] implClassDump = ServiceImplDump.dump(implClassName, interfaceName,info.loadClassAndGetField().getType().getMethods()[0].getName());
                            FileOutputStream implOutputStream = new FileOutputStream(implClassFile);
                            implOutputStream.write(implClassDump);
                            implOutputStream.close();

                            System.out.println("注入类完成");

                            String currClasspath = c.getClasspathElementFile().getAbsolutePath() + File.separator + c.getName().replace(".","/") + ".class";
                            File currClassFile = new File(currClasspath);
                            if (!currClassFile.exists()) {
                                System.out.println("文件不存在");
                            }
                            ClassReader classReader = new ClassReader(new FileInputStream(currClassFile));
                            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
                            //创建类访问器   并交给它去处理

                            RPCClassVisitor cv = new RPCClassVisitor(Opcodes.ASM5,classWriter, c.getName(), interfaceName, implClassName,fieldAnnotation.getName());
                            classReader.accept(cv, ClassReader.EXPAND_FRAMES);
                            byte[] code = classWriter.toByteArray();
                            FileOutputStream op = new FileOutputStream(currClassFile);
                            op.write(code);
                            op.close();

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
