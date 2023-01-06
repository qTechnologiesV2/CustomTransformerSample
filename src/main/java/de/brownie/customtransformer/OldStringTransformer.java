package de.brownie.customtransformer;

import dev.mdma.qprotect.api.jar.JarFile;
import dev.mdma.qprotect.api.transformer.ClassTransformer;
import dev.mdma.qprotect.api.transformer.TransformException;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.concurrent.ThreadLocalRandom;

public class OldStringTransformer extends ClassTransformer {
    String DECRYPT_METHOD_NAME = "decrypt";
    public OldStringTransformer() {
        super("Old qProtect String Transformer", "Encrypted {} strings.");
    }

    @Override
    public boolean runOnClass(String s, ClassNode classNode, JarFile jarFile)  {
        classNode.methods.forEach(methodNode ->
                methodNode.instructions.forEach(abstractInsnNode -> {
                    Object cst;
                    if (!(abstractInsnNode instanceof LdcInsnNode) || !((cst = ((LdcInsnNode) abstractInsnNode).cst) instanceof String))
                        return;
                    String originalString = (String) ((LdcInsnNode) abstractInsnNode).cst;
                    int random = ThreadLocalRandom.current().nextInt(563848838);
                    int random2 = ThreadLocalRandom.current().nextInt(5485939);
                    int classHash = classNode.name.hashCode();
                    int methodHash = methodNode.name.hashCode() + random2 + classHash;

                    ((LdcInsnNode) abstractInsnNode).cst = encrypt(originalString, encryptCall(classNode.name), classHash, methodHash,
                            random, random2);

                    methodNode.instructions.insert(abstractInsnNode, new MethodInsnNode(184, classNode.name,
                            DECRYPT_METHOD_NAME, "(Ljava/lang/String;Ljava/lang/String;IIII)Ljava/lang/String;"));
                    methodNode.instructions.insert(abstractInsnNode, new LdcInsnNode(random2));
                    methodNode.instructions.insert(abstractInsnNode, new LdcInsnNode(random));
                    methodNode.instructions.insert(abstractInsnNode, new LdcInsnNode(methodHash));
                    methodNode.instructions.insert(abstractInsnNode, new LdcInsnNode(classHash));
                    methodNode.instructions.insert(abstractInsnNode, new LdcInsnNode(encryptCall(classNode.name)));
                    counter.incrementAndGet();
        }));
        classNode.methods.add(makeMethod());
        return false;
    }

    public static String encrypt(String msg, String className, int classHash, int methodHash, int random, int random2) {
        char[] messageChars = className.toCharArray();
        char[] newMessage = new char[messageChars.length];
        char[] XORKEY = new char[] { '\u4832', '\u2385', '\u2386', '\u9813', '\u9125', '\u4582', '\u0913', '\u3422',
                '\u0853', '\u0724' };
        char[] XORKEY2 = new char[] { '\u4820', '\u8403', '\u8753', '\u3802', '\u3840', '\u3894', '\u8739', '\u1038',
                '\u8304', '\u3333' };
        for (int j = 0; j < messageChars.length; ++j) {
            newMessage[j] = (char) (messageChars[j] ^ XORKEY[j % XORKEY.length]);
        }
        char[] decryptedmsg = new char[newMessage.length];
        for (int j = 0; j < messageChars.length; ++j) {
            decryptedmsg[j] = (char) (newMessage[j] ^ XORKEY2[j % XORKEY2.length]);
        }
        String decryptedClassName = new String(decryptedmsg);

        int key1 = decryptedClassName.hashCode();
        int key2 = methodHash - random2 - classHash;
        char[] chars = msg.toCharArray();
        char[] encrypted = new char[chars.length];

        for (int i = 0; i < encrypted.length; i++) {
            switch (i % 2) {
                case 0:
                    encrypted[i] = (char) (key2 ^ key1 ^ chars[i]);
                    break;
                case 1:
                    encrypted[i] = (char) (random2 ^ key2 ^ chars[i]);
                    break;
            }
        }

        return new String(encrypted);
    }

