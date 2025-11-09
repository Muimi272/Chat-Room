import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String username;

    public void setName(String s) {
        username = s;
    }

    public void joinRoom() throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(("join:" + username + "加入了聊天\n").getBytes());
    }

    public void creatSocket() throws IOException {
        this.socket = new Socket("10.18.166.249", 10000);
    }

    public void sendMassage(String s) throws IOException {
        if (s.isEmpty()||s.equals("\n")) {
            System.out.println("系统提示：消息内容不能为空！");
        } else {
            OutputStream os = socket.getOutputStream();
            os.write(("chat:" + username + ":" + s + "\n").getBytes());
        }
    }

    public void exit() throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(("away:" + username + "退出了聊天\n").getBytes());
        os.close();
        socket.close();
        System.exit(0);
    }

    public Socket getSocket() {
        return socket;
    }
}
