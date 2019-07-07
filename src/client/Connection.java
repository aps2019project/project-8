package client;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.*;
import model.AccountData;
import model.AccountUser;
import model.CollectionItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String authenticationToken;

    Connection(Socket socket, PrintWriter out, BufferedReader in, String authenticationToken) {
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
        jsonObject.addProperty("requestType", (sell ? "sell" : "buy") + "CollectionItem");
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
            if (jsonObject.get("count") != null)
                return jsonObject.get("count").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
        }
        return "";
    }

    public void sendChatMessage(String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "sendChatMessage");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("message", message);
        sendSimpleMessage(jsonObject);
    }


    private String[] getJsonStringArray(JsonArray jsonArray) {
        String[] s = new String[jsonArray.size()];
        for (int i = 0; i < s.length; i++)
            s[i] = jsonArray.get(i).getAsString();
        return s;
    }

    public String[] getNewMessages() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getNewMessages");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        out.println(jsonObject.toString());
        out.flush();
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            JsonArray jsonArray = (JsonArray) jsonObject.get("messages");
            System.err.println(jsonArray);
            return getJsonStringArray(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return new String[]{};
        }
    }

    public String[] getUsers() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getOnlineUsers");
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            System.err.println(jsonObject.get("log").getAsString());
            JsonArray jsonArray =  (JsonArray) jsonObject.get("users");
            return getJsonStringArray(jsonArray);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return new String[]{};
        }
    }

    /*

    public void sendMultiplayerGameRequest(String opponentName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "multiplayerGameRequest");
        jsonObject.addProperty("opponentName", opponentName);
        sendSimpleMessage(jsonObject);
        out.println(jsonObject.toString());
    }

    public String receiveGameRequests() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "getGameRequests");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        out.println(jsonObject.toString());
        try {
            String response = in.readLine();
            jsonObject = getAsJson(response);
            System.err.println(jsonObject.get("log").getAsString());
            return jsonObject.get("requester").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return "error occurred in fetching response from server";
        }
    }

    public void cancelGameRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "cancelGameRequest");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        out.println(jsonObject.toString());
        sendSimpleMessage(jsonObject);
    }

    public void startMultiplayerGame(String requesterName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "startGame");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("requesterName", requesterName);
        sendSimpleMessage(jsonObject);
    }

    public void sendNewCard(CollectionItem collectionItem) {
        JsonObject jsonObject;
        YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
        jsonObject = (JsonObject) yaGson.toJsonTree(collectionItem, CollectionItem.class);
        jsonObject.addProperty("requestType", "addNewCard");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        sendSimpleMessage(jsonObject);
    }

    */

    public void sendFuckingNewCard(String s, String name, int count) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "addFuckingNewCard");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("object", s);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("count", count);
        sendSimpleMessage(jsonObject);
    }

    public void enterMultiplayerMenu(boolean in) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("requestType", "enterMultiplayer");
        jsonObject.addProperty("authenticationToken", authenticationToken);
        jsonObject.addProperty("in", in ? "yes" : "no");
        JsonObject response = sendSimpleMessage(jsonObject);
    }

    private JsonObject sendSimpleMessage(JsonObject jsonObject) {
        out.println(jsonObject.toString());
        out.flush();
        try {
            String response = in.readLine();
            JsonObject responseObject = getAsJson(response);
//            System.err.println(responseObject.get("log").getAsString());
            return responseObject;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error occurred in fetching response from server");
            return null;
        }
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
}