package client;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.JsonObject;
import model.Hero;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static int port = 6666; // read from config
    private static Socket socket;
    private static PrintWriter out;
    private static InputStreamReader in;

    private static void establishConnection() {
        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        try {
            OutputStream socketOutputStream = socket.getOutputStream();
//            out = new OutputStreamWriter(socket.getOutputStream()   );
//            in = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    private static void sendJSON(JsonObject jsonObject) {
        out.write(jsonObject.toString());
    }

    private static void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public static void test() {
        establishConnection();
        out.write("hello server");
        //        sendJSON(obj);
    }

    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setHandle(scanner.next());
//        loginRequest.setPassword(scanner.next());

        test();
        if (1 == 1)
            return;

        JsonObject obj = new JsonObject();
        obj.addProperty("isCat", "false");
        obj.addProperty("age", 3);

        System.out.println(obj);

        FileWriter out = null;
        try {
            out = new FileWriter("./test/" + "myFirstJson" + ".json", false);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        try {
//            out.write(yaGson.toJson(loginRequest, LoginRequest.class));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(obj.toString());
        out.flush();
    }
}