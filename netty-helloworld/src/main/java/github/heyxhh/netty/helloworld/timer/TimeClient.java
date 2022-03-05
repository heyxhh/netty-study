package github.heyxhh.netty.helloworld.timer;

import java.sql.Time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    private String host = "127.0.0.1";
    private int port = 8888;

    public TimeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void connect() throws InterruptedException {
        EventLoopGroup workGroup =  new NioEventLoopGroup();

        try {
            Bootstrap b =  new Bootstrap();
            b.group(workGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler()); 
                }
                 
             })
             .option(ChannelOption.SO_KEEPALIVE, true);
            
            // Start the client.
            ChannelFuture f =  b.connect(host, port).sync();
            
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TimeClient client = new TimeClient("127.0.0.1", 8888);
        client.connect();
    }

    
}
