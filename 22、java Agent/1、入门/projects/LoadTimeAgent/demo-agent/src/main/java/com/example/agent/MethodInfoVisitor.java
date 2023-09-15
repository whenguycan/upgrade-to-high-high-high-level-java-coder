package com.example.agent;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @Auther: tangwei
 * @Date: 2023/5/9 3:40 PM
 * @Description: 类描述信息
 */
public class MethodInfoVisitor extends ClassVisitor {
    private String owner;

    public MethodInfoVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !name.equals("<init>") && !name.equals("<clinit>")) {
            mv = new MethodInfoAdapter(mv, owner, access, name, descriptor);
        }
        return mv;
    }
}
