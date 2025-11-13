import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive implements Runnable {
    private final Socket socket;
    private String name;

    public Receive(Socket socket) {
        this.socket = socket;
    }

    public void setName(String name) {
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
                    String message = sb.toString();
                    if (message.equals("[Admin]你已被踢出聊天\n")) {
                        System.out.print(message);
                        System.out.println("服务器终止了连接。");
                        System.exit(0);
                    } else if (message.equals("[Admin]无权限加入此聊天室！\n")) {
                        System.out.print(message);
                        System.exit(0);
                    }
                    if (shouldDisplayMessage(message)) {
                        System.out.print(message);
                    }
                    sb.setLength(0);
                }
            }
        } catch (IOException e) {
            System.out.println("服务器断开连接：" + e.getMessage());
        }
    }

    private boolean shouldDisplayMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }
        String trimmedMessage = message.trim();
        if (trimmedMessage.startsWith(name + ":")) {
            return false;
        }
        return !message.startsWith("\n" + name + ":") &&
                !message.startsWith("\r\n" + name + ":") &&
                !message.contains("\n" + name + ":");
    }
}

