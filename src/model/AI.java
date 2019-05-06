package model;

import com.gilecode.yagson.YaGson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

// SmsS is great
public class AI {
    private static HashMap<Integer, Deck> defaultDecks = new HashMap<>();
    private static HashMap<Integer, Integer> gameMode = new HashMap<>();
    private static HashMap<Integer, Integer> gamePrize = new HashMap<>();
    private static int newID = 0;
    private Deck deck;
    private Game game;

    public AI(Deck deck) {
        this.deck = new Deck(deck);
    }

    public static AI get(int aiID) {
        return new AI(defaultDecks.get(aiID));
    }

    public static GameType getGameType(int aiID) {
        return GameType.get(gameMode.get(aiID));
    }

    public static int getNumberOfFlags(int aiID) {
        if (getGameType(aiID) == GameType.COLLECT_THE_FLAGS)
            return 7;
        return -1;
    }

    public static int getGamePrize(int aiID) {
        return gamePrize.get(aiID);
    }

    public static void load() {
        try {
            for (File file : new File("./gameData/AIDecks/").listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                int id = Integer.parseInt(bufferedReader.readLine());
                resetNewID();
                defaultDecks.put(id, new Deck(String.valueOf(id)));
                for (int i = 0; i < 22; i++) {
                    defaultDecks.get(id).addCollectionItem(getCollectionItem(bufferedReader.readLine()), getNewID());
                }
                gameMode.put(id, Integer.parseInt(bufferedReader.readLine()));
                gamePrize.put(id, Integer.parseInt(bufferedReader.readLine()));
            }
        } catch (Exception ignored) {}
    }

    private static CollectionItem getCollectionItem(String collectionItemID) {
        try {
            YaGson yaGson = new YaGson();
            switch (Integer.parseInt(collectionItemID) / 1000) {
                case 1:
                    for (File file : new File("./gameData/SpellCards/").listFiles()) {
                        SpellCard spellCard = yaGson.fromJson(new String(Files.readAllBytes(Paths.
                                get(file.getAbsolutePath())), StandardCharsets.UTF_8), SpellCard.class);
                        if (spellCard.getCollectionItemID().equals(collectionItemID))
                            return spellCard;
                    }
                    return null;
                case 2:
                    for (File file : new File("./gameData/Minions/").listFiles()) {
                        Minion minion = yaGson.fromJson(new String(Files.readAllBytes(Paths.
                                get(file.getAbsolutePath())), StandardCharsets.UTF_8), Minion.class);
                        if (minion.getCollectionItemID().equals(collectionItemID))
                            return minion;
                    }
                    return null;
                case 3:
                    for (File file : new File("./gameData/Heroes/").listFiles()) {
                        Hero hero = yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())),
                                StandardCharsets.UTF_8), Hero.class);
                        if (hero.getCollectionItemID().equals(collectionItemID))
                            return hero;
                    }
                    return null;
                default:
                    for (File file : new File("./gameData/Usables/").listFiles()) {
                        Usable usable = yaGson.fromJson(new String(Files.readAllBytes(Paths.get(file.
                                getAbsolutePath())), StandardCharsets.UTF_8), Usable.class);
                        if (usable.getCollectionItemID().equals(collectionItemID))
                            return usable;
                    }
                    return null;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static void resetNewID() {
        newID = 0;
    }

    private static String getNewID() {
        return String.valueOf(newID++);
    }

    public static HashMap<Integer, Deck> getDecks() {
        return defaultDecks;
    }

    public static HashMap<Integer, Integer> getGameMode() {
        return gameMode;
    }

    public Player getPlayer() {
        return new Player(deck);
    }

    public void makeMove() {
        //rand 0 -> n
        //e.g. : rand = 0 -> move : game.moveSelectedUnit(rand, rand)
        //e.g. : rand = 1 -> insert : game.insertCard(randCardName, rand, rand)
        //..
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
