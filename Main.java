import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.creatSocket();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入你的ID：");
        String name = scanner.nextLine().trim();
        client.setName(name);
        Receive receive = new Receive(client.getSocket(),name);
        client.joinRoom();
        new Thread(receive).start();
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equals("\\exit")) {
                client.exit();
                break;
            }else{
                client.sendMassage(line);
            }
        }
    }
}