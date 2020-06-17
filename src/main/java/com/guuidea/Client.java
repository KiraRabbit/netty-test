package com.guuidea;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.scheduling.annotation.Async;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Async
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            Thread.sleep(200000000);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.print("优雅的关闭");
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        Executor threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            if (i%10==0){
                Thread.sleep(3000);
            }
            threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        buildClient();
                    } catch (InterruptedException e) {
                        System.out.println("线程异常"+e);
                    }
                    System.out.println(Thread.currentThread().getName() + " is running");
                }
            });
        }


    }

    public static void buildClient() throws InterruptedException {
        String s = UUID.randomUUID().toString();
        Client client = new Client("192.168.1.75", 7934);
        try {
            client.start();
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("创建Client:" + s);
        Thread.sleep(1000000000);
    }
}
