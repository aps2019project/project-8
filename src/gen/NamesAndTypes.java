package gen;

import model.Collection;
import model.CollectionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NamesAndTypes {
    public static ArrayList<String> spellCards = new ArrayList<>();
    public static ArrayList<String> usables = new ArrayList<>();
    public static ArrayList<String> collectibles = new ArrayList<>();
    public static ArrayList<String> heroes = new ArrayList<>();
    public static ArrayList<String> minions = new ArrayList<>();

    private static Map<String, CollectionItem> collectionItemMap = new HashMap<>();

    public static void addCollectionItem(CollectionItem collectionItem) {
        collectionItemMap.put(collectionItem.getName(), collectionItem);
    }

    public static void addSpellCard(String name) {
        spellCards.add(name);
    }

    public static void addUsable(String name) {
        usables.add(name);
    }

    public static void addCollectible(String name) {
        collectibles.add(name);
    }

    public static void addHero(String name) {
        heroes.add(name);
    }

    public static void addMinion(String name) {
        minions.add(name);
    }

    public static String getType(String name) {
        String type = null;
        if (spellCards.contains(name))
            type = "spellCard";
        if (usables.contains(name))
            type = "usable";
        if (collectibles.contains(name))
            type = "collectible";
        if (heroes.contains(name))
            type = "hero";
        if (minions.contains(name))
            type = "minion";
        return type;
    }

    public static void showNumbers() {
        System.err.println("Number of spellCards: " + spellCards.size());
        System.err.println("Number of Usables: " + usables.size());
        System.err.println("Number of collectibles: " + collectibles.size());
        System.err.println("Number of heroes: " + heroes.size());
        System.err.println("Number of minions: " + minions.size());
    }

    public static CollectionItem getCollectionItem(String name) {
        if (collectionItemMap.containsKey(name)) {
            return collectionItemMap.get(name);
        }
        return null;
    }
}
