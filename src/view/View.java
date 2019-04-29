package view;

import model.Account;
import model.CollectionItem;
import model.Deck;

import java.util.ArrayList;
import java.util.HashMap;

public interface View {
    void showAccountCreationError();

    void showNoSuchAccountError();

    void showIncorrectPasswordError();

    void showLeaderboard(ArrayList<Account> accounts);

    void showInvalidCommandError();

    void showHelp(String[] commands);

    void promptPassword();

    void alertAccountCreation();

    void alertLogin();

    void alertSave();

    void showCollection(HashMap<String, CollectionItem> collectionItems);

    void showID(ArrayList<String> collectionItemIDs);

    void showAlreadyExistingDeckError();

    void alertDeckCreation();

    void alertDeckDeletion();

    void showNoSuchDeckError();

    void showDeckAlreadyHasCollectionItemError();

    void showDeckIsFullError();

    void showDeckDoesNotExistError();

    void showAddingASecondItemToDeckError();

    void showAddingASecondHeroToDeckError();

    void alertCollectionItemAddedToDeck();

    void showNoSuchCollectionItemError();

    void alertCollectionItemRemovedFromDeck();

    void showDeckHasNoSuchCollectionItemError();

    void alertValidDeck();

    void showInvalidDeckError();

    void alertDeckSelection();

    void showDecks(ArrayList<Deck>decks);

    void showDeck(Deck deck);

    void showNotEnoughMoneyError();

    void showFourthItemError();

    void alertBuy();

    void alertSell();

    void showShop(ArrayList<CollectionItem> collectionItems);

    void showName(String name);
}
