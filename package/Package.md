# Package

## 基础

Java平台的类型属于各种按功能打包的包的成员:基本类在`java.lang`中,用于读写(输入和输出)的类在 `java.io`中,等等。你也可以将你的类型放在包中。也就是说,Java 标准库中的类型(String、ArrayList、Socket 等)被组织到不同的包中,这些包都是根据类型的功能来组织的。例如:- java.lang 包中包含最基本的类型,如 String、Math、Integer 等

- java.util 包中包含集合类型,如 ArrayList、HashMap 等
- java.net包中包含网络相关的类型,如 Socket、URL 等
- java.io包中包含输入/输出相关的类型,如 File、InputStream 等
- java.lang包 反射 变量类型

Java使用包（package）是为了防止命名冲突，也为了更好的组织类，方便查找和使用类。

关于在实际场景中该如何根据需求来定义package,可以看oracle官方这个文档 [https://docs.oracle.com/javase/tutorial/java/package/packages.html](https://docs.oracle.com/javase/tutorial/java/package/packages.html)

## Reference

- [https://docs.oracle.com/javase/tutorial/java/index.html](https://docs.oracle.com/javase/tutorial/java/index.html)