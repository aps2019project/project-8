package view;

import model.Account;
import model.Card;
import model.CollectionItem;
import model.Deck;

import java.util.*;

public class CommandLineView implements View {
    @Override
    public void showAccountCreationError() {
        System.out.println("An account with this name already exists.");
    }

    @Override
    public void showIncorrectPasswordError() {
        System.out.println("Incorrect password entered.");
    }

    @Override
    public void showNoSuchAccountError() {
        System.out.println("No account with this name exists.");
    }

    @Override
    public void showLeaderboard(ArrayList<Account> accounts) {
        Collections.sort(accounts);
        for (int i = 0; i < accounts.size(); i++)
            System.out.println((i + 1) + accounts.get(i).toString());
    }

    @Override
    public void showInvalidCommandError() {
        System.out.println("Invalid command entered.");
    }

    @Override
    public void showHelp(String[] commands) {
        if (commands == null) {
            return;
        }
        for (String command : commands)
            System.out.println(command);
    }

    @Override
    public void promptPassword() {
        System.out.println("Please enter the password.");
    }

    @Override
    public void alertAccountCreation() {
        System.out.println("Account successfully created.");
    }

    @Override
    public void alertLogin() {
        System.out.println("Successfully logged into account.");
    }

    @Override
    public void alertSave() {
        System.out.println("Successfully saved account.");
    }

    @Override
    public void showCollection(HashMap<String, CollectionItem> collectionItems) {
        if (collectionItems.isEmpty()) {
            System.out.println("Your collection is empty!");
            return;
        }
        collectionItems.forEach((collectionItemID, collectionItem) -> {
            System.out.println(collectionItemID + " -> " + collectionItem.getName());
        });
    }

    @Override
    public void showID(ArrayList<String> collectionItemIDs) {
        if (!collectionItemIDs.isEmpty()) {
            collectionItemIDs.forEach(System.out::println);
            return;
        }
        System.out.println("No such item found.");
    }

    @Override
    public void showAlreadyExistingDeckError() {
        System.out.println("A deck with this name already exists.");
    }

    @Override
    public void alertDeckCreation() {
        System.out.println("Deck successfully created.");
    }

    @Override
    public void alertDeckDeletion() {
        System.out.println("Deck successfully deleted.");
    }

    @Override
    public void showDeckAlreadyHasCollectionItemError() {
        System.out.println("The selected deck already has this collection item.");
    }

    @Override
    public void showDeckIsFullError() {
        System.out.println("The selected deck is full.");
    }

    @Override
    public void showDeckDoesNotExistError() {
        System.out.println("Deck with this name doesn't exist");
    }

    @Override
    public void showAddingASecondHeroToDeckError() {
        System.out.println("Cannot have 2 heroes in a single deck.");
    }

    @Override
    public void showAddingASecondItemToDeckError() {
        System.out.println("Cannot have 2 items in a single deck.");
    }

    @Override
    public void alertCollectionItemAddedToDeck() {
        System.out.println("Collection item successfully added to deck.");
    }

    @Override
    public void showNoSuchCollectionItemError() {
        System.out.println("No such collection item found.");
    }

    @Override
    public void alertCollectionItemRemovedFromDeck() {
        System.out.println("Collection item successfully removed from deck.");
    }

    @Override
    public void showDeckHasNoSuchCollectionItemError() {
        System.out.println("The selected deck has no such collection item.");
    }

    @Override
    public void alertValidDeck() {
        System.out.println("The deck is valid.");
    }

    @Override
    public void showInvalidDeckError() {
        System.out.println("The deck is invalid");
    }

    @Override
    public void alertDeckSelection() {
        System.out.println("Main deck successfully changed.");
    }

    @Override
    public void showDecks(ArrayList<Deck> decks) {
        if (decks.isEmpty()) {
            System.out.println("No deck.");
            return;
        }
        for (int i = 0; i < decks.size(); i++) {
            Deck deck = decks.get(i);
            System.out.println((i + 1) + " : " + deck.getDeckName());
            deck.incrementTabsOnToString();
            System.out.println(deck);
            deck.decrementTabsOnToString();
        }
    }

    @Override
    public void showDeck(Deck deck) {
        System.out.println(deck);
    }

    @Override
    public void showNotEnoughMoneyError() {
        System.out.println("Not enough money.");
    }

    @Override
    public void showFourthItemError() {
        System.out.println("Cannot have 4 items in collection.");
    }

    @Override
    public void alertBuy() {
        System.out.println("Successfully bought the collection item.");
    }

    @Override
    public void alertSell() {
        System.out.println("Successfully sold the collection item.");
    }

    @Override
    public void showShop(ArrayList<CollectionItem> collectionItems) {
        collectionItems.forEach(System.out::println);
    }

    @Override
    public void showName(String name) {
        if (name != null) {
            System.out.println(name);
            return;
        }
        System.out.println("No such collection item found.");
    }
}
