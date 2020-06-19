/**
 * NIO 的文件映射
 *
 * JAVA处理大文件，一般用BufferedReader,BufferedInputStream这类带缓冲的IO类，不过如果文件超大的话，更快的方式是采用MappedByteBuffer。
 *
 * MappedByteBuffer是NIO引入的文件内存映射方案，读写性能极高。
 * NIO最主要的就是实现了对异步操作的支持。其中一种通过把一个套接字通道(SocketChannel)注册到一个选择器(Selector)中,不时调用后者的选择(select)方法就能返回满足的选择键(SelectionKey),键中包含了SOCKET事件信息。
 * 这就是select模型。
 *
 *
 * FileChannel提供了map方法来把文件影射为内存映像文件： MappedByteBuffer map(int mode,long position,long size); 可以把文件的从position开始的size大小的区域映射为内存映像文件，mode指出了 可访问该内存映像文件的方式：
 *
 * READ_ONLY,（只读）： 试图修改得到的缓冲区将导致抛出 ReadOnlyBufferException.(MapMode.READ_ONLY)
 *
 * READ_WRITE（读/写）： 对得到的缓冲区的更改最终将传播到文件；该更改对映射到同一文件的其他程序不一定是可见的。 (MapMode.READ_WRITE)
 *
 * PRIVATE（专用）： 对得到的缓冲区的更改不会传播到文件，并且该更改对映射到同一文件的其他程序也不是可见的；相反，会创建缓冲区已修改部分的专用副本。 (MapMode.PRIVATE)
 *
 * MappedByteBuffer是ByteBuffer的子类，其扩充了三个方法：
 *
 * force()：缓冲区是READ_WRITE模式下，此方法对缓冲区内容的修改强行写入文件；
 *
 * load()：将缓冲区的内容载入内存，并返回该缓冲区的引用；
 *
 * isLoaded()：如果缓冲区的内容在物理内存中，则返回真，否则返回假；
 *
 *
 */
package com.geek45.exampleall.io.nio.demo3;