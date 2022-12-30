
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
    <version>1.9.2</version>
</dependency>
```
2. Create a new Class that extends ClassTransformer and implement the methods from the superClass
3. Change the ExclusionType for your Custom Transformers to ExclusionType.CUSTOM
```java
@Override  
public ExclusionType getExclusionType() {  
    return ExclusionType.CUSTOM;  
}
```
4. Start writing your custom transformers in the method runOnClass using ObjectWeb ASM
5. Compile your jar and rename the output to custom.jar
6. Put the output into:
- Windows: %appdata%/qProtect/
- Linux: /home/qProtect/
- macOS: Library/Application Support/qProtect/

