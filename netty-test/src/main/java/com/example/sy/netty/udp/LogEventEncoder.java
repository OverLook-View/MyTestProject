package com.example.sy.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress inetSocketAddress;

    public LogEventEncoder(java.net.InetSocketAddress inetSocketAddress) {//LogEventEncoder 创建了 DatagramPacket 消息类发送到指定的 InetSocketAddress
        this.inetSocketAddress = inetSocketAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> list) throws Exception {
        byte[] file = logEvent.getLogFile().getBytes(CharsetUtil.UTF_8);//写文件名到 ByteBuf
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc().buffer(file.length + msg.length + 1);
        buf.writeBytes(file);
        buf.writeByte(LogEvent.SEPARTOR);//添加一个 SEPARATOR
        buf.writeBytes(msg);//写一个日志消息到 ByteBuf
        list.add(new DatagramPacket(buf, inetSocketAddress));//添加新的 DatagramPacket 到出站消息
    }
}
