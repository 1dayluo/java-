# java篇 反射

学习笔记来自：[https://fhfirehuo.github.io/Attacking-Java-Rookie/Chapter05/reflection.html](https://fhfirehuo.github.io/Attacking-Java-Rookie/Chapter05/reflection.html)

<aside>
💡 运行在内存中的所有类都是Class类的实例对象

</aside>

<aside>
💡 每一个类都会有属于自己的class对象

</aside>

## 什么是反射

JAVA反射机制是在运行状态中，**对于任意一个类**，都能够知道这个类的所有属性和方法；**对于任意一个对象，**都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为Java语言的反射机制。

。Java让我们在运行时识别对象和类的信息， 主要有两种方式：

- 一种是传统的RTTI（Run-Time Type Identification)，它假定我们在编译时已经知道了所有的类型信息；
- 另一种是反射机制，它允许我们在运行时发现和使用类的信息。(也就是不知道类名的情况下 , 可以利用反射来实现new实例)

![Untitled](java%E7%AF%87%20%E5%8F%8D%E5%B0%84/Untitled.png)

反射，

jvm加载到对应的class文件 →  jvm 读取本地对应的 `.class`文件，并放置jvm内存中 → 将 `.class` 文件读入内存 → 自动创建一个class对象 →读取到class对象后，反向获取原始类的各种信息

## 反射使用的需求

```java
public Map<Integer, Integer> getMap(String className) {
    Class clazz = Class.forName(className);
    Consructor con = clazz.getConstructor();
    return (Map<Integer, Integer>) con.newInstance();
}
```

如例子，无论使用什么 `Map` ，只要实现了Map接口，就可以使用全类名路径传入到方法中，获得对应的 Map 实例(感觉是这样抽象化更高一些)

这里没用到 `invoke` , 再举个例子

## java反射的组成

- Class：
- Field：描述一个类的属性，内部包含了该属性的所有信息，例如数据类型，属性名，访问修饰符······
- Constructor：描述一个类的构造方法，内部包含了构造方法的所有信息，例如参数类型，参数名字，访问修饰符······
- Method：描述一个类的构造方法，内部包含了构造方法的所有信息，例如参数类型，参数名字，访问修饰符······

### 获取class对象的几个方法

1. `.class`  使用类名.class

这种获取方式只有在编译前已经声明了该类的类型才能获取到 Class 对象

```java
Class clazz = SmallPineapple.class;
```

1. `getClass()`通过实例化对象获取Class对象：

```java
Class clazz = sp.getClass();
```

1. `Class.forName(className)` 通过类的全限定名获取该类的Class对象

```java
Class clazz = Class.forName("com.bean.smallpineapple");
```

可以获取类信息，调用方法，获取属性

### 获取构造类的实例化对象

获取到类对象后

1. 调用 `newInstance()` 方法

但通过该方法创建的实例，所有属性都是对应类型的初始值，因为 `newInstance()` 构造实例会调用无参构造器

（通过Class对象调用的newInstance()会默认走无参构造方法）

```java
Class clazz = Class.forName("reflect.SmallPineapple");
        SmallPineapple smallPineapple = (SmallPineapple) clazz.newInstance();
        smallPineapple.getInfo();
```

1. 调用 `getConstructor()` 方法

使用该方法获取指定参数类型的Constructor，再使用该 `Constructor` 调用 `newinstance`时传入构造方法参数的值。

```java
Class clazz = Class.forName("reflect.SmallPineapple");
//        SmallPineapple smallPineapple = (SmallPineapple) clazz.newInstance();
//        smallPineapple.getInfo();

        Constructor constructor = clazz.getConstructor(String.class, int.class);
        constructor.setAccessible(true);
        SmallPineapple smallPineapple2 = (SmallPineapple) constructor.newInstance("小菠萝", 21);
        smallPineapple2.getInfo();
```

## 获取类内的所有信息

Class对象中包含了：

- 类的变量
- 方法
- 构造器

每种功能内部以 Declared 细分为2类：

> 有Declared修饰的方法：可以获取该类内部包含的所有变量、方法和构造器，但是无法获取继承下来的信息
> 
> 
> 无Declared修饰的方法：可以获取该类中public修饰的变量、方法和构造器，可获取继承下来的信息
> 

