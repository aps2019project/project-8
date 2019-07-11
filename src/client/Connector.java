package client;

import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonObject;
import com.gilecode.yagson.com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connector {

    private static final int DEFAULT_PORT = 6666;
    private static final String SERVER_IP = "172.20.9.214";
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    private int port;
    private String log;

    public Connector() {
        port = DEFAULT_PORT; // port should be read from config file
    }

    private boolean connectToSever() {
        try {
            socket = new Socket("localhost", DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            log = "unable to connect to game server";
            System.err.println("unable to connect to game server");
            System.err.println("try connecting later");
            return false;
        }

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("unable to set data streams of connection");
            return false;
        }
        return true;
    }


    // login is true for logging attempt. it's false for registering
    public Connection connect(String userName, String password, boolean login) { // connecting to server
        String authenticationToken = null;

        boolean connected = connectToSever();
        if (!connected)
            return null;

        JsonObject jsonObject = new JsonObject();

        String type = login ? "login" : "register";
        jsonObject.addProperty("requestType", type);
        jsonObject.addProperty("user", userName);
        jsonObject.addProperty("password", password);
        out.println(jsonObject.toString());

        try {
            String response = in.readLine();
            JsonParser parser = new JsonParser();
            jsonObject = (JsonObject) parser.parse(response);
            JsonElement jsonElement = jsonObject.get("authenticationToken");
            if (jsonElement != null)
                authenticationToken = jsonElement.getAsString();
            log = jsonObject.get("log").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("problem in server responding");
            log = "problem in server responding";
            return null;
        }
        if (authenticationToken == null) {

            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        return new Connection(socket, out, in, authenticationToken);
    }

    public AnonymousConnection anonymousConnect() {
        boolean connected = connectToSever();
        if (!connected)
            return null;
        log = "successfully connected to server";
        return new AnonymousConnection(socket, out, in);
    }

    public String getLog() {
        return log;
    }
}
