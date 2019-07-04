package model;

/*
import java.util.*;

public class Account implements Comparable {
    private String name;
    private String password;
    private int wins = 0;
    private int money = 0;
    private ArrayList<Match> matchHistory = new ArrayList<>();
    private Collection collection = new Collection();
    private Deck mainDeck = null;

    public AccountUser(String name, String password) {
        this.name = name;
        this.password = password;
//        accounts.add(this);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof AccountUser))
            return 0;
        return Comparator.comparingInt(o1 -> ((AccountUser) o1).getWins()).reversed().compare(this, o);
    }

    @Override
    public String toString() {
        return " - UserName : " + name + " - Wins : " + wins;
    }

    public boolean isPasswordValid(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void payMoney(int sum) {
        money -= sum;
    }

    public void receiveMoney(int sum) {
        money += sum;
    }

    public void addMatch(Match match) {
        matchHistory.add(match);
    }

    public void addWin() {
        wins++;
    }

    public int getWins() {
        return wins;
    }

    public AccountData getData() {
        return data;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

//    public Account(Account account) {
//        accounts.add(account);
//    }


//    public static ArrayList<Account> getAccounts() {
//        return accounts;
//    }

//    public static Account getAccount(String name) {
//        for (Account account : accounts)
//            if (account.name.equals(name))
//                return account;
//        return null;
//    }

//    public static boolean hasAccount(String name) {
//        for (Account account : accounts)
//            if (account.name.equalsIgnoreCase(name))
//                return true;
//        return false;
//    }



    public boolean hasThreeItems() {
        return collection.hasThreeItems();
    }

    public Collection getCollection() {
        return collection;
    }

    public HashMap<String, CollectionItem> getCollectionItems() {
        return collection.getCollectionItems();
    }

    public Deck getDeck(String deckName) {
        return collection.getDeck(deckName);
    }

    public Deck getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(Deck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public Player getPlayer() {
        return new Player(getMainDeck());
    }

    public ArrayList<Deck> getDecks() {
        return collection.getDecks();
    }

    public void importDeck(Deck deck) throws Exception {
        HashMap<String, CollectionItem> collectionItems = new HashMap<>(collection.getCollectionItems());
        String deckName = deck.getDeckName();
        int index = 0;
        while (collection.hasDeck(deckName + index))
            index++;
        Deck newDeck = new Deck(deck.getDeckName() + index);
        Optional<Map.Entry<String, CollectionItem>> hero = collectionItems.entrySet().stream().filter(e -> e.getValue().
                equals(deck.getHero())).findAny();
        Exception importedCardException = new Exception("Imported card not found.");
        if (hero.isPresent()) {
            Map.Entry<String, CollectionItem> heroEntry = hero.get();
            newDeck.addCollectionItem(heroEntry.getValue(), heroEntry.getKey());
        } else
            throw importedCardException;
        if (deck.getDeckUsableItem() != null) {
            Optional<Map.Entry<String, CollectionItem>> usableItem = collectionItems.entrySet().stream().filter(e -> e.getValue().
                    equals(deck.getDeckUsableItem())).findAny();
            if (usableItem.isPresent()) {
                Map.Entry<String, CollectionItem> itemEntry = usableItem.get();
                newDeck.addCollectionItem(itemEntry.getValue(), itemEntry.getKey());
            } else
                throw importedCardException;
        }
        StringBuilder problem = new StringBuilder();
        deck.getCards().forEach(o -> {
            Optional<Map.Entry<String, CollectionItem>> card = collectionItems.entrySet().stream().filter(e -> e.getValue().
                    equals(o)).findAny();
            if (card.isPresent()) {
                Map.Entry<String, CollectionItem> cardEntry = card.get();
                newDeck.addCollectionItem(cardEntry.getValue(), cardEntry.getKey());
                collectionItems.remove(cardEntry.getKey());
            } else
                problem.append("problem");
        });
        if (problem.length() != 0)
            throw importedCardException;
        collection.importDeck(newDeck);
    }
}
*/