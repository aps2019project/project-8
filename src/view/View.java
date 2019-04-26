package view;

import model.Account;
import model.CollectionItem;

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

    void showCollection(ArrayList<CollectionItem> collectionItems);

    void showID(ArrayList<String> collectionItemID);

    void showAlreadyExistingDeckError();

    void alertDeckCreation();

    void alertDeckDeletion();

    void showNoSuchDeckError();

    void showDeckAlreadyHasCollectionItemError();

    void showDeckIsFullError();

    void showAddingASecondHeroError();

    void alertCollectionItemAddedToDeck();

    void showNoSuchCollectionItemError();

    void alertCollectionItemRemovedFromDeck();

    void showDeckHasNoSuchCollectionItemError();

    void alertValidDeck();

    void showInvalidDeckError();

    void alertDeckSelection();

    void showDecks(HashMap<String, ArrayList<CollectionItem>> decks);

    void showDeck(ArrayList<CollectionItem> deck);

    void showNotEnoughMoneyError();

    void showFourthItemError();

    void alertBuy();

    void alertSell();

    void showShop(ArrayList<CollectionItem> collectionItems);
}
