package de.brownie.customtransformer;

import dev.mdma.qprotect.api.jar.JarFile;
import dev.mdma.qprotect.api.transformer.ClassTransformer;
import dev.mdma.qprotect.api.transformer.TransformException;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;

//Sample transformer that adds useless NOP instuctions
public class NopSpamTransformer extends ClassTransformer {

    //Set the transformername and the message after obfuscation finish
    public NopSpamTransformer() {
        super("NopSpamTransformer", "Added {} NOP instructions");
    }

    @Override
    public boolean runOnClass(String className, ClassNode classNode, JarFile jarFile) throws TransformException {
        //loop through methods
        classNode.methods.forEach(methodNode ->
                //loop through instructions
                methodNode.instructions.forEach(abstractInsnNode -> {
                    //Add useless NOP instruction before each real instruction
                methodNode.instructions.insertBefore(abstractInsnNode, new InsnNode(Opcodes.NOP));
                counter.incrementAndGet();
        }));

        //return true if class was modified
        return true;
    }
}
