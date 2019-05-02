package gen;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;
import model.Collection;
import view.ShengdeBaoPrinter;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

// Incomplete!

/*
 *  this class gets the features from your input and stores
 *  your feedback in ManualFeatureInputLogs
 */

class ManualFeatureAdder {
    private static int numberOfCards = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static BufferedWriter writer;
    private static boolean hasArg = false;
    private static String arg3;
    private static String preffix = "";
    
    private static String getInput() {
        String string = scanner.nextLine();
        if (!hasArg) {

            try {
                writer.write(string + "\n");
            } catch (IOException io) {
                ShengdeBaoPrinter.println("IO Exception while reading");
            }
        }
        return string;
    }

    private static Buff addBuff() {
        ShengdeBaoPrinter.println("***Enter Buff***");
        int duration, holy, power, poison, weaknessAP, weaknessHP, unholy;
        boolean stun, disarm;
        ShengdeBaoPrinter.addString("Buff: ");

        {
            ShengdeBaoPrinter.println("Enter duration of the buff");
            duration = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter holy");
            holy = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter power");
            power = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter poison");
            poison = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter weakness AP");
            weaknessAP = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter weakness HP");
            weaknessHP = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter unholy");
            unholy = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter can stun (yes/no)");
            String feedBack = getInput();
            stun = feedBack.equals("yes");
        }
        {
            ShengdeBaoPrinter.println("Enter can disarm (yes/no)");
            String feedBack = getInput();
            disarm = feedBack.equals("yes");
        }

        ShengdeBaoPrinter.println("Add extra features to buff \"none\" to end!");
        String command = getInput();
        while (!command.matches("none")) {
            command = getInput();
        }

        ShengdeBaoPrinter.println("buff created");

        ShengdeBaoPrinter.undo();
        return new Buff.BuffBuilder()
                .setDuration(duration)
                .setHoly(holy)
                .setPower(power)
                .setPoison(poison)
                .setWeaknessAP(weaknessAP)
                .setWeaknessHP(weaknessHP)
                .setStun(stun)
                .setDisarm(disarm)
                .setUnholy(unholy)
                .build();
    }

    private static Spell addSpell() {
        ShengdeBaoPrinter.println("***Enter Spell***");
        Buff buff;
        SpellTarget spellTarget;
        ShengdeBaoPrinter.addString("Spell: ");
        {
            ShengdeBaoPrinter.println("Enter spell type:");
            int i = 1;
            ArrayList<SpellTarget> spellTargets = new ArrayList<>(EnumSet.allOf(SpellTarget.class));
            for (SpellTarget st : spellTargets) {
                ShengdeBaoPrinter.println(i + ". " + st);
                i++;
            }
            int selectedIndex = Integer.parseInt(getInput()) - 1;
            spellTarget = spellTargets.get(selectedIndex);
        }
        {
            buff = addBuff();
        }

        ShengdeBaoPrinter.println("Add extra features to spell \"none\" to end!");
        String command = getInput();
        while (!command.matches("none")) {
            command = getInput();
        }

        ShengdeBaoPrinter.println("spell created!");
        ShengdeBaoPrinter.undo();
        return new Spell.SpellBuilder()
                .setSpellTarget(spellTarget)
                .setBuff(buff)
                .build();
    }
    
    private static SpellCard addSpellCard(Card card) {
        ShengdeBaoPrinter.println("***Enter Spell Card***");
        Spell spell;
        ShengdeBaoPrinter.addString("Spell: ");
        {
            spell = addSpell();
        }
        ShengdeBaoPrinter.println("spell card created!");
        ShengdeBaoPrinter.undo();
        return new SpellCard.SpellCardBuilder()
                .setCard(card)
                .setSpell(spell)
                .build();
    }

