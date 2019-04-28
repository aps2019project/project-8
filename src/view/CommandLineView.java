package view;

import model.Account;
import model.CollectionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    public void showCollection(ArrayList<CollectionItem> collectionItems) {
        for (CollectionItem collectionItem : collectionItems)
            System.out.println(collectionItem + String.valueOf(collectionItem.getPrice()));
    }

    @Override
    public void showID(ArrayList<String> collectionItemIDs) {
        if (collectionItemIDs != null) {
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
    public void showNoSuchDeckError() {
        System.out.println("No deck with this name exists.");
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
    public void showAddingASecondHeroError() {
        System.out.println("Cannot have 2 heroes in a single deck.");
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
    public void showDecks(HashMap<String, ArrayList<CollectionItem>> decks) {
        decks.forEach((deckName, deckCollectionItems) -> {
            System.out.println(deckName);
            deckCollectionItems.forEach(System.out::println);
        });
    }

    @Override
    public void showDeck(ArrayList<CollectionItem> deck) {
        deck.forEach(System.out::println);
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
