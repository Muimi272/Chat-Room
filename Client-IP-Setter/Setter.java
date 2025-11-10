import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("address.json")));
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your server IP address: ");
        String ip = sc.next();
        System.out.println("Setting...");
        bf.write("ip:" + ip);
        System.out.println("Done.Your new server address is: " + ip);
        bf.close();
    }
}