<aside>
💡 如果想获取类中所有的（包括继承）变量、方法和构造器，则需要同时调用getXXXs()和getDeclaredXXXs()两个方法，用Set集合存储它们获得的变量、构造器和方法，以防两个方法获取到相同的东西

</aside>

### 类中的变量（Field）

- `Field[] getFields()` : 获取类中所有被public修饰的所有变量
- `Field getField(String name)` : 根据变量名获取类中的一个变量，该变量必须被public修饰
- `Field[] getDeclaredFields()` ：获取类中所有的变量，但无法获取继承下来的变量
- `Field getDeclaredField(String name)`：根据姓名获取类中的某个变量，无法获取继承下来的变量

### 获取类中的方法（Method）

- `Method[] getMethods()`：获取类中被public修饰的所有方法
- `Method getMethod(String name, Class...<?> paramTypes)`：根据名字和参数类型获取对应方法，该方法必须被public修饰
- `Method[] getDeclaredMethods()`：获取所有方法，但无法获取继承下来的方法
- `Method getDeclaredMethod(String name, Class...<?> paramTypes)`：根据名字和参数类型获取对应方法，无法获取继承下来的方法

### 获取类的构造器（Constructor）

- `Constuctor[] getConstructors()`：获取类中所有被public修饰的构造器
- `Constructor getConstructor(Class...<?> paramTypes)`：根据参数类型获取类中某个构造器，该构造器必须被public修饰
- `Constructor[] getDeclaredConstructors()`：获取类中所有构造器
- `Constructor getDeclaredConstructor(class...<?> paramTypes)`：根据参数类型获取对应的构造器

以上就是Class对象的三种信息

## 获取注解

注解不是Class对象下的信息，每个变量，方法和构造器都可以被注解修饰。在反射中， `Field` , `Constructor` 和 `Method` 类对象都可以调用下面的方法获取标注在它们之上的注解：

- `Annotation[] getAnnotations()`：获取该对象上的所有注解
- `Annotation getAnnotation(Class annotaionClass)`：传入注解类型，获取该对象上的特定一个注解
- `Annotation[] getDeclaredAnnotations()`：获取该对象上的显式标注的所有注解，无法获取继承下来的注解
- `Annotation getDeclaredAnnotation(Class annotationClass)`：根据注解类型，获取该对象上的特定一个注解，无法获取继承下来的注解

只有注解的@Retension标注为RUNTIME时，才能够通过反射获取到该注解。@Retension 有3种保存策略：

- `SOURCE`：只在源文件(.java)中保存，即该注解只会保留在源文件中，编译时编译器会忽略该注解，例如 @Override 注解
- `CLASS`：保存在字节码文件(.class)中，注解会随着编译跟随字节码文件中，但是运行时不会对该注解进行解析
- `RUNTIME`：一直保存到运行时，用得最多的一种保存策略，在运行时可以获取到该注解的所有信息

## 通过反射调用方法

