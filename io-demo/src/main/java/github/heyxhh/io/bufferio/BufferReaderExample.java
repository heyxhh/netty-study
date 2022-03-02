package github.heyxhh.io.bufferio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class BufferReaderExample {
    public static void main(String[] args) throws IOException {
        Reader rd = new FileReader("io-demo/target/classes/github/heyxhh/io/bufferio/BufferReaderExample.class");
        BufferedReader bufRd =  new BufferedReader(rd);

        String recv = null;
        while ((recv = bufRd.readLine()) != null) {
            System.out.println(recv);
        }

        bufRd.close();
        rd.close();
    }
}
