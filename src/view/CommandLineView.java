package view;

import model.*;
import sun.java2d.pipe.OutlineTextRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CommandLineView implements View {
    private static final String DASH = " - ";

    public StringBuilder output;

    public void clean() {
        output = new StringBuilder();
    }

    public String getMessages() {
        return output.toString();
    }


//    private static void appendTabs(StringBuilder stringBuilder, int d) {
//        for (int i = 0; i < d; i++) {
//            stringBuilder.append("\t");
//        }
//    }

//    @Override
//    public void showAccountCreationError() {
//        System.out.println("An account with this name already exists.");
//    }
//
//    @Override
//    public void showIncorrectPasswordError() {
//        System.out.println("Incorrect password entered.");
//    }
//
//    @Override
//    public void showNoSuchAccountError() {
//        System.out.println("No account with this name exists.");
//    }
//
//    @Override
//    public void showLeaderboard(ArrayList<AccountUser> accounts) {
//        Collections.sort(accounts);
//        for (int i = 0; i < accounts.size(); i++)
//            System.out.println((i + 1) + accounts.get(i).toString());
//    }

    @Override
    public void showInvalidCommandError() {
        output.append("Invalid command entered.\n");

        //System.out.println("Invalid command entered.");
    }

    @Override
    public void showHelp(String[] commands) {
        if (commands == null) {
            return;
        }
        for (String command : commands)
            output.append(command).append("\n");
    }

//    @Override
//    public void promptPassword() {
//        System.out.println("Please enter the password.");
//    }
//
//    @Override
//    public void alertAccountCreation() {
//        System.out.println("Account successfully created.");
//    }
//
//    @Override
//    public void alertLogin() {
//        System.out.println("Successfully logged into account.");
//    }
//
//    @Override
//    public void alertSave() {
//        System.out.println("Successfully saved account.");
//    }
//
//    @Override
//    public void showCollection(HashMap<String, CollectionItem> collectionItems) {
//        if (collectionItems.isEmpty()) {
//            System.out.println("Your collection is empty!");
//            return;
//        }
//        showCollectionItemsWithPrice(new ArrayList<>(collectionItems.values()), "Sell");
//    }
//
//    @Override
//    public void showID(ArrayList<String> collectionItemIDs) {
//        if (!collectionItemIDs.isEmpty()) {
//            collectionItemIDs.stream().sorted().forEach(System.out::println);
//            return;
//        }
//        System.out.println("No such item found.");
//    }
//
//    @Override
//    public void showAlreadyExistingDeckError() {
//        System.out.println("A deck with this name already exists.");
//    }
//
//    @Override
//    public void alertDeckCreation() {
//        System.out.println("Deck successfully created.");
//    }
//
//    @Override
//    public void alertDeckDeletion() {
//        System.out.println("Deck successfully deleted.");
//    }
//
//    @Override
//    public void showDeckAlreadyHasCollectionItemError() {
//        System.out.println("The selected deck already has this collection item.");
//    }

//    @Override
//    public void showDeckIsFullError() {
//        System.out.println("The selected deck is full.");
//    }
//
//    @Override
//    public void showDeckDoesNotExistError() {
//        System.out.println("Deck with this name doesn't exist");
//    }
//
//    @Override
//    public void showAddingASecondHeroToDeckError() {
//        System.out.println("Cannot have 2 heroes in a single deck.");
//    }
//
//    @Override
//    public void showAddingASecondItemToDeckError() {
//        System.out.println("Cannot have 2 items in a single deck.");
//    }
//
//    @Override
//    public void alertCollectionItemAddedToDeck() {
//        System.out.println("Collection item successfully added to deck.");
//    }
//
//    @Override
//    public void showNoSuchCollectionItemError() {
//        System.out.println("No such collection item found.");
//    }

//    @Override
//    public void alertCollectionItemRemovedFromDeck() {
//        System.out.println("Collection item successfully removed from deck.");
//    }
//
//    @Override
//    public void showDeckHasNoSuchCollectionItemError() {
//        System.out.println("The selected deck has no such collection item.");
//    }
//
//    @Override
//    public void alertValidDeck() {
//        System.out.println("The deck is valid.");
//    }
//
//    @Override
//    public void showInvalidDeckError() {
//        System.out.println("The deck is invalid");
//    }
//
//    @Override
//    public void alertDeckSelection() {
//        System.out.println("Main deck successfully changed.");
//    }
//
//    @Override
//    public void showDecks(ArrayList<Deck> decks) {
//        if (decks.isEmpty()) {
//            System.out.println("No deck.");
//            return;
//        }
//        for (int i = 0; i < decks.size(); i++) {
//            Deck deck = decks.get(i);
//            System.out.println((i + 1) + " : " + deck.getDeckName());
//            deck.incrementTabsOnToString();
//            System.out.println(deck);
//            deck.decrementTabsOnToString();
//        }
//    }

//    @Override
//    public void showDeck(Deck deck) {
//        System.out.println(deck);
//    }
//
//    @Override
//    public void showNotEnoughMoneyError() {
//        System.out.println("Not enough money.");
//    }
//
//    @Override
//    public void showFourthItemError() {
//        System.out.println("Cannot have 4 items in collection.");
//    }
//
//    @Override
//    public void alertBuy() {
//        System.out.println("Successfully bought the collection item.");
//    }
//
//    @Override
//    public void alertSell() {
//        System.out.println("Successfully sold the collection item.");
//    }
//
//    @Override
//    public void showShop(ArrayList<CollectionItem> collectionItems) {
//        showCollectionItemsWithPrice(collectionItems, "Buy");
//    }

//    private void showCollectionItemsWithPrice(ArrayList<CollectionItem> collectionItems, String tradeKind) {
//        System.out.println("Heroes :");
//        int index = 1;
//        for (CollectionItem collectionItem : collectionItems) {
//            if (collectionItem instanceof Hero) {
//                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
//            }
//        }
//        System.out.println("Items : ");
//        index = 1;
//        for (CollectionItem collectionItem : collectionItems) {
//            if (collectionItem instanceof Item)
//                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
//        }
//        System.out.println("Cards : ");
//        index = 1;
//        for (CollectionItem collectionItem : collectionItems) {
//            if (collectionItem instanceof Card && !(collectionItem instanceof Hero))
//                index = showIndexedCollectionItemWithPrice(index, collectionItem, tradeKind);
//        }
//    }

//    private int showIndexedCollectionItemWithPrice(int index, CollectionItem collectionItem, String tradeKind) {
//        System.out.println("\t" + index++ + " : " + collectionItem + DASH + tradeKind + " Cost : " + collectionItem.getPrice());
//        return index;
//    }

//    @Override
//    public void showName(String name) {
//        if (name != null) {
//            System.out.println(name);
//            return;
//        }
//        System.out.println("No such collection item found.");
//    }

    @Override
    public void showInvalidCardError() {
        output.append("Invalid card name\n");

//        System.out.println("Invalid card name");
    }

    @Override
    public void showTargetOutOfRangeError() {
        output.append("Target out of range\n");

//        System.out.println("Target out of range");
    }

    @Override
    public void showNotEnoughManaError() {
        output.append("You don't have enough mana\n");

//        System.out.println("You don't have enough mana");
    }

    @Override
    public void showInvalidCardIDError() {
        output.append("Invalid card id\n");

//        System.out.println("Invalid card id");
    }

    @Override
    public void logMessage(String message) {
        output.append(message).append("\n");

//        System.out.println(message);
    }

//    @Override
//    public void showAccount(AccountUser account) {
//        System.out.println(account.getName());
//    }
//
//    @Override
//    public void alertSecondAccountSelection() {
//        System.out.println("Second account successfully selected.");
//    }
//
//    @Override
//    public void showInvalidParametersError() {
//        System.out.println("The entered parameters are invalid.");
//    }
//
//    @Override
//    public void showSecondPlayerInvalidMainDeckError() {
//        System.out.println("Selected deck for second player is invalid.");
//    }
//
//    @Override
//    public void showDeckInformation(Deck deck) {
//        System.out.println(deck.getDeckName() + " : " + deck.getHero().getName());
//    }
//
//    @Override
//    public void showNoSuchGameModeError() {
//        System.out.println("No such game mode exists.");
//    }
//
//    @Override
//    public void showSecondPlayerHasNoMainDeckError() {
//        System.out.println("The selected player has no main deck.");
//    }

    @Override
    public void showInvalidCoordinatesError() {
        output.append("Coordinates out of range\n");

//        System.out.println("Coordinates out of range");
    }

//    @Override
//    public void showShopCollection(HashMap<String, CollectionItem> collectionItems) {
//        showCollectionItemsWithPrice(new ArrayList<>(collectionItems.values()), "Sell");
//    }

//    @Override
//    public void showCellHasContentError() {
//        System.out.println("Cell is already full!");
//    }

    @Override
    public void showGraveyard(ArrayList<Card> graveYard) {
        for (Card card : graveYard)
            output.append(card).append("\n");

//        graveYard.forEach(System.out::println);
    }

    @Override
    public void showCardInfo(Card card) {
        output.append(card.toString()).append("\n");

//        System.out.println(card);
    }

    @Override
    public void showHand(Hand hand) {
        output.append(hand.toString()).append("\n");

//        System.out.println(hand);
    }

//    @Override
//    public void showColletibleInfo(Collectible selectedCollectible) {
//        System.out.println(selectedCollectible);
//    }

    @Override
    public void showCollectibles(ArrayList<Collectible> collectibles) {
        for (Collectible collectible : collectibles)
            output.append(collectible.getCollectionItemID()).append(": ").append(collectible.toString()).append("\n");


//        collectibles.forEach(o -> System.out.println(o.getCollectionItemID() + ": " + o));
    }

//    @Override
//    public void showStoryModes(HashMap<Integer, Deck> decks, HashMap<Integer, Integer> gameMode) {
//        decks.forEach((level, deck) -> {
//            System.out.println(level + ":");
//            System.out.println(deck.getHero().getName());
//            System.out.println(GameType.get(gameMode.get(level)));
//        });
//    }

//    @Override
//    public void showInfoOfCollectionItem(CollectionItem collectionItem) {
//        System.out.println(collectionItem.showInfo());
//    }

//    @Override
//    public void showNoMainDeckError() {
//        System.out.println("You have no main deck to play with.");
//    }

    @Override
    public void infoShowNumberOfFlags(ArrayList<Unit> units) {
        if (units.isEmpty()) {
            output.append("No on has a flag!");
//            System.out.println("No one has a flag!");
            return;
        }
        for (Unit unit : units) {
            output.append(unit.getName() + " for " + unit.getPlayer().getName() + " has " + unit.getNumberOfFlags() + " flags!\n\n");

//            System.out.print(unit.getName() + " for " + unit.getPlayer().getName() + " has " + unit.getNumberOfFlags() + " flags!\n");
        }
    }

    @Override
    public void showGameInfoKillOponentHero(Hero firstHero, Hero secondHero) {
        output.append("Player one hero name is: " + firstHero.getName() + " and has " + firstHero.getHitPoint() + "hp.").append("\n");
        output.append("Player two hero name is: " + secondHero.getName() + " and has " + secondHero.getHitPoint() + "hp.").append("\n");

//        System.out.println("Player one hero name is: " + firstHero.getName() + " and has " + firstHero.getHitPoint() + "hp.");
//        System.out.println("Player two hero name is: " + secondHero.getName() + " and has " + secondHero.getHitPoint() + "hp.");
    }

    @Override
    public void showGameInfoHoldTheFlag(int row, int column, Unit content) {
        if (content == null) {
            output.append("No one has a flag\n");

//            System.out.println("No one has a flag");
            return;
        }
        output.append("Unit " + content.getName() + " in row(" + row + ") and column(" + column + ") has the flag!\n");
//        System.out.println("Unit " + content.getName() + " in row(" + row + ") and column(" + column + ") has the flag!");
    }

    @Override
    public void showNoSuchUnitFoundError() {
        output.append("No such unit found\n");

//        System.out.println("No such unit found.");
    }

    @Override
    public void showUnitInfo(Unit unit) {
        if (unit instanceof Hero) {

            output.append("Hero:\n");
            output.append("Name: " + unit.getName() + "\n");
            output.append("Cost:" + unit.getPrice() + "\n");
            output.append("Desc: " + unit.getDescription() + "\n");


//            System.out.println("Hero:");
//            System.out.println("Name: " + unit.getName());
//            System.out.println("Cost: " + unit.getPrice());
//            System.out.println("Desc: " + unit.getDescription());
            return;
        }

        output.append("Minion:" + "\n");
        output.append("HP: " + unit.calculateHP() + " AP: " + unit.calculateAP() + " MP: " + unit.getManaCost() + "\n");
        output.append("Range: " + unit.getAttackRange() + "\n");
        output.append("Combo-ability: " + unit.getSpecialPowerTypes().contains(SpecialPowerType.COMBO) + "\n");
        output.append("Cost: " + unit.getPrice());
        output.append("Desc: " + unit.getDescription());


//        System.out.println("Minion:");
//        System.out.println("HP: " + unit.calculateHP() + " AP: " + unit.calculateAP() + " MP: " + unit.getManaCost());
//        System.out.println("Range: " + unit.getAttackRange());
//        System.out.println("Combo-ability: " + unit.getSpecialPowerTypes().contains(SpecialPowerType.COMBO));
//        System.out.println("Cost: " + unit.getPrice());
//        System.out.println("Desc: " + unit.getDescription());

    }

    @Override
    public void showCollectible(Collectible collectible) {
        if (collectible == null) {
            output.append("No collectible selected.\n");

//            System.out.println("No collectible selected.");
            return;
        }
        output.append(collectible.toString() + "\n");

//        System.out.println(collectible);
    }


    @Override
    public void showUnit(Unit unit) {
        output.append(unit.getCollectionItemID() + ": " + unit.getName() + ", health: " + unit.calculateHP() +
                ", location: (" + (unit.getX() + 1) + ", " + (unit.getY() + 1) + "), power: " + unit.calculateAP());


//        System.out.print(unit.getCollectionItemID() + ": " + unit.getName() + ", health: " + unit.calculateHP() +
//                ", location: (" + (unit.getX() + 1) + ", " + (unit.getY() + 1) + "), power: " + unit.calculateAP());
        if (unit.getBuffs().stream().anyMatch(buff -> buff.getHoly() > 0)) {
            output.append(" holy");

//            System.out.print(" holy");
        } else if (unit.getBuffs().stream().anyMatch(buff -> buff.getHoly() < 0)) {
            output.append(" unholy");

//            System.out.print(" unholy");
        }
        if (unit.getBuffs().stream().anyMatch(buff -> buff.getPoison() != 0)) {
            output.append(" poison");

//            System.out.print(" poison");
        }
        if (unit.getBuffs().stream().anyMatch(buff -> buff.getEffectHp() > 0 || buff.getEffectAp() > 0)) {
            output.append(" powerBuff");

//            System.out.print(" powerBuff");

        } else if (unit.getBuffs().stream().anyMatch(buff -> buff.getEffectHp() < 0 || buff.getEffectAp() < 0)) {
            output.append(" weaknessBuff");

//            System.out.print(" weaknessBuff");
//
        }
        if (unit.getBuffs().stream().anyMatch(Buff::canStun)) {
            output.append(" stun");

//            System.out.print(" stun");
        }
        if (unit.getBuffs().stream().anyMatch(Buff::canDisarm)) {
            output.append(" disarm");

//            System.out.print(" disarm");
        }
        if (unit.getSpecialPowerTypes().stream().anyMatch(e -> e == SpecialPowerType.ON_ATTACK)) {
            output.append("on Attack");

//            System.out.print(" onAttack");
        }
        if (unit.getSpecialPowerTypes().stream().anyMatch(e -> e == SpecialPowerType.ON_SPAWN)) {
            output.append(" onSpawn");

//            System.out.print(" onSpawn");
        }
        if (unit.getSpecialPowerTypes().stream().anyMatch(e -> e == SpecialPowerType.PASSIVE)) {
            output.append(" passive");

//            System.out.print(" passive");
        }
        if (unit.getSpecialPowerTypes().stream().anyMatch(e -> e == SpecialPowerType.ON_DEFEND)) {
            output.append(" onDefend");

//            System.out.print(" onDefend");
        }
        output.append("\n");
//        System.out.print("\n");
    }

    @Override
    public void showCooldownError() {
        output.append("Hero is not yet cool to use special power !\n");

//        System.out.println("Hero is not yet cool to use special power !");
    }

    @Override
    public void alertUnitSelection(String cardID) {
        output.append("Selected unit " + cardID + "\n");

//        System.out.println("Selected unit " + cardID);
    }

    @Override
    public void alertCollectibleSelection(String collectibleID) {
        output.append("Selected collectible " + collectibleID + "\n");

//        System.out.println("Selected collectible " + collectibleID);
    }

    @Override
    public void showUnableToMoveError() {
        output.append("Unable to move.\n");

//        System.out.println("Unable to move.");
    }

    @Override
    public void showPathBlockedError() {
        output.append("No path found.\n");

//        System.out.println("No path found.");
    }

    @Override
    public void showNoUnitSelectedError() {
        output.append("No unit selected");

//        System.out.println("No unit selected.");
    }

    @Override
    public void showUncastableItemError() {
        output.append("This item is not castable.\n");

//        System.out.println("This item is not castable.");
    }

    @Override
    public void showUnitsReadyToMove(ArrayList<Card> availableOptions) {
        output.append("Units ready to move:\n");

//        System.out.println("Units ready to move:");
        for (Card card : availableOptions)
            output.append(card.toString() + "\n");

//        availableOptions.forEach(System.out::println);
    }

    @Override
    public void showUnitsAvailableForAttack(ArrayList<Card> availableOptions) {
        output.append("Units available for attack:\n");
        for (Card card : availableOptions)
            output.append(card.toString() + "\n");

//        System.out.println("Units available for attack:");
//        availableOptions.forEach(System.out::println);

    }

    @Override
    public void showCardsReadyToBePlayed(ArrayList<Card> availableOptions) {
        output.append("Cards ready to be played:\n");
        for (Card card : availableOptions)
            output.append(card.toString() + "\n");

//        System.out.println("Cards ready to be played:");
//        availableOptions.forEach(System.out::println);
    }

    @Override
    public void showWinner(AccountUser account, int prize) {
        output.append("The winner is: " + account.getName() + "\n");
        output.append(prize + "$$$\n");

//        System.out.println("The winner is: " + account.getName());
//        System.out.println(prize + "$$$");
    }

    @Override
    public void alertCPUWin() {
        output.append("HAHA! You lost to this stupid shit!\n");
//        System.out.println("HAHA! You lost to this stupid shit!");
    }

    @Override
    public void showInvalidTargetError() {
        output.append("Invalid target\n");

//        System.out.println("Invalid target");
    }

//    @Override
//    public void showInvalidMainDeckError() {
//        System.out.println("Your main deck is invalid.");
//    }

    @Override
    public void showNoAdjacentFriendlyUnitError() {
        output.append("No friendly unit in the vicinity.\n");

//        System.out.println("No friendly unit in the vicinity.");
    }

//    @Override
//    public void alertExport(String fileName) {
//        System.out.println("Deck successfully exported to " + fileName);
//    }
//
//    @Override
//    public void showNoSuchFileError() {
//        System.out.println("No such file found.");
//    }
//
//    @Override
//    public void showInvalidFileError() {
//        System.out.println("The provided file is invalid.");
//    }
//
//    @Override
//    public void showImportedCardError() {
//        System.out.println("Imported card not found.");
//    }
//
//    @Override
//    public void showSuccessfulImport() {
//        System.out.println("Deck successfully imported.");
//    }
//
//    @Override
//    public void showGameCannotLoadError() {
//        System.out.println("Game can't load");
//    }
}
