# java网络: TCP/UDP

首先要了解一些基础网络知识, 包括TCP/IP协议,还有网络模型.这里略.

## TCP编程

socket由IP地址和端口号组成(端口号0~65535, 其中>1024为特权端口)

使用的基础包为 `[java.net](http://java.net)` 此外客户端和服务端进行通讯, 还需要使用 `[java.io](http://java.io)` 包.

注意: 以下代码真实环境允许,需要加 `try...catch ...`捕获IOException异常. 后续代码案例可能有省略.最好是都写一遍.

### 服务端

步骤:

1. 使用 `ServerSokcet` 监听指定端口
2. 使用 `accept()` 接受连接, 并返回 `Socket` 
3. `Sokect` 流进行读取/写入网络数据, 具体函数方法为
    1. `getInputStream()` 
    2.  `getOutputStream()`

具体代码如下:

```java
//创建ServerSocket对象,绑定端口
ServerSocket serverSocket = new ServerSocket(8000);

//监听客户端连接,获取Socket对象
Socket socket = serverSocket.accept();

//获取输入输出流
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();

//读取客户端发送的消息
byte[] bytes = new byte[1024];
int len = is.read(bytes);
String msg = new String(bytes, 0, len);
System.out.println("接收到客户端的消息:" + msg);

//向客户端发送消息
os.write("Hello Client".getBytes());

//关闭流和socket
is.close();
os.close(); 
socket.close();
serverSocket.close();
```

### 客户端

步骤:

1. 实例化 `Sockect` 并指定端口, 返回 `Socket`
2. 同服务端,  `Sokect` 流进行读取/写入网络数据,

```java
Socket socket = new Socket("127.0.0.1", 8000);
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();

os.write("Hello Server".getBytes());

byte[] bytes = new byte[1024];
int len = is.read(bytes);
String msg = new String(bytes, 0, len);
System.out.println("接收到服务器的消息:" + msg);
is.close();
os.close();
socket.close();
```

备注, 除了使用 `is.read()` 来读取 `byte[]` 的方法外, 也可以使用 `Scanner` 来. 如下

```java
InputStream is = socket.getInputStream();
OutputStream os = socket.getOutputStream();

// os.write("Hello Server".getBytes());

Scanner in = new Scanner(is);

while(in.hasNextLine()) {
    System.out.println("接收到服务器的消息:" + in.nextLine());
}
```

## UDP编程

UDP没有流的概念, 使用 `DatagramSocket`实现. 

步骤:

1. 实例化 `DatagramSocket` 并指定端口
2. 创建 `DatagramPacket()` ,并使用 `byte[]`  类型变量以及长度作为参数. 返回类型为 `DatagramPacket`
3. 调用 `DatagramPacket` 的 `receive` 和 `send` 方法读取/发送UDP数据包.

示例代码

```java
public static void main(String[] args){
        try {
            DatagramSocket ds = new DatagramSocket(6666); // 监听指定端口
            for (; ; ) { // 无限循环
                // 数据缓冲区:
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ds.receive(packet); // 收取一个UDP数据包
                // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
                // 将其按UTF-8编码转换为String:
                String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
                // 发送数据:
                byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
                packet.setData(data);
                ds.send(packet);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
```

### 客户端

1. 实例化 `DatagramSocket`  ,返回DatagramSocket
2. 设置DatagramSocket的 `setSoTimeout({{time}})`
3. 同2, 调用其下的 `connect` 方法, 连接指定服务器和端口
4. 同样是用 `DatagramPacket` 发送 `byte[]` 类型的packet

想要打印出具体消息则需要转换为String类型.

```java
String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
```

示例代码

```java
DatagramSocket ds = new DatagramSocket(); // 监听指定端口
            ds.setSoTimeout(1000);
            ds.connect(InetAddress.getByName("localhost"), 6666);
            //发送
            byte[] data = "Hello".getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            ds.send(packet);
            //接受
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet);
            String resp = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.println(resp);
            ds.disconnect();
```

## Reference

- [https://www.liaoxuefeng.com/wiki/1252599548343744/1305207629676577](https://www.liaoxuefeng.com/wiki/1252599548343744/1305207629676577)
- [https://www.runoob.com/java/java-networking.html](https://www.runoob.com/java/java-networking.html)