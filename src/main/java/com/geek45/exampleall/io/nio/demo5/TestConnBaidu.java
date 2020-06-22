package com.geek45.exampleall.io.nio.demo5;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 测试使用nio链接百度
 */
public class TestConnBaidu {

    public static void main(String[] args) throws IOException, InterruptedException {
        testConnHttp();
    }

    private static void testTcp() throws IOException {
        String body = "CONNECT www.geek45.com:443 HTTP/1.1\n" +
                "Host: www.geek45.com:443\n" +
                "Proxy-Connection: keep-alive\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36\n" +
                "\n";
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("www.geek45.com", 443));
        ByteBuffer byteBuffer = ByteBuffer.wrap(body.getBytes());
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (true) {
            if (socketChannel.finishConnect()) {
                System.out.println("链接成功");
                byteBuffer.rewind();
                socketChannel.write(byteBuffer);
                socketChannel.register(selector, SelectionKey.OP_READ);
                break;
            }
        }
        System.out.println("开始监听");
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isConnectable()) {
                    System.out.println("链接事件");
                } else if (selectionKey.isWritable()) {
                    System.out.println("写事件");
                } else if (selectionKey.isReadable()) {
                    System.out.println("读取事件");
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    readBuffer.clear();
                    int length = socketChannel1.read(readBuffer);
                    if (length > 0) {
                        System.out.println(new String(readBuffer.array(), 0, length));
                    }
                    socketChannel1.register(selector, SelectionKey.OP_READ);
                }
            }

        }


    }

    /**
     * 测试链接http链接
     */
    private static void testConnHttp() throws IOException, InterruptedException {

        String body = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cache-Control: max-age=0\n" +
                "Connection: keep-alive\n" +
                "Cookie: tale_remember_mail=test%40123.com; tale_remember_author=%E6%B8%B8%E5%AE%A2; tale_remember_url=http%3A%2F%2F11; SESSION=5me8cs2j4qjh3qpucv7apojbm6\n" +
                "Host: www.geek45.com\n" +
                "Sec-Fetch-Dest: document\n" +
                "Sec-Fetch-Mode: navigate\n" +
                "Sec-Fetch-Site: cross-site\n" +
                "Sec-Fetch-User: ?1\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36\n\n";
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("www.geek45.com", 80));
        ByteBuffer write = ByteBuffer.wrap(body.getBytes());
        retry:
        while (true) {
            if (socketChannel.finishConnect()) {
                write.rewind();
                socketChannel.write(write);
                break retry;
            } else {
                Thread.sleep(1000L);
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        retry:
        while (true) {

            int length = socketChannel.read(byteBuffer);
            if (length > 0) {
                System.err.println(new String(byteBuffer.array(), 0, length));
                break retry;
            }


        }
    }

    /**
     * 测试链接正经百度域名
     * @throws IOException
     * @throws InterruptedException
     */
    private static void testConnBaidu() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("www.baidu.com", 443));

        String body = "CONNECT www.baidu.com:443 HTTP/1.1\r\n" +
                "Host: www.baidu.com:443\r\n" +
                "Proxy-Connection: keep-alive\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36\r\n\r\n";

        ByteBuffer write = ByteBuffer.wrap(body.getBytes());
        Thread.yield();
        retry:
        while (true) {
            if (socketChannel.finishConnect()) {
                write.rewind();
                socketChannel.write(write);
                break retry;
            } else {
                Thread.sleep(1000L);
            }
        }



        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        retry:
        while (true) {

                int length = socketChannel.read(byteBuffer);
                if (length > 0) {
                    System.err.println(new String(byteBuffer.array(), 0, length));
                    break retry;
                }


        }

    }

}
