import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive implements Runnable {
    private final Socket socket;
    private final String name;

    public Receive(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = isr.read()) != -1) {
                sb.append((char) c);
                if (c == '\n') {
                    if (sb.substring(0, name.length() + 1).equals(name + ":")) {
                        sb.setLength(0);
                    } else if (sb.toString().equals("Admin:你已被踢出聊天\n")) {
                        System.out.println(sb);
                        throw new IOException("服务器终止了连接。");
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
