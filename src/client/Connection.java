package client;

import com.gilecode.yagson.com.google.gson.JsonObject;
import com.gilecode.yagson.com.google.gson.JsonParser;
import model.Buff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String authenticationToken;

    public Connection(Socket socket, PrintWriter out, BufferedReader in, String authenticationToken) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.authenticationToken = authenticationToken;

        System.err.println("made connection! :)) auth token : " + authenticationToken);
    }

    public void closeConnection() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
