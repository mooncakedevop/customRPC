package com.gosec.customrpc.transform;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;

public class ServiceImplDump implements Opcodes {

    public static byte[] dump(String implClasName, String interfaceName, String methodName) throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        RecordComponentVisitor recordComponentVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

//        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "com/gosec/customrpc/transform/HelloServiceImpl", null, "java/lang/Object", new String[]{"com/gosec/customrpc/server/service/HelloService"});
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, implClasName, null, "java/lang/Object", new String[]{interfaceName});

        classWriter.visitSource("HelloServiceImpl.java", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "EXTRACTOR", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractor;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(16, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
//            methodVisitor.visitLocalVariable("this", "Lcom/gosec/customrpc/transform/HelloServiceImpl;", null, label0, label1, 0);
            methodVisitor.visitLocalVariable("this", "L" + implClasName + ";", null, label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
//            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "sayHello", "(Ljava/lang/String;)Ljava/lang/String;", null, null);
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, methodName, "(Ljava/lang/String;)Ljava/lang/String;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(20, label0);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/client/extractor/RequestArgumentExtractInput");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractInput", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(21, label1);
            methodVisitor.visitVarInsn(ALOAD, 2);
//            methodVisitor.visitLdcInsn(Type.getType("Lcom/gosec/customrpc/server/service/HelloService;"));
            methodVisitor.visitLdcInsn(Type.getType("L" + interfaceName + ";"));

            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractInput", "setInterfaceKlass", "(Ljava/lang/Class;)V", false);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(22, label2);
            methodVisitor.visitVarInsn(ALOAD, 2);
//            methodVisitor.visitLdcInsn(Type.getType("Lcom/gosec/customrpc/server/service/HelloService;"));
            methodVisitor.visitLdcInsn(Type.getType("L" + interfaceName + ";"));

            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethods", "()[Ljava/lang/reflect/Method;", false);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitInsn(AALOAD);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractInput", "setMethod", "(Ljava/lang/reflect/Method;)V", false);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(23, label3);
//            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/transform/HelloServiceImpl", "EXTRACTOR", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractor;");
            methodVisitor.visitFieldInsn(GETSTATIC, implClasName, "EXTRACTOR", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractor;");

            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "com/gosec/customrpc/client/extractor/RequestArgumentExtractor", "extract", "(Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractInput;)Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractOutput;", true);
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(26, label4);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/protocol/message/RequestMessagePacket");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(27, label5);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setMagicNumber", "(I)V", false);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(28, label6);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setVersion", "(I)V", false);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLineNumber(29, label7);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitLdcInsn("1");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setSerialNumber", "(Ljava/lang/String;)V", false);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitLineNumber(30, label8);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/protocol/message/MessageType", "REQUEST", "Lcom/gosec/customrpc/protocol/message/MessageType;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setMessageType", "(Lcom/gosec/customrpc/protocol/message/MessageType;)V", false);
            Label label9 = new Label();
            methodVisitor.visitLabel(label9);
            methodVisitor.visitLineNumber(31, label9);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractOutput", "getInterfaceName", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setInterfaceName", "(Ljava/lang/String;)V", false);
            Label label10 = new Label();
            methodVisitor.visitLabel(label10);
            methodVisitor.visitLineNumber(32, label10);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractOutput", "getMethodName", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setMethodName", "(Ljava/lang/String;)V", false);
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitLineNumber(33, label11);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractOutput", "getMethodArgumentSignatures", "()Ljava/util/List;", false);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "toArray", "([Ljava/lang/Object;)[Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/String;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setMethodArgumentSignatures", "([Ljava/lang/String;)V", false);
            Label label12 = new Label();
            methodVisitor.visitLabel(label12);
            methodVisitor.visitLineNumber(34, label12);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/protocol/message/RequestMessagePacket", "setMethodArguments", "([Ljava/lang/Object;)V", false);
            Label label13 = new Label();
            methodVisitor.visitLabel(label13);
            methodVisitor.visitLineNumber(35, label13);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/client/ClientChannelHolder", "CHANNEL_ATOMIC_REFERENCE", "Ljava/util/concurrent/atomic/AtomicReference;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/atomic/AtomicReference", "get", "()Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "io/netty/channel/Channel");
            methodVisitor.visitVarInsn(ASTORE, 5);
            Label label14 = new Label();
            methodVisitor.visitLabel(label14);
            methodVisitor.visitLineNumber(36, label14);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/Channel", "writeAndFlush", "(Ljava/lang/Object;)Lio/netty/channel/ChannelFuture;", true);
            methodVisitor.visitInsn(POP);
            Label label15 = new Label();
            methodVisitor.visitLabel(label15);
            methodVisitor.visitLineNumber(37, label15);
            methodVisitor.visitLdcInsn("[%s#%s]\u8c03\u7528\u6210\u529f\uff0c \u53d1\u9001\u4e86[%s]\u5230Netty Server[%s]");
            methodVisitor.visitInsn(ICONST_4);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractOutput", "getInterfaceName", "()Ljava/lang/String;", false);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/extractor/RequestArgumentExtractOutput", "getMethodName", "()Ljava/lang/String;", false);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_2);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "com/alibaba/fastjson/JSON", "toJSONString", "(Ljava/lang/Object;)Ljava/lang/String;", false);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitInsn(ICONST_3);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/Channel", "remoteAddress", "()Ljava/net/SocketAddress;", true);
            methodVisitor.visitInsn(AASTORE);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/String", "format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            methodVisitor.visitInsn(ARETURN);
            Label label16 = new Label();
            methodVisitor.visitLabel(label16);
//            methodVisitor.visitLocalVariable("this", "Lcom/gosec/customrpc/transform/HelloServiceImpl;", null, label0, label16, 0);
            methodVisitor.visitLocalVariable("this", "L" + implClasName +";", null, label0, label16, 0);

            methodVisitor.visitLocalVariable("name", "Ljava/lang/String;", null, label0, label16, 1);
            methodVisitor.visitLocalVariable("input", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractInput;", null, label1, label16, 2);
            methodVisitor.visitLocalVariable("output", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractOutput;", null, label4, label16, 3);
            methodVisitor.visitLocalVariable("packet", "Lcom/gosec/customrpc/protocol/message/RequestMessagePacket;", null, label5, label16, 4);
            methodVisitor.visitLocalVariable("channel", "Lio/netty/channel/Channel;", null, label14, label16, 5);
            methodVisitor.visitMaxs(5, 6);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(17, label0);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/client/extractor/DefaultRequestArgumentExtractor");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/client/extractor/DefaultRequestArgumentExtractor", "<init>", "()V", false);
//            methodVisitor.visitFieldInsn(PUTSTATIC, "com/gosec/customrpc/transform/HelloServiceImpl", "EXTRACTOR", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractor;");
            methodVisitor.visitFieldInsn(PUTSTATIC, implClasName, "EXTRACTOR", "Lcom/gosec/customrpc/client/extractor/RequestArgumentExtractor;");

            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}

