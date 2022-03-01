package github.heyxhh.netty.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        int port = 8899;

        // TODO: 在哪关闭server
        ServerSocket serverSocket =  new ServerSocket(port);

        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");

        //如果使用多线程，那就需要线程池，防止并发过高时创建过多线程耗尽资源
        ExecutorService threadPool =  Executors.newFixedThreadPool(100);

        while (true) {
            Socket socket =  serverSocket.accept();

            Runnable runable = ()->{
                // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                        sb.append(new String(bytes, 0, len, "UTF-8"));
                    }
                    System.out.println("get message from client: " + sb);

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            threadPool.submit(runable);

        }

    }
    
}
