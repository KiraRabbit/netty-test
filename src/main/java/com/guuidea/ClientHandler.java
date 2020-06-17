package com.guuidea;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    public static Channel channel = null;
    private final static Executor executor = Executors.newCachedThreadPool();//启用多线程


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String sdk = null;
        Random random = new Random();
        int num = random.nextInt(1000);
        sdk = "abc" + num;
        //sdk = UUID.randomUUID().toString();
        //String sdk = "fae836c0-d2b4-4e77-8f0a-bf3a5e664bdd";
        send1(ctx, sdk);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String sdk = "fae836c0-d2b4-4e77-8f0a-bf3a5e664bdd";
        ByteBuf in = (ByteBuf) msg;
        byte type = 0;
        if (in.isReadable()) {
            byte data = in.readByte();
            System.out.println(data);
            type = in.readByte();
            System.out.println("type:" + type);
        }
        in.release();
        Channel channel = ctx.channel();
        System.out.println("接受协议:" + type);

        if (type == 13) {
            System.out.println("接受13协议");
            send1(ctx, sdk);
        }
        if (type == 1) {
            System.out.println("接受1协议");
            send7(ctx);
        }
        if (type == 5) {
            System.out.println("接受1协议");
            //获取sdkId
            Executor threadPool = Executors.newFixedThreadPool(2);
            threadPool.execute(new Runnable() {
                public void run() {
                    send3(ctx);
                    System.out.println(Thread.currentThread().getName() + " 开始异步发送协议3");
                }
            });
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


    public void send1(ChannelHandlerContext ctx, String sidParam) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 1, 0};
        byte[] sid = stringToLengthByteArray(sidParam);
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + sid.length + end.length];
        System.arraycopy(version, 0, data, 0, version.length);
        System.arraycopy(sid, 0, data, version.length, sid.length);
        System.arraycopy(end, 0, data, version.length + sid.length, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println(sidParam + "发送协议1");
    }

    /**
     * 分配使用端
     */

    public void send2(ChannelHandlerContext ctx) {
        System.out.println("发送消息！");
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 2};
        byte[] uid = stringToLengthByteArray("test20200516");
        byte[] sid = stringToLengthByteArray("1fc8e99f7fd48db80808696bb46b6fd2");
        byte[] ip = stringToLengthByteArray("190.2.2.222");
        byte[] city = stringToLengthByteArray("315");
        byte[] province = stringToLengthByteArray("26");
        byte[] country = stringToLengthByteArray("229");
        byte[] aid = stringToLengthByteArray("");
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + uid.length + sid.length + ip.length + city.length + province.length + country.length + aid.length
                + end.length];
        int len1 = version.length + uid.length;
        int len2 = len1 + sid.length;
        int len3 = len2 + ip.length;
        int len4 = len3 + city.length;
        int len5 = len4 + province.length;
        int len6 = len5 + country.length;
        int len7 = len6 + aid.length;
        System.arraycopy(version, 0, data, 0, version.length);
        System.arraycopy(uid, 0, data, version.length, uid.length);
        System.arraycopy(sid, 0, data, len1, sid.length);
        System.arraycopy(ip, 0, data, len2, ip.length);
        System.arraycopy(city, 0, data, len3, city.length);
        System.arraycopy(province, 0, data, len4, province.length);
        System.arraycopy(country, 0, data, len5, country.length);
        System.arraycopy(aid, 0, data, len6, aid.length);
        System.arraycopy(end, 0, data, len7, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议2");
    }

    public void send3(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 3};
        byte[] uid = stringToLengthByteArray("fae836c0-d2b4-4e77-8f0a-bf3a5e664bdd");
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + uid.length + end.length];
        int startIndex = 0;
        System.arraycopy(version, 0, data, startIndex, version.length);
        startIndex = version.length;
        System.arraycopy(uid, 0, data, startIndex, uid.length);
        startIndex = startIndex + uid.length;
        System.arraycopy(end, 0, data, startIndex, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议3");
    }


    public void send7(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 7};
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + end.length];
        int startIndex = 0;
        System.arraycopy(version, 0, data, startIndex, version.length);
        startIndex = version.length;
        System.arraycopy(end, 0, data, startIndex, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议7");
    }

    public void send10(ChannelHandlerContext ctx) {
        System.out.println("发送消息！");
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 10};
        byte[] uid = stringToLengthByteArray("uid");
        byte[] sid = stringToLengthByteArray("sid");
        byte[] ip = stringToLengthByteArray("127.0.0.1");
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + uid.length + sid.length + ip.length + end.length];
        int len1 = version.length + uid.length;
        int len2 = len1 + sid.length;
        int len3 = len2 + ip.length;
        System.arraycopy(version, 0, data, 0, version.length);
        System.arraycopy(uid, 0, data, version.length, uid.length);
        System.arraycopy(sid, 0, data, len1, sid.length);
        System.arraycopy(ip, 0, data, len2, ip.length);
        System.arraycopy(end, 0, data, len3, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议10");
    }


    public void send11(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 11};
        byte[] uid = stringToLengthByteArray("uid");
        byte[] sid = stringToLengthByteArray("sid");
        byte[] status = new byte[]{0, 1};
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + uid.length + sid.length + status.length + end.length];
        int startIndex = 0;
        System.arraycopy(version, 0, data, startIndex, version.length);
        startIndex = version.length;
        System.arraycopy(uid, 0, data, startIndex, uid.length);
        startIndex = startIndex + uid.length;
        System.arraycopy(sid, 0, data, startIndex, sid.length);
        startIndex = startIndex + sid.length;
        System.arraycopy(status, 0, data, startIndex, status.length);
        startIndex = startIndex + status.length;
        System.arraycopy(end, 0, data, startIndex, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议11");
    }

    public void send13(ChannelHandlerContext ctx, String sdk) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 13};
        byte[] uid = stringToLengthByteArray(sdk);
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + uid.length + end.length];
        int startIndex = 0;
        System.arraycopy(version, 0, data, startIndex, version.length);
        startIndex = version.length;
        System.arraycopy(uid, 0, data, startIndex, uid.length);
        startIndex = startIndex + uid.length;
        System.arraycopy(end, 0, data, startIndex, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议13");
    }


    public void send14(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        byte[] version = new byte[]{2, 14, 0};
        byte[] sid = stringToLengthByteArray("sid");
        byte[] end = stringToLengthByteArray("$$__");
        byte[] data = new byte[version.length + sid.length + end.length];
        System.arraycopy(version, 0, data, 0, version.length);
        System.arraycopy(sid, 0, data, version.length, sid.length);
        System.arraycopy(end, 0, data, version.length + sid.length, end.length);
        channel.writeAndFlush(Unpooled.copiedBuffer(data));
        System.out.println("发送协议14");
    }

    public static byte[] stringToLengthByteArray(String param) {
        byte[] bytes = param.getBytes();
        byte[] data = new byte[bytes.length + 1];
        data[0] = (byte) bytes.length;
        for (int a = 0; a < bytes.length; a++) {
            data[a + 1] = bytes[a];
        }
        return data;
    }

    public static byte[] parseParam(byte[] bytes) {
        byte[] data = new byte[bytes.length + 1];
        data[0] = (byte) bytes.length;
        for (int a = 0; a < bytes.length; a++) {
            data[a + 1] = bytes[a];
        }
        return data;
    }
}
