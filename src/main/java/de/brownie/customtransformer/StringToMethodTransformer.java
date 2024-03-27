package de.brownie.customtransformer;

import de.brownie.customtransformer.utils.Utils;
import dev.mdma.qprotect.api.jar.JarFile;
import dev.mdma.qprotect.api.transformer.ClassTransformer;
import dev.mdma.qprotect.api.transformer.TransformException;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

//puts every String into a seperate method, requested by chef
public class StringToMethodTransformer extends ClassTransformer {
    //name is the TransformerName, message is the message in the qProtcet Console, where {} represents the current counter value
    public StringToMethodTransformer() {
        super("String to Method Transformer", "Put {} strings into methods.");
    }

    @Override
    public boolean runOnClass(String s, ClassNode classNode, JarFile jarFile) throws TransformException {
        //create an arrayList where we put all methodNodes we add after running the transformer, prevents ConcurrentModificationException
        ArrayList<MethodNode> toAdd = new ArrayList<>();
        //loop through all methodNodes and fitler to loop through all LdcInsnNodes only
        classNode.methods.forEach(methodNode -> Arrays.stream(methodNode.instructions.toArray()).filter(abstractInsnNode -> abstractInsnNode instanceof LdcInsnNode && ((LdcInsnNode) (abstractInsnNode)).cst instanceof String).forEach(abstractInsnNode -> {
            //get the string from the LdcInsnNode, we use this value for creating the method
            String inputString = (String) ((LdcInsnNode) abstractInsnNode).cst;
            //if the string is empty we ignore it
            if (inputString.isEmpty()) {
                return;
            }
            //create a random mehotdName
            //TODO: make qProtect-API able to return a random ClassName, MethodName or FieldName from the dictionary
            String methodName = Base64.getEncoder().encodeToString(Utils.getRandomString().getBytes());
            //create the methodNode, pass the inputString and random methodName
            MethodNode stringMethodNode = makeMethod(inputString, methodName);
            // add our methodNode to our list we created before
            toAdd.add(stringMethodNode);
            //replace the Strings in the methodNode with the MethodInsnNodes which calls our generated method
            methodNode.instructions.set(abstractInsnNode, new MethodInsnNode(INVOKESTATIC, classNode.name, methodName, "()Ljava/lang/String;"));
            //increase counter, used in the message defined in the constructor
            counter.incrementAndGet();
        }));
        // add all our methodNodes from the ArrayList to the classNode
        classNode.methods.addAll(toAdd);
        return true;
    }

    //our generated methodNodes
    public static MethodNode makeMethod(String value, String methodName) {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC | ACC_STATIC, methodName, "()Ljava/lang/String;", null, null);
        methodNode.visitCode();
        //this adds a new LdcInsNode with the inputString passed
        methodNode.visitLdcInsn(value);
        // areturn represents the end and return "<value>" in our method
        methodNode.visitInsn(ARETURN);
        methodNode.visitEnd();
        return methodNode;
    }
}
