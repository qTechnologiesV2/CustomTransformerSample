
# qProtect Custom Transformer Sample
Sample to use with qProtect's custom transformer system

### How to use
 
 1.  Add the qProtect API to your maven pom.xml
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.qTechnologiesV2</groupId>
    <artifactId>qProtect-API</artifactId>
    <version>1.9.4</version>
</dependency>
```
2. Create a new Class that extends ClassTransformer and implement the methods from the superClass
3. Start writing your custom transformers in the method runOnClass using ObjectWeb ASM
4. Compile your jar and put into:
- Windows: %appdata%/qProtect/custom/
- Linux: /home/qProtect/custom/
- macOS: Library/Application Support/qProtect/custom/

