package com.gosec.customrpc.transform;

import com.gosec.customrpc.annotation.RPCService;
import com.gosec.customrpc.server.annotation.RpcServer;
import jdk.internal.org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Set;

public class RPCAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("agentArgs : " + agentArgs);
        inst.addTransformer(new DefineTransformer(), true);
    }

    static class DefineTransformer implements ClassFileTransformer {
        private boolean flag;

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if (!flag) {
                ClassUtils.FieldAnnotationScan("com.gosec.customrpc.client", RPCService.class);
                flag = true;
                System.out.println("premain load Class:" + className);
            }
            return classfileBuffer;
        }
    }
}
