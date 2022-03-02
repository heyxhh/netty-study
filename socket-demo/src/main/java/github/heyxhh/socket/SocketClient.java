package github.heyxhh.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) throws UnknownHostException, IOException {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 8899;
        
        // 与服务端建立连接
        Socket socket =  new Socket(host, port);
        
        OutputStream outputStream =  socket.getOutputStream();

        byte[] msgSend = new String("你好呀！").getBytes("UTF-8");
        outputStream.write(msgSend);
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] msgGet = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(msgGet)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(msgGet, 0, len, "UTF-8"));
        }
        System.out.println("get message from server: " + sb);
    
        inputStream.close();
        outputStream.close();
        socket.close();

    }
    
}
