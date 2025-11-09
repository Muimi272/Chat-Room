import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive implements Runnable {
    private Socket socket;
    private String name;
    public Receive(Socket socket , String name ) {
        this.socket = socket;
        this.name = name;
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = isr.read()) != -1) {
                sb.append((char) c);
                if (c == '\n') {
                    if (sb.toString().substring(0,name.length()+1).equals(name + ":")) {
                        sb.setLength(0);
                    } else {
                        System.out.print(sb);
                        sb.setLength(0);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("服务器断开连接：" + e.getMessage());
        }
    }
}
