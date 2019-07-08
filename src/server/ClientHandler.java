package server;

import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonObject;
import com.gilecode.yagson.com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {

    private Server server;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String authenticationToken;

    ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private JsonObject accountLogging(JsonObject jsonObject) {
        String user = jsonObject.get("user").getAsString();
        String password = jsonObject.get("password").getAsString();
        JsonObject response;
        if (jsonObject.get("requestType").getAsString().equals("login")) {
            response = server.login(user, password);
            JsonElement jsonElement = response.get("authenticationToken");
            if (jsonElement != null) {
                authenticationToken = jsonElement.getAsString();
            }
        } else {
            response = server.register(user, password);
        }
        return response;
    }

    private void parseMessage(String message) {
        if (message == null)
            return;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = (JsonObject) parser.parse(message);
        String requestType = jsonObject.get("requestType").getAsString();
        JsonObject response = new JsonObject();
        switch (requestType) {
            case "login" :
            case "register" :
                response = accountLogging(jsonObject);
                break;
            case "getAccount" :
                response = server.getAccount(jsonObject);
                break;
            case "setAccountData" :
                response = server.setAccountData(jsonObject);
                break;
            case "buyCollectionItem" :
                response = server.tradeCollectionItem(jsonObject, false);
                break;
            case "sellCollectionItem" :
                response = server.tradeCollectionItem(jsonObject,true);
                break;
            case "getItemCount" :
                response = server.getItemCount(jsonObject);
                break;
            case "getLeaderBoard" :
                response = server.getLeaderBoard();
                break;
            case "sendChatMessage" :
                response = server.addChatMessage(jsonObject);
                break;
            case "getOnlineUsers" :
                response = server.getOnlineUsers();
                break;
            case "getNewMessages" :
                response = server.getNewMessages(jsonObject);
                break;
//            case "multiplayerGameRequest" :
//                response = server.addGameRequest(jsonObject);
//                break;
//            case "cancelGameRequest" :
//                response = server.cancelGameRequest(jsonObject);
//                break;
//            case "getGameRequests" :
//                response = server.getGameRequests(jsonObject);
//                break;
//            case "startGame" :
//                response = server.startGame(jsonObject);
//                break;
//            case "addNewCard" :
//                response = server.addNewCard(jsonObject);
//                break;
            case "addFuckingNewCard" :
                response = server.addFuckingNewCard(jsonObject);
                break;
            case "enterMultiplayer" :
                response = server.enterMulti(jsonObject);
                break;
            case "sendGameCommand" :
                response = server.sendGameCommand(jsonObject);
                break;
            case "checkMe" :
                response = server.checkMe(jsonObject);
                break;
            case "addWin" :
                response = server.addWin(jsonObject);
                break;
        }
        sendMessage(response.toString());
    }

    @Override
    public void run() {

        final int maxNoRespondingCount = 30;
        int disconnectionFactor = 0;
        while (true) {
            try {
                String message = in.readLine();
//                if (message != null)
//                    System.out.println("message from client : " + message);
                if (message == null) {
                    disconnectionFactor++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    parseMessage(message);
            } catch (IOException e) {
                disconnectionFactor++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            if (disconnectionFactor > maxNoRespondingCount) { // connection may be lost
                break;
            }
        }
        close();
    }

    public void sendMessage(String message) {

//        System.out.println("message from server : " + message);

        out.println(message);
    }

    public void setAuthenticationToken(String token) {
        authenticationToken = token;
    }

    private void close() {
        if (authenticationToken != null) {
            server.expireToken(authenticationToken);
        }
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}