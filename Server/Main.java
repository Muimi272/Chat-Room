import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private static final CopyOnWriteArrayList<OutputStream> osl = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<String> allMemberNames = new CopyOnWriteArrayList<>();//allMemberNames是指的是在线的所有成员名字
    private static final CopyOnWriteArrayList<String> legalMemberNames = new CopyOnWriteArrayList<>();//legalMemberNames是指的是所有能够通过登录验证的成员名字

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10000);
        System.out.println("服务器启动在端口 10000...");
        ChatLogger.creatPW();
        readLegalNames();
        Admin.startAdminConsole(osl, allMemberNames, legalMemberNames);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            OutputStream myOS = clientSocket.getOutputStream();
            osl.add(myOS);
            new ClientHandler(clientSocket, osl, myOS, allMemberNames, legalMemberNames).start();
        }
    }

    public static void send(CopyOnWriteArrayList<OutputStream> osl, String input) {
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

    public static void readLegalNames() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Members.json")));
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = br.read()) != -1) {
            if ((char) i == '\n') {
                legalMemberNames.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append((char) i);
            }
        }
    }
}
