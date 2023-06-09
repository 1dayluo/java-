# Java IO流

## 基础

java使用stream(流)的概念来使得处理I/o快速,Java IO API 位于 `java.io.package` 另外,Java IO package 主要用于处理文件,网络流等的input和output.

IO可以用来:

- 读/写文件
- 获取网络sockets
- 压缩与解压缩数据
- 写objects给streams
- …..

一览:

![Untitled](Java%20IO/Untitled.png)

分类就是

- InputStream
- OutputStream

另外还有专门处理IO的:IOException

### InputStream

输入流就是通过java读取原始数据(包括文件, 数组, socket,外围设备,etc), 这里 `java.io.InputStream` 是所有Java IO input streams的base class. 继承系见下图

![Untitled](Java%20IO/Untitled%201.png)

提供的方法有:

- `read()`
- `mark(int arg)`
- `reset()`
- `close()`
- `read(byty []arg)`
- `skip(long arg)`
- `markSupported()`

### OutputStream

*它这个图最右边我怀疑画错了

![Untitled](Java%20IO/Untitled%202.png)

提供的方法有:

- `flush()`
- `close()`
- `write(int b)`
- `write(byte []b)`

### streams的分类

如图 分为ByteStream和CharacterStream

![Untitled](Java%20IO/Untitled%203.png)

1. Byte Stream: java字节流实现8-bit bytes的输入和输出.最常见的是 `FileInputStream` 和 `FileOutputStream` 

2. Character Stream: java字符流用于实现16-bits Unicode的输入输出, 最常用的类是 `FileReader` 和 `FileWriter` .虽然其函数内部使用字节流输入输出方法, 但是 `FileReader` 和 `FileWriter` 使用2bytes作为读/写  

3. Standard Streams: 所有的编程语言都支持标准I/O , 例如C/C++, 有三个标准streams: STDIN, STDOUT, STDERR. java也有相应的三个标准流:
   
   1. `System.in` 
   2. `System.out`
   3. `System.err` 

## Reference

- [https://www.youtube.com/watch?v=7dmIVusn8mk&list=PLfu_Bpi_zcDO4CdNYNS2Wten1vLuQfgp7](https://www.youtube.com/watch?v=7dmIVusn8mk&list=PLfu_Bpi_zcDO4CdNYNS2Wten1vLuQfgp7)
- [https://www.geeksforgeeks.org/java-io-tutorial/](https://www.geeksforgeeks.org/java-io-tutorial/)