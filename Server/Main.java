import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static final CopyOnWriteArrayList<OutputStream> osl = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<String> allMemberNames = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10000);
        System.out.println("服务器启动在端口 10000...");
        ChatLogger.creatPW();
        Admin.startAdminConsole(osl, allMemberNames);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            OutputStream myOS = clientSocket.getOutputStream();
            osl.add(myOS);
            new ClientHandler(clientSocket, osl, myOS, allMemberNames).start();
        }
    }


    static void send(CopyOnWriteArrayList<OutputStream> osl, String input) {
        for (OutputStream os : osl) {
            try {
                os.write(input.getBytes());
                os.flush();
            } catch (IOException e) {
                System.out.println("向客户端发送消息失败");
                osl.remove(os);
                try {
                    os.close();
                } catch (IOException _) {
                }
            }
        }
    }
}
