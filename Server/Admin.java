
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Admin {
    private static CopyOnWriteArrayList<OutputStream> osl;
    private static String name;
    private static CopyOnWriteArrayList<String> allMemberNames;

    public static void startAdminConsole(CopyOnWriteArrayList<OutputStream> osl, CopyOnWriteArrayList<String> allMemberNames) {
        Admin.osl = osl;
        name = "Host";
        Admin.allMemberNames = allMemberNames;
        Thread thread = new Thread(() -> {
            Scanner command = new Scanner(System.in);
            String input;
            while (true) {
                input = command.next();
                if (input.equals("\\exit")) {
                    System.exit(0);
                } else if (input.equals("\\name")) {
                    System.out.println("请输入更新后的ID：");
                    name = command.next().trim();
                    System.out.println("已更新。欢迎回来，" + name);
                } else if (input.equals("\\showAll")) {
                    System.out.println("当前在线用户：" + allMemberNames);
                    System.out.println("当前共有" + allMemberNames.size() + "名用户在线");
                } else if (input.equals("\\kick")) {
                    System.out.println("Which one do you want to kick?");
                    String kickOutName = command.next().trim();
                    System.out.println("Why kick " + kickOutName + "?");
                    command.nextLine();
                    String kickReason = command.nextLine();
                    if (kickReason.equals("\n") || kickReason.isEmpty()) {
                        kickReason = "违反了聊天室规则";
                    }
                    try {
                        osl.get(allMemberNames.indexOf(kickOutName)).write("Admin:你已被踢出聊天\n".getBytes());
                        osl.get(allMemberNames.indexOf(kickOutName)).flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    osl.remove(allMemberNames.indexOf(kickOutName));
                    allMemberNames.remove(kickOutName);
                    broadCast(osl, kickOutName + "由于" + kickReason + "被踢出了聊天\n");
                    System.out.println(kickOutName + "由于" + kickReason + "被踢出了聊天");
                    System.out.println("当前在线用户：" + allMemberNames);
                    System.out.println("当前共有" + allMemberNames.size() + "名用户在线");
                } else if (input.equals("\\broadcast")) {
                    System.out.print("请输入广播内容：");
                    command.nextLine();
                    String content = command.nextLine();
                    Main.send(Admin.osl, "[All]" + content + "\n");
                    System.out.println("[All]" + content);
                } else if (input.equals("\\help")) {
                    System.out.println("\\name\t\t更改管理员ID");
                    System.out.println("\\kick\t\t踢出聊天成员");
                    System.out.println("\\broadcast\t广播消息");
                    System.out.println("\\showAll\t显示在线成员");
                    System.out.println("\\exit\t\t关闭服务器");
                } else {
                    broadCast(osl, input);
                }
            }
        });
        thread.start();
    }

    private static void broadCast(CopyOnWriteArrayList<OutputStream> osl, String input) {
        Main.send(osl, "[Admin]" + name + ":" + input + "\n");
    }
}
