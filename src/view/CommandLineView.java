package view;

import model.*;

import java.util.*;

public class CommandLineView implements View {
    private static final String DASH = " - ";

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
        showCollectionItemsWithPrice(new ArrayList<>(collectionItems.values()), "Sell");
    }

    @Override
    public void showID(ArrayList<String> collectionItemIDs) {
        if (!collectionItemIDs.isEmpty()) {
            collectionItemIDs.stream().sorted().forEach(System.out::println);
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
        showCollectionItemsWithPrice(collectionItems, "Buy");
    }

    private static void appendTabs(StringBuilder stringBuilder, int d) {
        for (int i = 0; i < d; i++) {
            stringBuilder.append("\t");
        }
    }

    private void showCollectionItemsWithPrice(ArrayList<CollectionItem> collectionItems, String tradeKind) {
        System.out.println("Heroes :");
        int index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Hero) {
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
            }
        }
        System.out.println("Items : ");
        index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Item)
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
        }
        System.out.println("Cards : ");
        index = 1;
        for (CollectionItem collectionItem : collectionItems) {
            if (collectionItem instanceof Card && !(collectionItem instanceof Hero))
                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
        }
    }

    private int showIndexedCollectionItemWithPrice(int index, CollectionItem collectionItem, String tradeKind) {
        System.out.println("\t" + index++ + " : " + collectionItem + DASH + tradeKind + " Cost : " + collectionItem.getPrice());
        return index;
    }

    @Override
    public void showName(String name) {
        if (name != null) {
            System.out.println(name);
            return;
        }
        System.out.println("No such collection item found.");
    }

    @Override
    public void showInvalidCardError() {
        System.out.println("Invalid card name");
    }

    @Override
    public void showTargetOutOfRangeError() {
        System.out.println("Target out of range");
    }

    @Override
    public void showNotEnoughManaError() {
        System.out.println("You don't have enough mana");
    }

    @Override
    public void showInvalidCardIDError() {
        System.out.println("Invalid card id");
    }

    @Override
    public void logMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showAIDeckInformation(AI ai) {
    }

    @Override
    public void showAccount(Account account) {
        System.out.println(account.getName());
    }

    @Override
    public void alertSecondAccountSelection() {
        System.out.println("Second account successfully selected.");
    }

    @Override
    public void showInvalidParametersError() {
        System.out.println("The entered parameters are invalid.");
    }

    @Override
    public void showSecondPlayerInvalidMainDeckError() {
        System.out.println("Selected deck for second player is invalid.");
    }

    @Override
    public void showDeckInformation(Deck deck) {
        System.out.println(deck.getDeckName() + " : " + deck.getHero().getName());
    }

    @Override
    public void showNoSuchGameModeError() {
        System.out.println("No such game mode exists.");
    }

    @Override
    public void showSecondPlayerHasNoMainDeckError() {
        System.out.println("The selected player has no main deck.");
    }

    @Override
    public void showInvalidCoordinatesError() {
        System.out.println("Coordinates out of range");
    }

    @Override
    public void showShopCollection(HashMap<String, CollectionItem> collectionItems) {
        showCollectionItemsWithPrice(new ArrayList<>(collectionItems.values()), "Sell");
    }

    @Override
    public void showCellHasContentError() {
        System.out.println("Cell is already full!");
    }

    @Override
    public void showGraveyard(ArrayList<Card> graveYard) {
        graveYard.forEach(System.out::println);
    }

    @Override
    public void showCardInfo(Card card) {
        System.out.println(card);
    }

    @Override
    public void showHand(Hand hand) {
        System.out.println(hand);
    }

    @Override
    public void showColletibleInfo(Collectible selectedCollectible) {
        System.out.println(selectedCollectible);
    }

    @Override
    public void showCollectibles(ArrayList<Collectible> collectibles) {
        collectibles.forEach(System.out::println);
    }

    @Override
    public void showStoryModes(HashMap<Integer, Deck> decks, HashMap<Integer, Integer> gameMode) {
        decks.forEach((level, deck) -> {
            System.out.println(level + ":");
            System.out.println(deck.getHero().getName());
            System.out.println(GameType.get(gameMode.get(level)));
        });
    }

    @Override
    public void showInfoOfCollectionItem(CollectionItem collectionItem) {
        System.err.println(collectionItem.showInfo());
    }

    @Override
    public void showNoMainDeckError() {
        System.out.println("You have no main deck to play with.");
    }

    @Override
    public void infoShowNumberOfFlags(ArrayList<Unit> units) {
        if (units.isEmpty()) {
            System.out.println("No one has a flag!");
            return;
        }
        for (Unit unit: units) {
            System.out.print(unit.getName() + " for " + unit.getPlayer().getName() + " has " + unit.getNumberOfFlags() + " flags!\n");
        }
    }

    @Override
    public void showGameInfoKillOponentHero(Hero firstHero, Hero secondHero) {
        System.out.println("Player one hero name is: " + firstHero.getName() + " and has " + firstHero.getHitPoint() + "hp.");
        System.out.println("Player two hero name is: " + secondHero.getName() + " and has " + secondHero.getHitPoint() + "hp.");
    }

    @Override
    public void showGameInfoHoldTheFlag(int row, int column, Unit content) {
        if (content == null) {
            System.out.println("No one has a flag");
            return;
        }
        System.out.println("Unit " + content.getName() + " in row(" + row + ") and column(" + column + ") has the flag!");
    }

    @Override
    public void showNoSuchUnitFoundError() {
        System.out.println("No such unit found.");
    }

    @Override
    public void showUnitInfo(Unit unit) {
        if (unit instanceof Hero) {
            System.out.println("Hero:");
            System.out.println("Name: " + unit.getName());
            System.out.println("Cost: " + unit.getPrice());
            System.out.println("Desc: " + unit.getDescription());
            return;
        }
        System.out.println("Minion:");
        System.out.println("HP: " + unit.calculateHP() + " AP: " + unit.calculateAP() + " MP: " + unit.getManaCost());
        System.out.println("Range: " + unit.getAttackRange());
        System.out.println("Combo-ability: " + unit.getSpecialPowerTypes().contains(SpecialPowerType.COMBO));
        System.out.println("Cost: " + unit.getPrice());
        System.out.println("Desc: " + unit.getDescription());

    }

    @Override
    public void showCollectible(Collectible collectible) {
        if (collectible == null) {
            System.err.println("No collectible selected.");
            return;
        }
        System.out.println(collectible);
    }


    @Override
    public void showUnit(Unit unit) {
        System.out.println(unit.getCollectionItemID() + ": " + unit.getName() + ", health: " + unit.calculateHP() +
                ", location: (" + (unit.getX() + 1) + ", " + (unit.getY() + 1) + "), power: " + unit.calculateAP());
    }

    @Override
    public void showCooldownError() {
        System.out.println("Hero is not yet cool to use special power !");
    }

    @Override
    public void alertUnitSelection(String cardID) {
        System.out.println("Selected unit " + cardID);
    }

    @Override
    public void alertCollectibleSelection(String collectibleID) {
        System.out.println("Selected collectible " + collectibleID);
    }

    @Override
    public void showUnableToMoveError() {
        System.out.println("Unable to move.");
    }

    @Override
    public void showPathBlockedError() {
        System.out.println("No path found.");
    }

    @Override
    public void showNoUnitSelectedError() {
        System.out.println("No unit selected.");
    }
}
