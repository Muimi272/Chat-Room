import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Admin {
    private static CopyOnWriteArrayList<OutputStream> osl;
    private static String name;

    public static void startAdminConsole(CopyOnWriteArrayList<OutputStream> osl, CopyOnWriteArrayList<String> allMemberNames, CopyOnWriteArrayList<String> legalMemberNames, CopyOnWriteArrayList<String> legalMemberPasswords) {
        Admin.osl = osl;
        name = "Host";
        Thread thread = new Thread(() -> {
            Scanner command = new Scanner(System.in);
            String input;
            while (true) {
                input = command.next();
                if (input.startsWith("\\")) {
                    switch (input) {
                        case "\\exit" -> System.exit(0);
                        case "\\name" -> {
                            System.out.println("请输入更新后的ID：");
                            name = command.next().trim();
                            System.out.println("已更新。欢迎回来，" + name);
                        }
                        case "\\showAll" -> {
                            System.out.println("当前在线用户：" + allMemberNames);
                            System.out.println("当前共有" + allMemberNames.size() + "名用户在线");
                        }
                        case "\\showLegal" -> {
                            System.out.println("所有可加入聊天用户名及密码：");
                            String head = String.format("%-20s%-20s", "用户名", "密码");
                            System.out.println(head);
                            for (int i = 0; i < legalMemberNames.size(); i++) {
                                String line = String.format("%-20s%-20s", legalMemberNames.get(i), legalMemberPasswords.get(i));
                                System.out.println(line);
                            }
                        }
                        case "\\kick" -> {
                            System.out.println("Which one do you want to kick?");
                            String kickOutName = command.next().trim();
                            if (!allMemberNames.contains(kickOutName)) {
                                System.out.println("用户 " + kickOutName + " 不在线或不存在");
                                continue;
                            }
                            System.out.println("Why kick " + kickOutName + "?");
                            command.nextLine();
                            String kickReason = command.nextLine();
                            if (kickReason.isEmpty()) {
                                kickReason = "违反了聊天室规则";
                            }
                            try {
                                int index = allMemberNames.indexOf(kickOutName);
                                OutputStream kickedUserOS = osl.get(index);
                                kickedUserOS.write("[Admin]你已被踢出聊天\n".getBytes());
                                kickedUserOS.flush();
                                kickedUserOS.close();
                                osl.remove(index);
                                allMemberNames.remove(kickOutName);
                                broadCast(osl, kickOutName + "由于" + kickReason + "被踢出了聊天\n");
                                System.out.println(kickOutName + "由于" + kickReason + "被踢出了聊天");
                                System.out.println("当前在线用户：" + allMemberNames);
                                System.out.println("当前共有" + allMemberNames.size() + "名用户在线");
                            } catch (IOException e) {
                                System.out.println("踢出用户时发生错误: " + e.getMessage());
                            }
                        }
                        case "\\broadcast" -> {
                            System.out.print("请输入广播内容：");
                            String content = command.nextLine();
                            Main.send(Admin.osl, "[All]" + content + "\n");
                            System.out.println("[All]" + content);
                        }
                        case "\\add" -> {
                            System.out.print("请输入用户ID：");
                            command.nextLine();
                            String name = command.nextLine();
                            System.out.print("请输入用户密码：");
                            String password = command.nextLine();
                            if (legalMemberNames.contains(name)) {
                                try {
                                    writeup(legalMemberNames, legalMemberPasswords);
                                    System.out.println("已存在用户" + name);
                                    System.out.print("目前可加入聊天的用户：");
                                    System.out.println(legalMemberNames);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                legalMemberNames.add(name);
                                legalMemberPasswords.add(password);
                                try {
                                    writeup(legalMemberNames, legalMemberPasswords);
                                    System.out.print("目前可加入聊天的用户：");
                                    System.out.println(legalMemberNames);
                                } catch (IOException e) {
                                    System.out.println("用户名写入失败，请打开文件写入");
                                }
                            }
                        }
                        case "\\remove" -> {
                            System.out.print("请输入用户ID：");
                            command.nextLine();
                            String name = command.nextLine();
                            if (!(legalMemberNames.contains(name))) {
                                try {
                                    writeup(legalMemberNames, legalMemberPasswords);
                                    System.out.println("不存在用户" + name);
                                    System.out.print("目前可加入聊天的用户：");
                                    System.out.println(legalMemberNames);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                legalMemberPasswords.remove(legalMemberNames.indexOf(name));
                                legalMemberNames.remove(name);
                                try {
                                    writeup(legalMemberNames, legalMemberPasswords);
                                    System.out.print("目前可加入聊天的用户：");
                                    System.out.println(legalMemberNames);
                                } catch (IOException e) {
                                    System.out.println("用户名写入失败，请打开文件写入");
                                }
                            }
                        }
                        case "\\help" -> {
                            System.out.println("\\name\t\t更改管理员ID");
                            System.out.println("\\kick\t\t踢出聊天成员");
                            System.out.println("\\broadcast\t广播消息");
                            System.out.println("\\showAll\t显示在线成员");
                            System.out.println("\\add\t\t添加可连接的用户");
                            System.out.println("\\remove\t\t删除可连接的用户");
                            System.out.println("\\showLegal\t显示所有可连接的用户及密码");
                            System.out.println("\\exit\t\t关闭服务器");
                        }
                        default -> {
                            System.out.println("命令不存在");
                        }
                    }
                } else {
                    broadCast(osl, input);
                }
            }
        });
        thread.start();
    }

    private static void writeup(CopyOnWriteArrayList<String> legalMemberNames, CopyOnWriteArrayList<String> legalMemberPasswords) throws IOException {
        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Members.json")));
        for (int i = 0; i < legalMemberNames.size(); i++) {
            bf.write(legalMemberNames.get(i) + ":" + legalMemberPasswords.get(i) + "\n");
        }
        bf.close();
    }

    private static void broadCast(CopyOnWriteArrayList<OutputStream> osl, String input) {
        Main.send(osl, "[Admin]" + name + ":" + input + "\n");
    }
}
