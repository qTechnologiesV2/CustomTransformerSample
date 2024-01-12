
# qProtect Custom Transformer Sample
Sample to use with qProtect's custom transformer system

### How to use
 
 1.  Add the qProtect API to your maven pom.xml
```xml
<repositories>
    <repository>
        <id>mdma</id>
        <url>https://nexus.mdma.dev/repository/maven-releases/</url>
    </repository>
</repositories>

<dependency>
    <groupId>dev.mdma.qprotect</groupId>
    <artifactId>qprotect-api</artifactId>
    <version>1.11.0</version>
    <scope>provided</scope>
</dependency>
```
2. Create a new Class that extends ClassTransformer and implement the methods from the superClass
3. Start writing your custom transformers in the method runOnClass using ObjectWeb ASM
4. Compile your jar and put into:
- Windows: %appdata%/qProtect/custom/
- Linux: /home/qProtect/custom/
- macOS: Library/Application Support/qProtect/custom/

