package com.sslfer.sslftest.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author sslf
 * @date 2018/12/4
 */
public class ServerSocketChannelTest {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();




        }

    }

}
