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
        this.hostClass = hostClass.replace(".", "/");
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
                Label label0 = new Label();
                mv.visitLabel(label0);
                mv.visitLineNumber(28, label0);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
                Label label1 = new Label();
                mv.visitLabel(label1);
                mv.visitLineNumber(29, label1);
                mv.visitVarInsn(ALOAD, 0);
                mv.visitTypeInsn(NEW, implClass);
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, implClass, "<init>", "()V", false);
                mv.visitFieldInsn(PUTFIELD, hostClass, fieldName, "L" + interfaceClass + ";");
                Label label2 = new Label();
                mv.visitLabel(label2);
                mv.visitLineNumber(30, label2);
                mv.visitInsn(RETURN);
                Label label3 = new Label();
                mv.visitLabel(label3);
                mv.visitLocalVariable("this", "L" + hostClass + ";", null, label0, label3, 0);
                mv.visitMaxs(3, 1);
                mv.visitEnd();

            }

        }
    }
}

