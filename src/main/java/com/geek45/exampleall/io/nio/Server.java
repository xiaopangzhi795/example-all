package com.geek45.exampleall.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * NIO 服务端
 */
public class Server {

    public static void main(String[] args) throws IOException {
        server1();
    }

    private static void server1() throws IOException {
        //绑定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(10085));
        serverSocketChannel.configureBlocking(false);

        //选择器
        Selector selector = Selector.open();
        //注册channel  指定感兴趣的事情时accept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //读和写对象
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writBuffer = ByteBuffer.allocate(128);
        writBuffer.put("received".getBytes());
        writBuffer.flip();   //将指针移动到第一位，没有该行，指针在最后一位，读取的时候永远读取不到数据
        while (true) {
            System.err.println("start");
            int nReady = selector.select();
            System.err.println("end");
            System.out.println(nReady);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                if (selectionKey.isAcceptable()) {
                    //创建新的连接，并且把链接注册到selector上面
                    //声明该链接只对读写感兴趣
                    SocketChannel socketChannel = serverSocketChannel.accept();
//                        socketChannel.setOption(socketOption, SessionIDGenerate.getInstance().generateId());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    readBuffer.clear();
                    socketChannel.read(readBuffer);

                    readBuffer.flip();
                    String msg = new String(readBuffer.array());
                    System.err.println(msg);
//                        JSONObject object = JSONObject.parseObject(msg);
//                        writBuffer.clear();
//                        writBuffer.put((object.getString("user") + "--->" + object.getString("msg")).getBytes());
//                        writBuffer.put(msg.getBytes());
//                        writBuffer.flip();
//                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    for (SelectionKey key : selector.keys()) {
                        if (key.isValid()) {
                            System.out.println("123213");
                            if (key.channel() instanceof SocketChannel) {
                                SocketChannel socketChannel1 = (SocketChannel) key.channel();
                                if (!socketChannel.equals(socketChannel1)) {
                                    writBuffer = ByteBuffer.allocate(128);
                                    writBuffer.put(msg.getBytes());
                                    writBuffer.flip();
                                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                                }
                            }
//                                System.out.println(socketChannel.getOption(socketOption));
//                                System.out.println(socketChannel1.getOption(socketOption));
                        }
                    }
                } else if (selectionKey.isWritable()) {
                    writBuffer.rewind();
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.write(writBuffer);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }

    private static void server2(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(10085));
            Selector selector = Selector.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel == null) {
                    //没有连接
                    System.err.println("等待连接");
                    Thread.sleep(5000l);
                }else{
                    System.err.println("链接进入");
                    socketList.add(socketChannel);
                    SelectionKey selectorKey = socketChannel.register(selector, SelectionKey.OP_READ);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            for (SocketChannel socket : socketList
            ) {
                if (!socket.isConnectionPending()) {
                    socketList.remove(socket);
                }
                //有链接
                //将SocketChannel设置为非阻塞
                socket.configureBlocking(false);
                int effective = socket.read(byteBuffer);
                if (effective != 0) {
                    byteBuffer.flip();  //切换模式  写 --> 读
                    String content = Charset.forName("utf-8").decode(byteBuffer).toString();
                    System.out.println(content);
                    byteBuffer.clear();
                } else {
                    System.out.println("未读取到消息");
                }
                System.out.println("当前缓存连接数量：" + socketList.size());
            }
        } catch (Exception e) {

        }
    }

    static List<SocketChannel> socketList = new ArrayList<SocketChannel>();
}
