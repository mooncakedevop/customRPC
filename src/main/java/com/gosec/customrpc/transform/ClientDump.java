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

public class ClientDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        RecordComponentVisitor recordComponentVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "com/gosec/customrpc/client/Client", null, "java/lang/Object", null);

        classWriter.visitSource("Client.java", null);

        classWriter.visitInnerClass("com/gosec/customrpc/client/Client$1", null, null, ACC_STATIC);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "log", "Lorg/slf4j/Logger;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE, "helloService", "Lcom/gosec/customrpc/server/service/HelloService;", null, null);
            {
                annotationVisitor0 = fieldVisitor.visitAnnotation("Lcom/gosec/customrpc/annotation/RPCService;", true);
                annotationVisitor0.visitEnd();
            }
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(32, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(33, label1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/transform/HelloServiceImpl");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/transform/HelloServiceImpl", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTFIELD, "com/gosec/customrpc/client/Client", "helloService", "Lcom/gosec/customrpc/server/service/HelloService;");
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(34, label2);
            methodVisitor.visitInsn(RETURN);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLocalVariable("this", "Lcom/gosec/customrpc/client/Client;", null, label0, label3, 0);
            methodVisitor.visitMaxs(3, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, new String[]{"java/lang/InterruptedException"});
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, null);
            Label label3 = new Label();
            methodVisitor.visitTryCatchBlock(label2, label3, label2, null);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(37, label4);
            methodVisitor.visitIntInsn(SIPUSH, 9092);
            methodVisitor.visitVarInsn(ISTORE, 1);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLineNumber(38, label5);
            methodVisitor.visitTypeInsn(NEW, "io/netty/channel/nio/NioEventLoopGroup");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "io/netty/channel/nio/NioEventLoopGroup", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLineNumber(39, label6);
            methodVisitor.visitTypeInsn(NEW, "io/netty/bootstrap/Bootstrap");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "io/netty/bootstrap/Bootstrap", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(42, label0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "group", "(Lio/netty/channel/EventLoopGroup;)Lio/netty/bootstrap/AbstractBootstrap;", false);
            methodVisitor.visitInsn(POP);
            Label label7 = new Label();
            methodVisitor.visitLabel(label7);
            methodVisitor.visitLineNumber(43, label7);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitLdcInsn(Type.getType("Lio/netty/channel/socket/nio/NioSocketChannel;"));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "channel", "(Ljava/lang/Class;)Lio/netty/bootstrap/AbstractBootstrap;", false);
            methodVisitor.visitInsn(POP);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitLineNumber(44, label8);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitFieldInsn(GETSTATIC, "io/netty/channel/ChannelOption", "SO_KEEPALIVE", "Lio/netty/channel/ChannelOption;");
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "option", "(Lio/netty/channel/ChannelOption;Ljava/lang/Object;)Lio/netty/bootstrap/AbstractBootstrap;", false);
            methodVisitor.visitInsn(POP);
            Label label9 = new Label();
            methodVisitor.visitLabel(label9);
            methodVisitor.visitLineNumber(45, label9);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitFieldInsn(GETSTATIC, "io/netty/channel/ChannelOption", "TCP_NODELAY", "Lio/netty/channel/ChannelOption;");
            methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "option", "(Lio/netty/channel/ChannelOption;Ljava/lang/Object;)Lio/netty/bootstrap/AbstractBootstrap;", false);
            methodVisitor.visitInsn(POP);
            Label label10 = new Label();
            methodVisitor.visitLabel(label10);
            methodVisitor.visitLineNumber(46, label10);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/client/Client$1");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/client/Client$1", "<init>", "()V", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "handler", "(Lio/netty/channel/ChannelHandler;)Lio/netty/bootstrap/AbstractBootstrap;", false);
            methodVisitor.visitInsn(POP);
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitLineNumber(74, label11);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitLdcInsn("localhost");
            methodVisitor.visitVarInsn(ILOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/netty/bootstrap/Bootstrap", "connect", "(Ljava/lang/String;I)Lio/netty/channel/ChannelFuture;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/ChannelFuture", "sync", "()Lio/netty/channel/ChannelFuture;", true);
            methodVisitor.visitVarInsn(ASTORE, 4);
            Label label12 = new Label();
            methodVisitor.visitLabel(label12);
            methodVisitor.visitLineNumber(75, label12);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/client/Client", "log", "Lorg/slf4j/Logger;");
            methodVisitor.visitLdcInsn("\u542f\u52a8nettyClient[{}]\u6210\u529f ...");
            methodVisitor.visitVarInsn(ILOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/slf4j/Logger", "info", "(Ljava/lang/String;Ljava/lang/Object;)V", true);
            Label label13 = new Label();
            methodVisitor.visitLabel(label13);
            methodVisitor.visitLineNumber(76, label13);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/client/ClientChannelHolder", "CHANNEL_ATOMIC_REFERENCE", "Ljava/util/concurrent/atomic/AtomicReference;");
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/ChannelFuture", "channel", "()Lio/netty/channel/Channel;", true);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/atomic/AtomicReference", "set", "(Ljava/lang/Object;)V", false);
            Label label14 = new Label();
            methodVisitor.visitLabel(label14);
            methodVisitor.visitLineNumber(79, label14);
            methodVisitor.visitTypeInsn(NEW, "com/gosec/customrpc/client/Client");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/client/Client", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 5);
            Label label15 = new Label();
            methodVisitor.visitLabel(label15);
            methodVisitor.visitLineNumber(80, label15);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/gosec/customrpc/client/Client", "hello", "()V", false);
            Label label16 = new Label();
            methodVisitor.visitLabel(label16);
            methodVisitor.visitLineNumber(82, label16);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/ChannelFuture", "channel", "()Lio/netty/channel/Channel;", true);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/Channel", "closeFuture", "()Lio/netty/channel/ChannelFuture;", true);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/ChannelFuture", "sync", "()Lio/netty/channel/ChannelFuture;", true);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(84, label1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/EventLoopGroup", "shutdownGracefully", "()Lio/netty/util/concurrent/Future;", true);
            methodVisitor.visitInsn(POP);
            Label label17 = new Label();
            methodVisitor.visitLabel(label17);
            methodVisitor.visitLineNumber(85, label17);
            Label label18 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label18);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(84, label2);
            methodVisitor.visitFrame(Opcodes.F_FULL, 4, new Object[]{"[Ljava/lang/String;", Opcodes.INTEGER, "io/netty/channel/EventLoopGroup", "io/netty/bootstrap/Bootstrap"}, 1, new Object[]{"java/lang/Throwable"});
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/EventLoopGroup", "shutdownGracefully", "()Lio/netty/util/concurrent/Future;", true);
            methodVisitor.visitInsn(POP);
            Label label19 = new Label();
            methodVisitor.visitLabel(label19);
            methodVisitor.visitLineNumber(85, label19);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitLabel(label18);
            methodVisitor.visitLineNumber(86, label18);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(RETURN);
            Label label20 = new Label();
            methodVisitor.visitLabel(label20);
            methodVisitor.visitLocalVariable("future", "Lio/netty/channel/ChannelFuture;", null, label12, label1, 4);
            methodVisitor.visitLocalVariable("c", "Lcom/gosec/customrpc/client/Client;", null, label15, label1, 5);
            methodVisitor.visitLocalVariable("args", "[Ljava/lang/String;", null, label4, label20, 0);
            methodVisitor.visitLocalVariable("port", "I", null, label5, label20, 1);
            methodVisitor.visitLocalVariable("workerGroup", "Lio/netty/channel/EventLoopGroup;", null, label6, label20, 2);
            methodVisitor.visitLocalVariable("bootstrap", "Lio/netty/bootstrap/Bootstrap;", null, label0, label20, 3);
            methodVisitor.visitMaxs(3, 7);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "hello", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(88, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/gosec/customrpc/client/Client", "helloService", "Lcom/gosec/customrpc/server/service/HelloService;");
            methodVisitor.visitLdcInsn("throwable");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "com/gosec/customrpc/server/service/HelloService", "sayHello", "(Ljava/lang/String;)Ljava/lang/String;", true);
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(89, label1);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/client/Client", "log", "Lorg/slf4j/Logger;");
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/slf4j/Logger", "info", "(Ljava/lang/String;)V", true);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(90, label2);
            methodVisitor.visitInsn(RETURN);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLocalVariable("this", "Lcom/gosec/customrpc/client/Client;", null, label0, label3, 0);
            methodVisitor.visitLocalVariable("result", "Ljava/lang/String;", null, label1, label3, 1);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC | ACC_SYNTHETIC, "access$000", "()Lorg/slf4j/Logger;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(28, label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/gosec/customrpc/client/Client", "log", "Lorg/slf4j/Logger;");
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(1, 0);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(27, label0);
            methodVisitor.visitLdcInsn(Type.getType("Lcom/gosec/customrpc/client/Client;"));
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/slf4j/LoggerFactory", "getLogger", "(Ljava/lang/Class;)Lorg/slf4j/Logger;", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "com/gosec/customrpc/client/Client", "log", "Lorg/slf4j/Logger;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }
}
