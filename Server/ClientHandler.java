import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final CopyOnWriteArrayList<OutputStream> osl;
    private final OutputStream myOutputStream;
    private final CopyOnWriteArrayList<String> allMemberNames;
    private String userName;
    private String password;
    private final CopyOnWriteArrayList<String> legalMemberName;
    private final CopyOnWriteArrayList<String> legalMemberPasswords;

    public ClientHandler(Socket socket, CopyOnWriteArrayList<OutputStream> osl, OutputStream myOutputStream, CopyOnWriteArrayList<String> allMemberNames, CopyOnWriteArrayList<String> legalMemberName, CopyOnWriteArrayList<String> legalMemberPasswords) {
        this.socket = socket;
        this.osl = osl;
        this.myOutputStream = myOutputStream;
        this.allMemberNames = allMemberNames;
        this.legalMemberName = legalMemberName;
        this.legalMemberPasswords = legalMemberPasswords;
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int c;
            while ((c = isr.read()) != -1) {
                if (osl.contains(myOutputStream)) {
                    sb.append((char) c);
                    if (c == '\n') {
                        String[] request = handleContent(sb);
                        switch (request[0]) {
                            case "null":
                                break;
                            case "chat":
                                System.out.print(request[1]);
                                broadCast(request[1]);
                                ChatLogger.log(request[1]);
                                break;
                            case "join":
                                if (legalMemberName.contains(userName) && legalMemberPasswords.get(legalMemberName.indexOf(userName)).equals(password)) {
                                    allMemberNames.add(userName);
                                    System.out.println(userName + "加入了聊天");
                                    System.out.print("当前在线用户：");
                                    System.out.println(allMemberNames);
                                    System.out.println("当前" + allMemberNames.size() + "名用户在线");
                                    broadCast(userName + "加入了聊天");
                                    ChatLogger.log(request[1]);
                                } else {
                                    myOutputStream.write("[Admin]无权限加入此聊天室！请联系管理员获取权限\n".getBytes());
                                    osl.remove(myOutputStream);
                                    closeConnect();
                                }
                                break;
                            case "away":
                                allMemberNames.remove(request[1].substring(0, request[1].length() - 6));
                                System.out.println(request[1].substring(0, request[1].length() - 6) + "离开了聊天");
                                System.out.print("当前在线用户：");
                                System.out.println(allMemberNames);
                                System.out.println("当前" + allMemberNames.size() + "名用户在线");
                                broadCast(request[1]);
                                ChatLogger.log(request[1]);
                                break;
                        }
                        sb.setLength(0);
                    }
                } else {
                    isr.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("客户端连接异常: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private String[] handleContent(StringBuilder sb) {
        if (sb.toString().isEmpty()) {
            return new String[]{"null", "null"};
        }
        String request = sb.toString();
        String title = request.substring(0, 5);
        return switch (title) {
            case "chat:" -> {
                String msg = request.substring(5);
                yield new String[]{"chat", msg};
            }
            case "join:" -> {
                String introduce = request.substring(5);
                String info = introduce.substring(0, introduce.length() - 6);
                userName = info.split(":")[0];
                password = info.split(":")[1];
                yield new String[]{"join", introduce};
            }
            case "away:" -> {
                String leave = request.substring(5);
                yield new String[]{"away", leave};
            }
            default -> new String[]{"null", "null"};
        };
    }

    private void broadCast(String s) {
        Main.send(osl, s);
    }

    private void cleanup() {
        osl.remove(myOutputStream);
        allMemberNames.remove(userName);
        System.out.println("已从客户端列表中移除一个连接，当前连接数: " + osl.size());
        closeConnect();
    }

    private void closeConnect() {
        try {
            myOutputStream.close();
        } catch (IOException e) {
            System.out.println("关闭输出流时发生错误: " + e.getMessage());
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("关闭socket时发生错误: " + e.getMessage());
        }
    }
}
