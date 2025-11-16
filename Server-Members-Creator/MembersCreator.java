import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class MembersCreator {
    public static void main(String[] args) throws IOException {
        CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
        users.add(new User("test", "123456"));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("legalMembers.ser"))) {
            oos.writeObject(users);
        }
        System.out.println("用户文件创建成功");
    }
}
