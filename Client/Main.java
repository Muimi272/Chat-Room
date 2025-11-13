import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.creatSocket();
        try {
            System.out.println("正在连接服务器...");
            Thread.sleep(1000);
            System.out.println("服务器连接成功");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Receive receive = new Receive(client.getSocket());
        Scanner scanner = new Scanner(System.in);
        String name;
        String passwd;
        System.out.print("请输入你的ID：");
        name = scanner.nextLine().trim();
        System.out.print("请输入" + name + "的密码：");
        passwd = scanner.nextLine().trim();
        client.setName(name);
        client.setPassword(passwd);
        receive.setName(name);
        client.joinRoom();
        new Thread(receive).start();
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("\\exit")) {
                client.exit();
                break;
            } else {
                client.sendMassage(line);
            }
        }
    }
}