    private static Unit addUnit(Card card) {
        ShengdeBaoPrinter.println("***Enter Unit***");
        int hitPoint, attackPoint;
        UnitType unitType;
        SpecialPowerType specialPowerType;
        Spell specialPower;
        boolean canFly;
        Faction faction;
        int attackRange;
        ShengdeBaoPrinter.addString("Unit: ");

        {
            ShengdeBaoPrinter.println("Enter hit point");
            hitPoint = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter attack point");
            attackPoint = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter unit type");
            ArrayList<UnitType> unitTypes = new ArrayList<>(EnumSet.allOf(UnitType.class));
            int i = 1;
            for (UnitType ut : unitTypes) {
                ShengdeBaoPrinter.println(i + ". " + ut);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            unitType = unitTypes.get(index);
        }
        {
            ShengdeBaoPrinter.println("Enter Special power type");
            ArrayList<SpecialPowerType> specialPowerTypes = new ArrayList<>(EnumSet.allOf(SpecialPowerType.class));
            int i = 1;
            for (SpecialPowerType spt : specialPowerTypes) {
                ShengdeBaoPrinter.println(i + ". " + spt);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            specialPowerType = specialPowerTypes.get(index);
        }
        {
            ShengdeBaoPrinter.println("Enter Unit Special power");
            specialPower = addSpell();
        }
        {
            ShengdeBaoPrinter.println("Enter can fly (yes/no)");
            String feedBack = getInput();
            canFly = feedBack.equals("yes");
        }
        {
            ShengdeBaoPrinter.println("Enter Faction");
            ArrayList<Faction> factions = new ArrayList<>(EnumSet.allOf(Faction.class));
            int i = 1;
            for (Faction f : factions) {
                ShengdeBaoPrinter.println(i + ". " + f);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            faction = factions.get(index);
        }
        {
            ShengdeBaoPrinter.println("Enter attack range");
            attackRange = Integer.parseInt(getInput());
        }

        ShengdeBaoPrinter.println("Unit created!");
        ShengdeBaoPrinter.undo();
        return new Unit.UnitBuilder()
                .setAttackPoint(attackPoint)
                .setHitPoint(hitPoint)
                .setAttackRange(attackRange)
                .setUnitType(unitType)
                .setFaction(faction)
                .setCanFly(canFly)
                .setSpecialPowerType(specialPowerType)
                .setSpell(specialPower)
                .setCard(card)
                .build();
    }

    private static Hero addHero(Unit unit) {
        ShengdeBaoPrinter.println("***Enter Hero***");
        ShengdeBaoPrinter.addString("Hero: ");
        ShengdeBaoPrinter.println("Hero created!");
        ShengdeBaoPrinter.undo();
        return new Hero(unit);
    }

    private static Minion addMinion(Unit unit) {
        ShengdeBaoPrinter.println("***Enter Minion***");
        ShengdeBaoPrinter.addString("Minion: ");
        ShengdeBaoPrinter.println("Minion created!");
        ShengdeBaoPrinter.undo();
        return new Minion(unit);
    }

    private static Item addItem(CollectionItem collectionItem) {
        ShengdeBaoPrinter.println("***Enter Item***");
        String description;
        ShengdeBaoPrinter.addString("Item: ");
        {
            ShengdeBaoPrinter.println("Enter item descriprion");
            description = getInput();
        }
        ShengdeBaoPrinter.undo();
        return new Item(collectionItem, description);
    }

    private static Usable addUsable(Item item) {
        ShengdeBaoPrinter.println("***Enter Usable***");
        Spell spell;
        ShengdeBaoPrinter.addString("Usable: ");
        {
            ShengdeBaoPrinter.println("Enter item spell");
            spell = addSpell();
        }
        ShengdeBaoPrinter.println("Usable created!");
        ShengdeBaoPrinter.undo();
        return new Usable(item, spell);
    }

    private static Collectible addCollectible(Item item) {
        ShengdeBaoPrinter.println("***Enter collectible***");
        Spell spell;
        ShengdeBaoPrinter.addString("Collectible: ");
        {
            ShengdeBaoPrinter.println("Enter item spell");
            spell = addSpell();
        }
        ShengdeBaoPrinter.println("Collectible created!");
        ShengdeBaoPrinter.undo();
        return new Collectible(item, spell);
    }

    private static CollectionItem addCollectionItem() {
        ShengdeBaoPrinter.println("***Enter collection Item***");
        String name, collectionItemID;
        int price;
        ShengdeBaoPrinter.addString("Collection Item: ");

        {
            ShengdeBaoPrinter.println("Enter collection item name");
            name = getInput();
        }
        {
            ShengdeBaoPrinter.println("Enter collection item id");
            collectionItemID = getInput();
        }
        {
            ShengdeBaoPrinter.println("Enter collection item price");
            price = Integer.parseInt(getInput());
        }
        ShengdeBaoPrinter.println("Collection item created!");
        ShengdeBaoPrinter.undo();
        return new CollectionItem(price, collectionItemID, name);
    }

    private static Card addCard(CollectionItem collectionItem) {
        ShengdeBaoPrinter.println("***Enter Card***");
        int manaCost;
        ShengdeBaoPrinter.addString("Card: ");
        {
            ShengdeBaoPrinter.println("Enter mana cost");
            manaCost = Integer.parseInt(getInput());
        }
        ShengdeBaoPrinter.println("Card created!");
        ShengdeBaoPrinter.undo();
        return new Card(collectionItem, manaCost);
    }

    private static void setUpWriterAndScanner() {
        if (!hasArg) {
            scanner = new Scanner(System.in);
            try {
                ShengdeBaoPrinter.println("Lets start!");
                writer = new BufferedWriter(new FileWriter("./gameData/ManualFeatureInputLogs/" + "tempLog" + ".txt"));
                ShengdeBaoPrinter.println("./gameData/ManualFeatureInputLogs/" + "tempLog" + ".txt " + " FILE CREATED!");
            } catch (IOException io) {
                ShengdeBaoPrinter.println("IO Exception : No such file name");
            }
        } else {
            try {
                scanner = new Scanner(new File(arg3));
            } catch (IOException io) {
                ShengdeBaoPrinter.println("No Such File exists");
            }
        }
    }

    private static void closeWriter() {
        if (!hasArg) {
            try {
                writer.close();
            } catch (IOException io) {
                ShengdeBaoPrinter.println("IO Exception while closing");
            }
        }
    }

    private static String getMultipleChoice(String message, String... args) {
        ShengdeBaoPrinter.print("");
        System.out.print(message + " ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg: args) {
            System.out.print(arg + " ");
            if(!stringBuilder.toString().isEmpty())
                stringBuilder.append("|");
            stringBuilder.append(arg);
        }
        String response = getInput();
        while (!response.matches(stringBuilder.toString())) {
            System.out.print("Again! " + stringBuilder + " ");
            response = getInput();
        }
        return response;

    }

    private static CollectionItem askUnit(Unit unit) {
        String response = getMultipleChoice("What type of unit?", "Hero", "Minion");
        switch (response) {
            case "Hero":
                return addHero(unit);
            case "Minion":
                return addMinion(unit);
        }
        return null;
    }

    private static CollectionItem askCard(Card card) {
        String response = getMultipleChoice("What type of card?", "Unit", "SpellCard");
        switch (response) {
            case "SpellCard":
                return addSpellCard(card);
            case "Unit":
                card = addUnit(card);
                return askUnit((Unit) card);
        }
        return null;
    }

    private static CollectionItem askItem(Item item) {
        String response = getMultipleChoice("What type of item?", "Usable", "Collectible");
        switch (response) {
            case "Usable":
                return addUsable(item);
            case "Collectible":
                return addCollectible(item);
        }
        return null;
    }

    private static CollectionItem askCollectionItem(CollectionItem collectionItem) {
        String response = getMultipleChoice("What do you want to enter?", "Card", "Item");
        switch (response) {
            case "Card":
                collectionItem = addCard(collectionItem);
                return askCard((Card) collectionItem);
            case "Item":
                collectionItem = addItem(collectionItem);
                return askItem((Item) collectionItem);
        }
        return null;
    }

    public static void main(String[] args) {

        // set up args and writer and scanner
        {
            hasArg = args.length >= 3;
            if (hasArg)
                arg3 = args[2];
            setUpWriterAndScanner();
        }
        ShengdeBaoPrinter.println("-----------Add Collection Item Feature Console Section-----------");
        CollectionItem collectionItem = addCollectionItem();
        collectionItem = askCollectionItem(collectionItem);
        if (hasArg) {
            ShengdeBaoPrinter.println("************** I HAVA ARG!!! ********** " + arg3);
            saveCollectionItem(collectionItem);
        }
        // close the writer
        {
            closeWriter();
        }
        if (!hasArg) {
            if (collectionItem == null) {
                System.out.println("Collection Item not created correctly! Something went wrong");
            } else {
                File f1 = new File("./gameData/ManualFeatureInputLogs/tempLog.txt");
                File f2 = new File("./gameData/ManualFeatureInputLogs/" + collectionItem.getName() + ".txt");
                if (f2.exists()) {
                    System.out.println("delet result is: " + f2.delete());
                }
                System.out.println("rename result is : " + f1.renameTo(f2));
            }
        }
    }


    private static void saveCollectionItem(CollectionItem collectionItem) {
        try {
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            FileWriter out;
            if (collectionItem instanceof Hero) {
                out = new FileWriter("./gameData/Heros/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Hero.class));
            } else if (collectionItem instanceof SpellCard) {
                out = new FileWriter("./gameData/SpellCards/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, SpellCard.class));
            } else if (collectionItem instanceof Minion) {
                out = new FileWriter("./gameData/Minions/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Minion.class));
            } else if (collectionItem instanceof Usable) {
                out = new FileWriter("./gameData/Usables/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Usable.class));
            } else if (collectionItem instanceof Collectible) {
                out = new FileWriter("./gameData/Collectibles/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Collectible.class));
            } else {
                ShengdeBaoPrinter.println("INSTANCE OF NOTHING");
                throw new IOException();
            }
            out.flush();
        } catch (IOException ignored) {
            ShengdeBaoPrinter.println("Can't Read file for some reason: ");
            ShengdeBaoPrinter.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }

}
