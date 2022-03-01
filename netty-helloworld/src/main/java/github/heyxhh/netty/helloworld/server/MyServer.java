package github.heyxhh.netty.helloworld.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class MyServer {

    public static void main(String[] args) {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup boosGroup =  new NioEventLoopGroup();
        EventLoopGroup workerGroup =  new NioEventLoopGroup();

        //创建服务端的启动对象，设置参数
        ServerBootstrap bootstrap =  new ServerBootstrap();
        bootstrap.group(boosGroup, workerGroup);


    }
    
}
