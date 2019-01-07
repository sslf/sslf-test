package com.sslfer.sslftest.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author sslf
 * @date 2018/12/4
 */
public class BufferTest {

    public static void main(String[] args) throws IOException {

        RandomAccessFile file = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel channel = file.getChannel();

        // 48 如何确定
        ByteBuffer buf = ByteBuffer.allocate(1);

        int read = channel.read(buf);
        // channel.read(buf); 是否会阻塞
        while (read != -1) {

            // 是否默认是写
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }

            buf.clear();
            read = channel.read(buf);
        }

        file.close();

    }


}
