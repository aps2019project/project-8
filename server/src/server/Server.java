package server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.JsonArray;
import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonObject;
import interfaces.AccountInterface;
import interfaces.ShopInterface;
import model.AccountData;
import model.AccountUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Random;

public class Server {

    private final static int DEFAULT_PORT = 6666;
    private int port;
    private ServerSocket serverSocket;
    private ShopInterface shopInterface;
    private AccountInterface accountInterface;
    private HashMap<String, AccountUser> players;

    public Server() {
        port = DEFAULT_PORT; // should be read from config file
        accountInterface = new AccountInterface();
        shopInterface = new ShopInterface();
        players = new HashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("error in creating server socket");
        }
        while (true) {
            ClientHandler clientHandler;
            try {
                clientHandler = new ClientHandler(serverSocket.accept(), this);
                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRandomToken() {
        final int length = 16;
        char[] token = new char[length];
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            token[i] = (char) (random.nextInt(26) + 'a');
        }
        return new String(token);
    }

    private String getNewToken(AccountUser account) {
        String token = getRandomToken();
        while (players.get(token) != null) {
            token = getRandomToken();
        }
        players.put(token, account);
        return token;
    }

    public JsonObject login(String user, String password) {
        boolean logged = accountInterface.login(user, password);
        JsonObject jsonObject = new JsonObject();
        if (logged) {
            jsonObject.addProperty("log", user + " was successfully logged in");
            String token = getNewToken(accountInterface.getAccount(user));
            jsonObject.addProperty("authenticationToken", token);
        } else {
            jsonObject.addProperty("log", "invalid user name or password");
        }
        return jsonObject;
    }

    public JsonObject register(String user, String password) {
        boolean created = accountInterface.createAccount(user, password);
        JsonObject jsonObject = new JsonObject();
        if (created) {
            jsonObject.addProperty("log", "account successfully created");
        } else {
            jsonObject.addProperty("log","a user with this username already exists");
        }
        return jsonObject;
    }

    public void expireToken(String token) {
        System.err.println("token " + token + " has expired");
        System.err.println("connection of user " + players.get(token).getName() + " is lost");
        players.remove(token);
    }

    public JsonObject getAccount(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
                message = (JsonObject) yaGson.toJsonTree(accountUser, AccountUser.class);
                message.addProperty("log", "success");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject setAccountData(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                accountUser.setData(new YaGson().fromJson(jsonObject, AccountData.class));
                accountUser.saveAccount();
                message.addProperty("log", "user " + accountUser.getName() + "'s account data was set");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject tradeCollectionItem(JsonObject jsonObject, boolean sell) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                if (!sell) {
                    jsonElement = jsonObject.get("collectionItemName");
                    if (jsonElement == null) {
                        message.addProperty("log", "no collection item name sent");
                    } else {
                        String collectionItemName = jsonObject.get("collectionItemName").getAsString();
                        message.addProperty("log", shopInterface.buy(collectionItemName, accountUser));
                    }
                } else {
                    jsonElement = jsonObject.get("collectionItemID");
                    if (jsonElement == null) {
                        message.addProperty("log", "no collection item ID sent");
                    } else {
                        String collectionItemID = jsonObject.get("collectionItemID").getAsString();
                        message.addProperty("log", shopInterface.sell(collectionItemID, accountUser));
                    }
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject getItemCount(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                String collectionItemName = jsonObject.get("collectionItemName").getAsString();
                message.addProperty("count", shopInterface.getItemCount(collectionItemName));
                message.addProperty("log", "successful action");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject getLeaderBoard() {
        JsonObject jsonObject = new JsonObject();
        String[] names = accountInterface.getLeaderboard();
        JsonArray jsonArray = new JsonArray();
        for (String name : names)
            jsonArray.add(name);
        jsonObject.add("leaderBoard", jsonArray);
        jsonObject.addProperty("log", "successfully printed leader board");
        return jsonObject;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}