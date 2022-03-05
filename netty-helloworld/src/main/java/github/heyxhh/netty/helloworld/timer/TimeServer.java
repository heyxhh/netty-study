package github.heyxhh.netty.helloworld.timer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    private void run() throws InterruptedException {
        EventLoopGroup bossGroup =  new NioEventLoopGroup();
        EventLoopGroup workGroup =  new NioEventLoopGroup();

        try {
            ServerBootstrap b =  new ServerBootstrap();
            b.group(bossGroup, workGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeServerHandler()); 
                }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f =  b.bind(port).sync();
            
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8888;
        if (args != null && args.length > 0 ) {
            port = Integer.parseInt(args[0]);
        }
        new TimeServer(port).run();
    }
    
}
