package github.heyxhh.netty.helloworld.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        // NioEventLoopGroup is a multithreaded event loop that handles I/O operation. 
        // therefore two NioEventLoopGroup will be used. 
        // The first one, often called 'boss', accepts an incoming connection. 
        // The second one, often called 'worker', handles the traffic of the accepted
        // connection once the boss accepts the connection and registers the accepted connection to the worker.
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap is a helper class that sets up a server. 
            ServerBootstrap bootstrap = new ServerBootstrap();
            // bootstrap作为server的辅助类，做了一下事情
            // 1）绑定boss线程组合work线程组，分别用于处理io连接和消息处理
            // 2）为连接事务设置channel
            // 3）为处理消息的线程设置handler
            // 4）设置tcp属性
            bootstrap.group(bossGroup, workGroup)
                     // Here, we specify to use the NioServerSocketChannel class
                     // which is used to instantiate a new Channel to accept incoming connections.
                     .channel(NioServerSocketChannel.class)
                     // The handler specified here will always be evaluated by a newly accepted Channel. 
                     // The ChannelInitializer is a special handler that is purposed to help a user configure a new Channel.
                     // It is most likely that you want to configure the ChannelPipeline of the new Channel
                     // by adding some handlers such as EchoServerHandler to implement your network application
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());    
                        }  
                     })
                     // You can also set the parameters which are specific to the Channel implementation.
                     // We are writing a TCP/IP server, 
                     // so we are allowed to set the socket options such as tcpNoDelay and keepAlive
                     .option(ChannelOption.SO_BACKLOG, 128)
                     // Did you notice option() and childOption()? 
                     // option() is for the NioServerSocketChannel that accepts incoming connections. 
                     // childOption() is for the Channels accepted by the parent ServerChannel, which is NioSocketChannel in this case.
                     .childOption(ChannelOption.SO_KEEPALIVE, true);
            
            // Bind and start to accept incoming connections.
            ChannelFuture f =  bootstrap.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8888;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        EchoServer server =  new EchoServer(port);
        server.run();

    }
}
