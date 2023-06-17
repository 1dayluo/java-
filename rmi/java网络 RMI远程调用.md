# java网络: RMI远程调用

RMI(Remote Method Invocation) RMI远程调用为一个JVM代码可以通过网络远程调用另一个JVM中的某个方法. 相关的包为 `java.rmi.Remote` ,声明并抛出 `RemoteException` . 

*另外一定要学完自己再独立实现一遍.

## 创建接口及其实现

对于服务端需要:

1. 创建服务端/客户端共享接口, 即定义远程接口.
2. 创建对该接口的实现

### 第一步: 定义远程接口

代码如下:

```java
public interface MessengerService extends Remote {
    String sendMessage(String clientMessage) throws RemoteException;
}
```

这里接口 extends了 `java.rmi.Remote` . 同时每一个在接口声明的方法必须throws `java.rmi.RemoteException` 

Note, though, that RMI supports the full Java specification for method signatures, as long as the Java types implement *java.io.Serializable*.

这个接口将会被服务端和客户端共同使用.

对于服务端, 我们需要创建一个接口实现, 通常被称为 *Remote Object*. 

对于客户端, RMI library将会动态创建一个叫做 `Stub` 的实现.

### 第二步: 对接口实现

实现remote interface, 即创建Remote Object

```java
public class MessengerServiceImpl implements MessengerService { 
 
    @Override 
    public String sendMessage(String clientMessage) { 
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }

    public String unexposedMethod() { /* code */ }
}
```

注意 这里从方法定义里移去了 `throws RemoteException` 的部分.Remote Object 抛出 `RemoteException` 是不常见的, 因为这个异常一般保留给RMI库,以向客户端引出(raise)通信错误.忽略它还有一个好处, 即使我们的方法实现与RMI无关.

此外，在远程对象中定义的任何其他方法，而不是在接口中定义的方法，对客户端来说都是**不可见**的

## 创建服务端

当我们创建了remote的实现, 我们需要将remote object绑定导RMI注册表上

需要:

1. 创建一个Stub
2. 创建注册表
3. 绑定Stub

### 第一步: 创建一个Stub

首先我们需要创建一个remote object的stub (转换为远程服务接口)

```java
MessengerService server = new MessengerServiceImpl();
MessengerService stub = (MessengerService) UnicastRemoteObject
  .exportObject((MessengerService) server, 0);
```

我们使用static对象 *`UnicastRemoteObject.exportObject`* 方法去创建我们的 `stub` 实现. stub能够通过底层rmi协议鱼server进行交流

其中 `exportObject` 第一个参数是远程服务对象, 第二个参数是 `exportObject` 导出remote对象到注册表所使用的端口(0代表随机, 动态选择端口)

*但是不推荐不使用指定端口号

### 第二步 创建一个Registry(注册表)

我们能在我们服务器本地建立一个registry, 或者作为单独的一个服务.

服务器本地简历registry:

```java
Registry registry = LocateRegistry.createRegistry(1099);
```

rmi 注册表默认在端口 `1099` 上.

如果是单独的服务, 我们则是调用 `getRegistry` 

### 第三步: 绑定Stub

绑定我们的stub到registry.. 

```java
registry.rebind("MessengerService", stub);
```

## 创建客户端

```java
public class RmiClient {
    public static void main(String[] args) throws RemoteException, NotBoundException  {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        MessengerService msg = (MessengerService) registry.lookup("MessengerService");
        String responseMessage = msg.sendMessage("Client Message");

        System.out.println(responseMessage);

    }
}
```

这里因为我们是本地跑RMI注册表, 所以 `getRegistry()` 不传参数, 如果是在不同的host和prot则需要提供这些参数.

## Reference

- [https://www.baeldung.com/java-rmi](https://www.baeldung.com/java-rmi)