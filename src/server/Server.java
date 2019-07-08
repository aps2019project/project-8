package server;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.com.google.gson.JsonArray;
import com.gilecode.yagson.com.google.gson.JsonElement;
import com.gilecode.yagson.com.google.gson.JsonObject;
import gen.JsonMaker;
import interfaces.AccountInterface;
import interfaces.GameInterface;
import interfaces.ShopInterface;
import model.AccountData;
import model.AccountUser;
import model.CollectionItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Server {

    private final static int DEFAULT_PORT = 6666;
    private static Server instance = new Server();
    private int port;
    private ServerSocket serverSocket;
    private ShopInterface shopInterface;
    private AccountInterface accountInterface;
    private HashMap<String, AccountUser> players;
    private ArrayList<String> chats;
    private HashMap<String, Integer> messageIndex;
    private ArrayList<AccountUser> inList;
    private GameInterface gameInterface;

//    private HashMap<AccountUser, AccountUser> sentRequest;
//    private HashMap<AccountUser, ArrayList<AccountUser>> receivedRequests;

    public Server() {
        port = DEFAULT_PORT; // should be read from config file
        accountInterface = new AccountInterface();
        shopInterface = new ShopInterface();
        players = new HashMap<>();
        chats = new ArrayList<>();
        messageIndex = new HashMap<>();

        inList = new ArrayList<>();

//        sentRequest = new HashMap<>();
//        receivedRequests = new HashMap<>();

        gameInterface = new GameInterface();
    }

    public static Server getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance.start();
    }

    public HashMap<String, AccountUser> getPlayers() {
        return players;
    }

    public ShopInterface getShopInterface() {
        return shopInterface;
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
            jsonObject.addProperty("log", "a user with this username already exists");
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
                if (collectionItemName.contains("7headed"))
                    System.err.println("sending item count " + collectionItemName + " " + shopInterface.getItemCount(collectionItemName));
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

    /*

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
                    message.addProperty("log", "you already have sent another game request. cancel that first");
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
                String deckValid = gameInterface.checkAccount(accountUser);
                if (deckValid.equals("ok")) {
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
                } else {
                    message.addProperty("log", deckValid);
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject startGame(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                jsonElement = jsonObject.get("requesterName");
                if (jsonElement == null) {
                    message.addProperty("log","no requester name sent");
                } else {
                    AccountUser requester = accountInterface.getAccount(jsonElement.getAsString());
                    if (requester == null) {
                        message.addProperty("log", "no such user");
                    } else {
                        ArrayList<AccountUser> reqs = receivedRequests.get(accountUser);
                        if (reqs.indexOf(requester) == -1) {
                            message.addProperty("log", "this user has not requested to play with you");
                        } else {
                            String deckValid = gameInterface.checkAccount(accountUser);
                            if (deckValid.equals("ok")) {

                                message.addProperty("log", "the game is about to begin...");

                                // here something happens;

                            } else {
                                message.addProperty("log", deckValid);
                            }
                        }
                    }
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject addNewCard(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                CollectionItem collectionItem = new YaGson().fromJson(jsonObject, CollectionItem.class);
                shopInterface.saveData(collectionItem);
                message.addProperty("log", "successfully created card " + collectionItem.getName());
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    */

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
                messageIndex.remove(token);
                for (; index < chats.size(); index++) {
                    jsonArray.add(chats.get(index));
                }
                messageIndex.put(token, chats.size());
                message.add("messages", jsonArray);
                message.addProperty("log", "successfully sent chat messages");
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

    public JsonObject addFuckingNewCard(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                String s = jsonObject.get("object").getAsString();
                String name = jsonObject.get("name").getAsString();
                int count = Integer.valueOf(jsonObject.get("count").getAsString());

                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(new File("./gameData/ManualFeatureInputLogs/" + name + ".txt"), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileWriter.append(s);
                    fileWriter.flush();
                    fileWriter.close();

                    JsonMaker.main(new String[]{"java", "JsonMaker"});
                    shopInterface.load();
                    CollectionItem collectionItem = shopInterface.getCollectionItemByName(name);
                    collectionItem.setCount(count);
                    shopInterface.saveData(collectionItem);
                    File file = new File("./gameData/ManualFeatureInputLogs/" + name + ".txt");
                    if (file.delete()) {
                    } else {
                        System.err.println("fucking file not deleted");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                message.addProperty("log", "fucking card added");
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject enterMulti(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                boolean in = jsonObject.get("in").getAsString().equals("yes");
                if (in) {
                    if (inList.size() > 0) {
                        AccountUser b = inList.remove(0);


                        int mode = Integer.valueOf(jsonObject.get("mode").getAsString());
                        int flags = Integer.valueOf(jsonObject.get("flags").getAsString());

                        System.err.println("created game" + accountUser.getName() + " " + b.getName() + " " + mode + " " + flags);

                        gameInterface.startGame(accountUser, b, mode, flags);
                        // game start game game here some thing happens
                    } else {
                        inList.add(accountUser);
                    }
                } else {
                    inList.remove(accountUser);
                    message.addProperty("log", "you were removed from list");
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject sendGameCommand(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                jsonElement = jsonObject.get("command");
                if (jsonElement != null) {
                    message.addProperty("log", gameInterface.sendCommand(accountUser, jsonElement.getAsString()));
                } else {
                    message.addProperty("log", "no command sent");
                }
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject checkMe(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {
                message.addProperty("log", gameInterface.inGame(accountUser));
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }

    public JsonObject addWin(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("authenticationToken");
        JsonObject message = new JsonObject();
        if (jsonElement != null) {
            String token = jsonElement.getAsString();
            AccountUser accountUser = players.get(token);
            if (accountUser == null) {
                message.addProperty("log", "your authentication token has expired");
            } else {


                int wins = Integer.valueOf(jsonObject.get("win").getAsString());
                int money = Integer.valueOf(jsonObject.get("money").getAsString());

                System.err.println("HERE TO ADD WIN FOR " + accountUser.getName() + " " + wins + " " + money);

                for (int i = 0; i < wins; i++)
                    accountUser.addWin();
                accountUser.receiveMoney(money);
                accountUser.saveAccount();
            }
        } else {
            message.addProperty("log", "no authentication token sent");
        }
        return message;
    }
}