    public String encryptCall(String message) {
        try {
            char[] messageChars = message.toCharArray();
            char[] newMessage = new char[messageChars.length];
            char[] XORKEY = new char[] { '\u4832', '\u2385', '\u2386', '\u9813', '\u9125', '\u4582', '\u0913', '\u3422',
                    '\u0853', '\u0724' };
            char[] XORKEY2 = new char[] { '\u4820', '\u8403', '\u8753', '\u3802', '\u3840', '\u3894', '\u8739',
                    '\u1038', '\u8304', '\u3333' };
            for (int j = 0; j < messageChars.length; ++j) {
                newMessage[j] = (char) (messageChars[j] ^ XORKEY[j % XORKEY.length]);
            }
            char[] decryptedmsg = new char[newMessage.length];
            for (int j = 0; j < messageChars.length; ++j) {
                decryptedmsg[j] = (char) (newMessage[j] ^ XORKEY2[j % XORKEY2.length]);
            }
            return new String(decryptedmsg);
        } catch (Exception ignore) {
            return message;
        }
    }

    public MethodNode makeMethod() {
        MethodNode mv = new MethodNode(ACC_PUBLIC + ACC_STATIC, DECRYPT_METHOD_NAME,
                "(Ljava/lang/String;Ljava/lang/String;IIII)Ljava/lang/String;", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(54, l0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C", false);
        mv.visitVarInsn(ASTORE, 6);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(55, l1);
        mv.visitVarInsn(ALOAD, 6);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitIntInsn(NEWARRAY, T_CHAR);
        mv.visitVarInsn(ASTORE, 7);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(56, l2);
        mv.visitIntInsn(BIPUSH, 10);
        mv.visitIntInsn(NEWARRAY, T_CHAR);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitIntInsn(SIPUSH, 18482);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_1);
        mv.visitIntInsn(SIPUSH, 9093);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_2);
        mv.visitIntInsn(SIPUSH, 9094);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_3);
        mv.visitLdcInsn(new Integer(38931));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_4);
        mv.visitLdcInsn(new Integer(37157));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_5);
        mv.visitIntInsn(SIPUSH, 17794);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 6);
        mv.visitIntInsn(SIPUSH, 2323);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 7);
        mv.visitIntInsn(SIPUSH, 13346);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 8);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitLineNumber(57, l3);
        mv.visitIntInsn(SIPUSH, 2131);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 9);
        mv.visitIntInsn(SIPUSH, 1828);
        mv.visitInsn(CASTORE);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(56, l4);
        mv.visitVarInsn(ASTORE, 8);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(58, l5);
        mv.visitIntInsn(BIPUSH, 10);
        mv.visitIntInsn(NEWARRAY, T_CHAR);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_0);
        mv.visitIntInsn(SIPUSH, 18464);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_1);
        mv.visitLdcInsn(new Integer(33795));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_2);
        mv.visitLdcInsn(new Integer(34643));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_3);
        mv.visitIntInsn(SIPUSH, 14338);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_4);
        mv.visitIntInsn(SIPUSH, 14400);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitInsn(ICONST_5);
        mv.visitIntInsn(SIPUSH, 14484);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 6);
        mv.visitLdcInsn(new Integer(34617));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 7);
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitLineNumber(59, l6);
        mv.visitIntInsn(SIPUSH, 4152);
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 8);
        mv.visitLdcInsn(new Integer(33540));
        mv.visitInsn(CASTORE);
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, 9);
        mv.visitIntInsn(SIPUSH, 13107);
        mv.visitInsn(CASTORE);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLineNumber(58, l7);
        mv.visitVarInsn(ASTORE, 9);
        Label l8 = new Label();
        mv.visitLabel(l8);
        mv.visitLineNumber(60, l8);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 10);
        Label l9 = new Label();
        mv.visitLabel(l9);
        Label l10 = new Label();
        mv.visitJumpInsn(GOTO, l10);
        Label l11 = new Label();
        mv.visitLabel(l11);
        mv.visitLineNumber(61, l11);
        mv.visitFrame(Opcodes.F_FULL, 11,
                new Object[] { "java/lang/String", "java/lang/String", Opcodes.INTEGER, Opcodes.INTEGER,
                        Opcodes.INTEGER, Opcodes.INTEGER, "[C", "[C", "[C", "[C", Opcodes.INTEGER },
                0, new Object[] {});
        mv.visitVarInsn(ALOAD, 7);
        mv.visitVarInsn(ILOAD, 10);
        mv.visitVarInsn(ALOAD, 6);
        mv.visitVarInsn(ILOAD, 10);
        mv.visitInsn(CALOAD);
        mv.visitVarInsn(ALOAD, 8);
        mv.visitVarInsn(ILOAD, 10);
        mv.visitVarInsn(ALOAD, 8);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitInsn(IREM);
        mv.visitInsn(CALOAD);
        mv.visitInsn(IXOR);
        mv.visitInsn(I2C);
        mv.visitInsn(CASTORE);
        Label l12 = new Label();
        mv.visitLabel(l12);
        mv.visitLineNumber(60, l12);
        mv.visitIincInsn(10, 1);
        mv.visitLabel(l10);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, 10);
        mv.visitVarInsn(ALOAD, 6);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IF_ICMPLT, l11);
        Label l13 = new Label();
        mv.visitLabel(l13);
        mv.visitLineNumber(63, l13);
        mv.visitVarInsn(ALOAD, 7);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitIntInsn(NEWARRAY, T_CHAR);
        mv.visitVarInsn(ASTORE, 10);
        Label l14 = new Label();
        mv.visitLabel(l14);
        mv.visitLineNumber(64, l14);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 11);
        Label l15 = new Label();
        mv.visitLabel(l15);
        Label l16 = new Label();
        mv.visitJumpInsn(GOTO, l16);
        Label l17 = new Label();
        mv.visitLabel(l17);
        mv.visitLineNumber(65, l17);
        mv.visitFrame(Opcodes.F_FULL, 12,
                new Object[] { "java/lang/String", "java/lang/String", Opcodes.INTEGER, Opcodes.INTEGER,
                        Opcodes.INTEGER, Opcodes.INTEGER, "[C", "[C", "[C", "[C", "[C", Opcodes.INTEGER },
                0, new Object[] {});
        mv.visitVarInsn(ALOAD, 10);
        mv.visitVarInsn(ILOAD, 11);
        mv.visitVarInsn(ALOAD, 7);
        mv.visitVarInsn(ILOAD, 11);
        mv.visitInsn(CALOAD);
        mv.visitVarInsn(ALOAD, 9);
        mv.visitVarInsn(ILOAD, 11);
        mv.visitVarInsn(ALOAD, 9);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitInsn(IREM);
        mv.visitInsn(CALOAD);
        mv.visitInsn(IXOR);
        mv.visitInsn(I2C);
        mv.visitInsn(CASTORE);
        Label l18 = new Label();
        mv.visitLabel(l18);
        mv.visitLineNumber(64, l18);
        mv.visitIincInsn(11, 1);
        mv.visitLabel(l16);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, 11);
        mv.visitVarInsn(ALOAD, 6);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IF_ICMPLT, l17);
        Label l19 = new Label();
        mv.visitLabel(l19);
        mv.visitLineNumber(67, l19);
        mv.visitTypeInsn(NEW, "java/lang/String");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 10);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "([C)V", false);
        mv.visitVarInsn(ASTORE, 11);
        Label l20 = new Label();
        mv.visitLabel(l20);
        mv.visitLineNumber(69, l20);
        mv.visitVarInsn(ALOAD, 11);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "hashCode", "()I", false);
        mv.visitVarInsn(ISTORE, 12);
        Label l21 = new Label();
        mv.visitLabel(l21);
        mv.visitLineNumber(70, l21);
        mv.visitVarInsn(ILOAD, 3);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitInsn(ISUB);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitInsn(ISUB);
        mv.visitVarInsn(ISTORE, 13);
        Label l22 = new Label();
        mv.visitLabel(l22);
        mv.visitLineNumber(71, l22);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "toCharArray", "()[C", false);
        mv.visitVarInsn(ASTORE, 14);
        Label l23 = new Label();
        mv.visitLabel(l23);
        mv.visitLineNumber(72, l23);
        mv.visitVarInsn(ALOAD, 14);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitIntInsn(NEWARRAY, T_CHAR);
        mv.visitVarInsn(ASTORE, 15);
        Label l24 = new Label();
        mv.visitLabel(l24);
        mv.visitLineNumber(74, l24);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 16);
        Label l25 = new Label();
        mv.visitLabel(l25);
        Label l26 = new Label();
        mv.visitJumpInsn(GOTO, l26);
        Label l27 = new Label();
        mv.visitLabel(l27);
        mv.visitLineNumber(75, l27);
        mv.visitFrame(Opcodes.F_FULL, 17,
                new Object[] { "java/lang/String", "java/lang/String", Opcodes.INTEGER, Opcodes.INTEGER,
                        Opcodes.INTEGER, Opcodes.INTEGER, "[C", "[C", "[C", "[C", "[C", "java/lang/String",
                        Opcodes.INTEGER, Opcodes.INTEGER, "[C", "[C", Opcodes.INTEGER },
                0, new Object[] {});
        mv.visitVarInsn(ILOAD, 16);
        mv.visitInsn(ICONST_2);
        mv.visitInsn(IREM);
        Label l28 = new Label();
        Label l29 = new Label();
        Label l30 = new Label();
        mv.visitTableSwitchInsn(0, 1, l30, new Label[] { l28, l29 });
        mv.visitLabel(l28);
        mv.visitLineNumber(77, l28);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 15);
        mv.visitVarInsn(ILOAD, 16);
        mv.visitVarInsn(ILOAD, 13);
        mv.visitVarInsn(ILOAD, 12);
        mv.visitInsn(IXOR);
        mv.visitVarInsn(ALOAD, 14);
        mv.visitVarInsn(ILOAD, 16);
        mv.visitInsn(CALOAD);
        mv.visitInsn(IXOR);
        mv.visitInsn(I2C);
        mv.visitInsn(CASTORE);
        Label l31 = new Label();
        mv.visitLabel(l31);
        mv.visitLineNumber(78, l31);
        mv.visitJumpInsn(GOTO, l30);
        mv.visitLabel(l29);
        mv.visitLineNumber(80, l29);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 15);
        mv.visitVarInsn(ILOAD, 16);
        mv.visitVarInsn(ILOAD, 5);
        mv.visitVarInsn(ILOAD, 13);
        mv.visitInsn(IXOR);
        mv.visitVarInsn(ALOAD, 14);
        mv.visitVarInsn(ILOAD, 16);
        mv.visitInsn(CALOAD);
        mv.visitInsn(IXOR);
        mv.visitInsn(I2C);
        mv.visitInsn(CASTORE);
        mv.visitLabel(l30);
        mv.visitLineNumber(74, l30);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitIincInsn(16, 1);
        mv.visitLabel(l26);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ILOAD, 16);
        mv.visitVarInsn(ALOAD, 15);
        mv.visitInsn(ARRAYLENGTH);
        mv.visitJumpInsn(IF_ICMPLT, l27);
        Label l32 = new Label();
        mv.visitLabel(l32);
        mv.visitLineNumber(85, l32);
        mv.visitTypeInsn(NEW, "java/lang/String");
        mv.visitInsn(DUP);
        mv.visitVarInsn(ALOAD, 15);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "([C)V", false);
        mv.visitInsn(ARETURN);
        Label l33 = new Label();
        mv.visitLabel(l33);
        mv.visitMaxs(6, 17);
        mv.visitEnd();
        return mv;

    }
}
