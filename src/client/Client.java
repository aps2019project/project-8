package client;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonObject;
import model.Hero;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static int port = 6666; // read from config
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    private static void establishConnection() {
        try {
            socket = new Socket("127.0.0.1", port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    private static void sendJSON(JsonObject jsonObject) {
//        out.println(jsonObject.toString());
//        out.write(jsonObject.toString());
//        out.flush();
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

    public static void test(JsonObject obj) {
        establishConnection();
//        out.write("hello");
//        out.println("hello server");
        sendJSON(obj);
    }

    public static void run() {
        establishConnection();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            out.println(input);
        }
    }

    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setHandle(scanner.next());
//        loginRequest.setPassword(scanner.next());


        JsonObject obj = new JsonObject();
        obj.addProperty("isCat", "false");
        obj.addProperty("age", 3);
//        obj.addProperty("array", int[]{1, 1, 1, 1});

        run();

        test(obj);

        JsonElement jsonElement;
        System.out.println(obj.get("age"));

        if (1 == 1)
            return;

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