package view;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public interface
View {
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

    void showDecks(ArrayList<Deck> decks);

    void showDeck(Deck deck);

    void showNotEnoughMoneyError();

    void showFourthItemError();

    void alertBuy();

    void alertSell();

    void showShop(ArrayList<CollectionItem> collectionItems);

    void showName(String name);

    void showInvalidCardError();

    void showInvalidTargetError();

    void showNotEnoughManaError();

    void showInvalidCardIDError();

    void logMessage(String message);

    void showAIDeckInformation(AI ai);

    void showAccount(Account account);

    void alertSecondAccountSelection();

    void showInvalidParametersError();

    void showSecondPlayerInvalidMainDeckError();

    void showDeckInformation(Deck deck);

    void showNoSuchGameModeError();

    void showSecondPlayerHasNoMainDeckError();

    void showInvalidCoordinatesError();

    void showShopCollection(HashMap<String, CollectionItem> collectionItems);

    void showCellHasContentError();

    void showGraveyard(ArrayList<Card> graveYard);

    void showCardInfo(Card card);

    void showHand(Hand hand);

    void showColletibleInfo(Collectible selectedCollectible);

    void showCollectibles(ArrayList<Collectible> collectibles);

    void showStoryModes(HashMap<Integer, Deck> decks, HashMap<Integer, Integer> gameMode);

    void showInfoOfCollectionItem(CollectionItem collectionItem);

    void showNoMainDeckError();

    void showUnit(Unit unit);

    void infoShowNumberOfFlags(ArrayList<Unit> units);

    void showGameInfoKillOponentHero(Hero firstHero, Hero secondHero);

    void showGameInfoHoldTheFlag(int row, int column, Unit content);

    void showNoSuchUnitFoundError();

    void showUnitInfo(Unit unit);

    void showCollectible(Collectible collectible);
}
