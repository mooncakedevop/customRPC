package com.gosec.customrpc.transform;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

public class RPCClassVisitor extends ClassVisitor {
    String hostClass;
    String interfaceClass;
    String implClass;
    private String fieldName;


    public RPCClassVisitor(int api, ClassVisitor classVisitor, String hostClass, String interfaceClass, String implClass, String fieldName) {
        super(api, classVisitor);
        this.hostClass = hostClass;
        this.interfaceClass = interfaceClass;
        this.implClass = implClass;
        this.fieldName = fieldName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        return new MyAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor);
    }

    class MyAdapter extends AdviceAdapter {

        /**
         * Creates a new {@link AdviceAdapter}.
         *
         * @param api    the ASM API version implemented by this visitor. Must be one
         *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
         * @param mv     the method visitor to which this adapter delegates calls.
         * @param access the method's access flags (see {@link Opcodes}).
         * @param name   the method's name.
         */
        protected MyAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
        }

        @Override
        public void invokeConstructor(Type type, Method method) {
            super.invokeConstructor(type, method);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (name.equals("<init>")) {
                System.out.println("11111");

                {
                    Label label0 = new Label();
                    mv.visitLabel(label0);
                    mv.visitLineNumber(32, label0);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                    Label label1 = new Label();
                    mv.visitLabel(label1);
                    mv.visitLineNumber(33, label1);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitInsn(POP);
//                    this.visitTypeInsn(NEW, "com/gosec/customrpc/transform/HelloServiceImpl");
                    mv.visitTypeInsn(NEW, implClass);

                    mv.visitInsn(DUP);

//                    this.visitMethodInsn(INVOKESPECIAL, "com/gosec/customrpc/transform/HelloServiceImpl", "<init>", "()V", false);
                    mv.visitMethodInsn(INVOKESPECIAL, implClass, "<init>", "()V", false);

//                    this.visitFieldInsn(PUTFIELD, "com/gosec/customrpc/client/Client", "helloService", "Lcom/gosec/customrpc/server/service/HelloService;");

                    mv.visitFieldInsn(PUTFIELD, hostClass, fieldName, "L" + interfaceClass + ";");

                    Label label2 = new Label();
                    mv.visitLabel(label2);
                    mv.visitLineNumber(34, label2);

                }
            }

        }
    }
}