- `• invoke(Oject obj, Object... args)`：参数``1指定调用该方法的**对象**，参数2`是方法的参数列表值。

例子：

```java
Class clazz = Class.forName("com.bean.SmallPineapple");
Constructor constructor = clazz.getConstructor(String.class, int.class);
constructor.setAccessible(true);
SmallPineapple sp = (SmallPineapple) constructor.newInstance("小菠萝", 21);
Method method = clazz.getMethod("getInfo");
if (method != null) {
    method.invoke(sp, null);
}
```

## 反射调用 `invoke` 的实现

<aside>
💡 Java 的反射调用机制还设立了另一种动态生成字节码的实现

</aside>

动态调用 `invoke` 的实现包括

- 本地实现
- 动态实现

### 本地实现

栈轨迹追踪的代码：

```java
// v0版本
import java.lang.reflect.Method;
public class Test {
    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }
    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("Test");
        Method method = klass.getMethod("target", int.class);
        method.invoke(null, 0);
    }
}
#不同版本的输出略有不同，这里我使用了Java 10。
$ java Test
java.lang.Exception: #0
at Test.target(Test.java:5)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl .invoke0(Native Methoa      t java.base/jdk.internal.reflect.NativeMethodAccessorImpl. .invoke(NativeMethodAt       java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.i .invoke(Delegatin
java.base/java.lang.reflect.Method.invoke(Method.java:564)
t        Test.main(Test.java:131
```

关于栈轨迹的备注：

- `DelegatingMethodAccessorimpi` 是委派实现
- `NativeMethodAccessorimpi` 是本地实现

其中本地实现调用了 `Method.invoke`，然后进入委派实现（DelegatingMethodAccessorImpl），再然后进入本地实现（NativeMethodAccessorImpl），最后到达目标方法。

这里之所以要用委派实现是为了实现本地实现和动态实现的转换

### 动态实现

动态实现的案例

```java
//动态实现的伪代码，这里只列举了关键的调用逻辑，其实它还包括调用者检测、参数检测的字节码。
package jdk.internal.reflect;
public class GeneratedMethodAccessor1 extends ... {
    @Overrides
    public Object invoke(Object obj, Object[] args) throws ... {
        Test.target((int) args[0]);
        return null;
    }
}
```

### 本地实现和动态实现的转变&&Inflation概念

考虑到许多反射调用仅会执行一次，Java 虚拟机设置了一个阈值 15（可以通过 -Dsun.reflect.inflationThreshold= 来调整），当某个反射调用的调用次数在 15 之下时，采用本地实现；当达到 15 时，便开始动态生成字节码，并将委派实现的委派对象切换至动态实现，这个过程我们称之为 `Inflation`。

案例（反射调用循环20次）

```java
// v1版本
import java.lang.reflect.Method;
public class Test {
    public static void target(int i) {
        new Exception("#" + i).printStackTrace();
    }
    public static void main(String[] args) throws Exception {
        Class<?> klass = Class.forName("Test");
        Method method = klass.getMethod("target", int.class);
        for (int i = 0; i < 20; i++) {
            method.invoke(null, i);
        }
    }
}
#使用-verbose:class打印加载的类
$ java -verbose:class Test
...
java.lang.Exception: #14
at Test.target(Test.java:5)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl .invoke0(Native Methoat java.base/jdk.internal.reflect.NativeMethodAccessorImpl .invoke(NativeMethodAat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl .invoke(Delegatinat java.base/java.lang.reflect.Method.invoke(Method.java:564)
at Test.main(Test.java:12)
[0.158s][info][class,load] ...
...
[0.160s][info][class,load] jdk.internal.reflect.GeneratedMethodAccessor1 source: __JVM_Djava.lang.Exception: #15
at Test.target(Test.java:5)
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl .invoke0(Native Methodat java.base/jdk.internal.reflect.NativeMethodAccessorImpl .invoke(NativeMethodAcat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl .invoke(Delegatingat java.base/java.lang.reflect.Method.invoke(Method.java:564)
at Test.main(Test.java:12)
java.lang.Exception: #16
at Test.target(Test.java:5)
at jdk.internal.reflect.GeneratedMethodAccessor1 .invoke(Unknown Source)
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl .invoke(Delegatingat java.base/java.lang.reflect.Method.invoke(Method.java:564)
at Test.main(Test.java:12)
...
```

可以看到第16次（ `java.lang.Exception: #16`) 的时候采用的动态实现 `GeneratedMethodAccessor` 

- `inflation` 的关闭：
    - 参数 `Dsun.reflect.noInflation=true`
- 改变 `inflation` 的阈值：
    - 参数 `Dsun.reflect.noInflation=true`

## 反射优缺点

### 概况

优点：

- 增加灵活性，应对变动的需求

缺点：

- 破坏类的封装性，强制访问 `private` 修饰的信息
- 性能损耗

### 缺点-破坏类的封装性

使用 `setAccessable(true)` 可以无视访问修饰符的限制，外界可以强制访问

### 缺点- 性能损耗

正常调用对象会提前指定对象类型和是否可访问，但反射操作对象时，编译器只有在**运行时调用** 代码才会从头检查，且jvm无法对反射代码优化

## Reference

- [https://zhuanlan.zhihu.com/p/405325823](https://zhuanlan.zhihu.com/p/405325823)

有缺失 是很久之前notion写的笔记.温习了一遍但是没有找到其他的引用.欢迎提出 补充