package client;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.*;
import model.AccountData;
import model.AccountUser;
import netscape.javascript.JSObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

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

    private JsonObject getAsJson(String message) {
        JsonParser parser = new JsonParser();
        return (JsonObject) parser.parse(message);
    }

    public AccountUser getAccount() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getAccount");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            System.err.println(jsonObject.get("log").getAsString());
            YaGson yaGson = new YaGson();
            return yaGson.fromJson(jsonObject, AccountUser.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return null;
        }
    }

    public void setAccountData(AccountData data) {
        JsonObject jsonObject;
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        jsonObject = (JsonObject) yaGson.toJsonTree(data, AccountData.class);
        jsonObject.addProperty("requestType", "setAccountData");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            System.err.println(jsonObject.get("log").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
        }
    }

    public String tradeCollectionItem(String s, boolean sell) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", sell ? "sell" : "buy" + "CollectionItem");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty(sell ? "collectionItemID" : "collectionItemName", s);
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            return jsonObject.get("log").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return "error occurred in fetching response from server";
        }
    }

    public String getItemCount(String collectionItemName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getItemCount");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("collectionItemName", collectionItemName);
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            System.err.println(jsonObject.get("log").getAsString());
            if (jsonObject.get("count") != null)
                return jsonObject.get("count").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
        }
        return "";
    }

    public void closeConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
