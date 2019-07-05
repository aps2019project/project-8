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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Server {

    private final static int DEFAULT_PORT = 6666;
    private int port;
    private ServerSocket serverSocket;
    private ShopInterface shopInterface;
    private AccountInterface accountInterface;
    private HashMap<String, AccountUser> players;
    private ArrayList<String> chats;
    private HashMap<String, Integer> messageIndex;

    private HashMap<AccountUser, AccountUser> sentRequest;
    private HashMap<AccountUser, ArrayList<AccountUser>> receivedRequests;

    public Server() {
        port = DEFAULT_PORT; // should be read from config file
        accountInterface = new AccountInterface();
        shopInterface = new ShopInterface();
        players = new HashMap<>();
        chats = new ArrayList<>();
        messageIndex = new HashMap<>();

        sentRequest = new HashMap<>();
        receivedRequests = new HashMap<>();
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
            messageIndex.put(token, chats.size());
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

    public JsonObject addChatMessage(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                jsonElement = jsonObject.get("message");
                if (jsonElement != null) {
                    chats.add(jsonElement.getAsString());
                    message.addProperty("log", "successfully received chat message");
                } else {
                    message.addProperty("log", "no chat message sent");
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject getNewMessages(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                JsonArray jsonArray = new JsonArray();
                int index = messageIndex.get(token);
                for (; index < chats.size(); index++) {
                    jsonArray.add(chats.get(index));
                }
                jsonObject.add("messages", jsonArray);
                jsonObject.addProperty("log", "successfully sent chat messages");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject getOnlineUsers() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (HashMap.Entry<String, AccountUser> entry : players.entrySet()) {
            jsonArray.add(entry.getValue().getName());
        }
        jsonObject.add("users", jsonArray);
        return jsonObject;
    }

    public JsonObject addGameRequest(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                if (sentRequest.get(accountUser) == null) {
                    jsonElement = jsonObject.get("opponentName");
                    if (jsonElement != null) {
                        AccountUser opponent = players.get(jsonElement.getAsString());
                        sentRequest.put(accountUser, opponent);
                        ArrayList<AccountUser> reqs = receivedRequests.get(opponent);
                        if (reqs == null) {
                            reqs = new ArrayList<>();
                        }
                        reqs.add(accountUser);
                        message.addProperty("log", "successfully received multiplayer game request");
                    } else {
                        message.addProperty("log", "no opponent user name sent");
                    }
                } else {
                    message.addProperty("log", "you already have sent another game reqeuest. cancel that first");
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }


    public JsonObject cancelGameRequest(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                sentRequest.remove(accountUser);
                message.addProperty("log", "cancelled all game requests sent");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject getGameRequests(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                ArrayList<AccountUser> reqs = receivedRequests.get(accountUser);
                JsonArray jsonArray = new JsonArray();
                if (reqs != null) {
                    for (AccountUser user : reqs)
                        jsonArray.add(user.getName());
                    message.addProperty("log", "your game requests were sent");
                } else {
                    message.addProperty("log", "you have no game requests");
                }
                message.add("users", jsonArray);
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public static void main(String[] args) {
        new Server().start();
    }
}