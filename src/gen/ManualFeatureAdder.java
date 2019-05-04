package gen;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;
import view.ShengdeBaoPrinter;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Complete!

/*
 *  this class gets the features from your input and stores
 *  your feedback in ManualFeatureInputLogs
 */

class ManualFeatureAdder {
    private static Scanner scanner = new Scanner(System.in);
    private static BufferedWriter writer;
    private static boolean hasArg = false;
    private static String arg3;
    private static String getInput() throws Exception {
        String string = scanner.nextLine();
        if (string.equals("exit"))
            throw new Exception();

        if (!hasArg) {
            try {
                writer.write(string + "\n");
            } catch (IOException io) {
                ShengdeBaoPrinter.println("IO Exception while reading");
            }
        }
        return string;
    }

    private static Buff addBuff() throws Exception {
        ShengdeBaoPrinter.println("***Enter Buff***");
        int duration, holy, poison, effectHp, effectAp;
        boolean stun, disarm, dispellable;

        ShengdeBaoPrinter.addString("Buff: ");
        {
            ShengdeBaoPrinter.println("Enter duration of the buff");
            duration = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter holy (negative for unholy)");
            holy = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter poison");
            poison = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter effectHp (negative for weaknessHp)");
            effectHp = Integer.parseInt(getInput());
        }
        {
            ShengdeBaoPrinter.println("Enter effectAp (negative for weaknessAp)");
            effectAp = Integer.parseInt(getInput());
        }
        {
            String feedBack = getMultipleChoice("Enter can stun", "yes", "no");
            stun = feedBack.equals("yes");
        }
        {
            String feedBack = getMultipleChoice("Enter can disarm", "yes", "no");
            disarm = feedBack.equals("yes");
        }
        {
            String feedBack = getMultipleChoice("Enter dipellable" , "yes", "no");
            dispellable = feedBack.equals("yes");
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
                .setPoison(poison)
                .setEffectHp(effectHp)
                .setEffectAp(effectAp)
                .setStun(stun)
                .setDisarm(disarm)
                .setDispellable(dispellable)
                .build();
    }

    private static Spell addSpell() throws Exception {
        ShengdeBaoPrinter.println("***Enter Spell***");
        Spell.TargetType targetType = null;
        Spell.TargetArea targetArea = null;
        Spell.TargetUnit targetUnit = null;
        Spell.TargetUnitType targetUnitType = null;
        int numberOfRandomTargets = Spell.MAX_GOALS;
        Buff[] buffs = null;
        boolean canDispel;
        int gridX = 1;
        int gridY = 1;

        ShengdeBaoPrinter.addString("Spell: ");
        {
            ShengdeBaoPrinter.println("Enter target type");
            ArrayList<Spell.TargetType> targetTypes = new ArrayList<>(EnumSet.allOf(Spell.TargetType.class));
            int i = 1;
            for (Spell.TargetType tt : targetTypes) {
                ShengdeBaoPrinter.println(i + ". " + tt);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            targetType = targetTypes.get(index);

            if (targetType == Spell.TargetType.UNIT) {
                ShengdeBaoPrinter.println("Enter target unit");
                ArrayList<Spell.TargetUnit> targetUnits = new ArrayList<>(EnumSet.allOf(Spell.TargetUnit.class));
                i = 1;
                for (Spell.TargetUnit tu : targetUnits) {
                    ShengdeBaoPrinter.println(i + ". " + tu);
                    i++;
                }
                index = Integer.parseInt(getInput()) - 1;
                targetUnit = targetUnits.get(index);

                ShengdeBaoPrinter.println("Enter target unit type");
                ArrayList<Spell.TargetUnitType> targetUnitTypes = new ArrayList<>(EnumSet.allOf(Spell.TargetUnitType.class));
                i = 1;
                for (Spell.TargetUnitType tut : targetUnitTypes) {
                    ShengdeBaoPrinter.println(i + ". " + tut);
                    i++;
                }
                index = Integer.parseInt(getInput()) - 1;
                targetUnitType = targetUnitTypes.get(index);
            }

            ShengdeBaoPrinter.println("Enter target Area");
            ArrayList<Spell.TargetArea> targetAreas = new ArrayList<>(EnumSet.allOf(Spell.TargetArea.class));
            i = 1;
            for (Spell.TargetArea ta : targetAreas) {
                ShengdeBaoPrinter.println(i + ". " + ta);
                i++;
            }
            index = Integer.parseInt(getInput()) - 1;
            targetArea = targetAreas.get(index);

            if (targetArea == Spell.TargetArea.SELECTED_X_Y_GRID) {
                ShengdeBaoPrinter.println("Enter number of grid rows (GridX):");
                gridX = Integer.parseInt(getInput());
                ShengdeBaoPrinter.println("Enter number of grid columns (GridY):");
                gridY = Integer.parseInt(getInput());
            }
        }
        {
            String response = getMultipleChoice("Is it a random spell", "yes", "no");
            if (response.equals("yes")) {
                ShengdeBaoPrinter.println("Enter the number of random picks!");
                numberOfRandomTargets = Integer.parseInt(getInput());
            }
        }
        {
            ShengdeBaoPrinter.println("Enter number of buffs");
            int n = Integer.parseInt(getInput()), i = 1;
            if (n > 0) {
                buffs = new Buff[n];
                while (i <= n) {
                    ShengdeBaoPrinter.println("Enter buff number (" + i + ") :");
                    buffs[i - 1] = addBuff();
                    i++;
                }
            }
        }

        {
            String response = getMultipleChoice("Enter can dispel:", "yes", "no");
            canDispel = response.equals("yes");
        }

        Spell spell = new Spell.SpellBuilder()
                .setBuffs(buffs)
                .setCanDispel(canDispel)
                .setNumberOfRandomTargets(numberOfRandomTargets)
                .setGridX(gridX)
                .setGridY(gridY)
                .setTargetType(targetType)
                .setTargetArea(targetArea)
                .setTargetUnit(targetUnit)
                .setTargetUnitType(targetUnitType)
                .build();

        ShengdeBaoPrinter.println("Add extra features to spell \"none\" to end!");
        String command = getInput();
        while (!command.matches("none")) {
            command = getInput();
        }

        ShengdeBaoPrinter.println("spell created!");
        ShengdeBaoPrinter.undo();
        return spell;
    }

    private static SpellCard addSpellCard(Card card) throws Exception {
        ShengdeBaoPrinter.println("***Enter Spell Card***");
        Spell spell;
        ShengdeBaoPrinter.addString("SpellCard: ");
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

    private static Unit addUnit(Card card) throws Exception {
        ShengdeBaoPrinter.println("***Enter Unit***");
        int hitPoint, attackPoint;
        UnitType unitType;
        ArrayList<SpecialPowerType> specialPowerType = new ArrayList<>();
        ArrayList<Spell> specialPower = new ArrayList<>();
        boolean canFly;
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
            String response = getMultipleChoice("has special power", "yes", "no");
            if (response.equals("yes")) {
                {
                    getSpecialPowerType(specialPowerType);
                }
                {
                    ShengdeBaoPrinter.println("Enter special power (spell)");
                    specialPower.add(addSpell());
                }
            }
        }
        {
            String feedBack = getMultipleChoice("Enter can fly", "yes", "no");
            canFly = feedBack.equals("yes");
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
                .setCanFly(canFly)
                .setSpecialPowerType(specialPowerType)
                .setSpecialPower(specialPower)
                .setCard(card)
                .build();
    }

    private static Hero addHero(Unit unit) throws Exception {
        ShengdeBaoPrinter.println("***Enter Hero***");
        ShengdeBaoPrinter.addString("Hero: ");
        int coolDown;
        {
            ShengdeBaoPrinter.print("Add cool down:");
            coolDown = Integer.parseInt(getInput());
        }
        ShengdeBaoPrinter.println("Hero created!");
        ShengdeBaoPrinter.undo();
        return new Hero(unit, coolDown);
    }

    private static Minion addMinion(Unit unit) throws Exception {
        ShengdeBaoPrinter.println("***Enter Minion***");
        ShengdeBaoPrinter.addString("Minion: ");
        ShengdeBaoPrinter.println("Minion created!");
        ShengdeBaoPrinter.undo();
        return new Minion(unit);
    }

    private static Item addItem(CollectionItem collectionItem) throws Exception {
        ShengdeBaoPrinter.println("***Enter Item***");
        Spell spell = null;
        int addMana = 0, addManaDuration = 0;
        ItemType itemType = null;
        ArrayList<SpecialPowerType> specialPowerType = new ArrayList<>();
        ArrayList<Spell> specialPower = new ArrayList<>();
        ArrayList<Target> specialPowerTarget = new ArrayList<>();

        ShengdeBaoPrinter.addString("Item: ");
        {
            String response = getMultipleChoice("Does it cast a spell on something (unit or cell)?", "yes", "no");
            if (response.equals("yes")) {
                itemType = ItemType.CAST_A_SPELL;
                ShengdeBaoPrinter.println("Enter Spell:");
                spell = addSpell();
            } else {
                response = getMultipleChoice("Does it add mana to the player?", "yes", "no");
                if (response.equals("yes")) {
                    {
                        itemType = ItemType.ADD_MANA;
                        ShengdeBaoPrinter.println("Enter add Mana:");
                        addMana = Integer.parseInt(getInput());
                    }
                    {
                        ShengdeBaoPrinter.println("Enter add Mana duration:");
                        addManaDuration = Integer.parseInt(getInput());
                    }
                } else {
                    ShengdeBaoPrinter.println("Enter number of special powers to be added:");
                    int numberOfSpecialPowers = Integer.parseInt(getInput());
                    for (int i = 0; i < numberOfSpecialPowers; i++) {
                        getSpecialPowerType(specialPowerType);
                        getSpecialPowerTarget(specialPowerTarget);
                        {
                            ShengdeBaoPrinter.println("Enter special power (spell)");
                            specialPower.add(addSpell());
                        }
                    }
                }
            }
        }
        ShengdeBaoPrinter.undo();
        return new Item.ItemBuilder()
                .setSpell(spell)
                .setAddMana(addMana)
                .setAddManaDuration(addManaDuration)
                .setCollectionItem(collectionItem)
                .setItemType(itemType)
                .setSpecialPowerType(specialPowerType)
                .setSpecialPower(specialPower)
                .setSpecialPowerTarget(specialPowerTarget)
                .build();
    }

    private static void getSpecialPowerTarget(ArrayList<Target> specialPowerTarget) {
        try {
            ShengdeBaoPrinter.println("Enter target unit");
            ArrayList<Target.TargetUnit> targetUnits = new ArrayList<>(EnumSet.allOf(Target.TargetUnit.class));
            int i = 1;
            for (Target.TargetUnit tu : targetUnits) {
                ShengdeBaoPrinter.println(i + ". " + tu);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            Target.TargetUnit targetUnit = targetUnits.get(index);

            ShengdeBaoPrinter.println("Enter target unit type");
            ArrayList<Target.TargetUnitType> targetUnitTypes = new ArrayList<>(EnumSet.allOf(Target.TargetUnitType.class));
            i = 1;
            for (Target.TargetUnitType tut : targetUnitTypes) {
                ShengdeBaoPrinter.println(i + ". " + tut);
                i++;
            }
            index = Integer.parseInt(getInput()) - 1;
            Target.TargetUnitType targetUnitType = targetUnitTypes.get(index);

            specialPowerTarget.add(new Target(targetUnit, targetUnitType));
        } catch (Exception ignored) {}
    }

    private static void getSpecialPowerType(ArrayList<SpecialPowerType> specialPowerType) throws Exception {
        ShengdeBaoPrinter.println("Enter special power type");
        ArrayList<SpecialPowerType> specialPowerTypes = new ArrayList<>(EnumSet.allOf(SpecialPowerType.class));
        int j = 1;
        for (SpecialPowerType spt : specialPowerTypes) {
            ShengdeBaoPrinter.println(j + ". " + spt);
            j++;
        }
        int index = Integer.parseInt(getInput()) - 1;
        specialPowerType.add(specialPowerTypes.get(index));
    }

    private static Usable addUsable(Item item) throws Exception {
        ShengdeBaoPrinter.println("***Enter Usable***");
        ShengdeBaoPrinter.addString("Usable: ");
        ShengdeBaoPrinter.println("Usable created!");
        ShengdeBaoPrinter.undo();
        return new Usable(item);
    }

    private static Collectible addCollectible(Item item) throws Exception {
        ShengdeBaoPrinter.println("***Enter collectible***");
        ShengdeBaoPrinter.addString("Collectible: ");
        ShengdeBaoPrinter.println("Collectible created!");
        ShengdeBaoPrinter.undo();
        return new Collectible(item);
    }

    private static CollectionItem addCollectionItem() throws Exception {
        ShengdeBaoPrinter.println("***Enter collection Item***");
        String name, collectionItemID;
        int price;
        String description;

        ShengdeBaoPrinter.addString("Collection Item: ");

        {
            ShengdeBaoPrinter.println("Enter collection item name");
            name = getInput();
        }
        {
            ShengdeBaoPrinter.println("Enter description (empty for none)");
            description = getInput();
        }
        {
            ShengdeBaoPrinter.println("Enter collection item id");
            collectionItemID = getInput();
        }
        {
            ShengdeBaoPrinter.println("Enter collection item price (0 if it is collectible)");
            price = Integer.parseInt(getInput());
        }
        ShengdeBaoPrinter.println("Collection item created!");
        ShengdeBaoPrinter.undo();
        return new CollectionItem(price, collectionItemID, name, description);
    }

    private static Card addCard(CollectionItem collectionItem) throws Exception {
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

    private static String getMultipleChoice(String message, String... args) throws Exception {
        ShengdeBaoPrinter.print("");
        System.out.print(message + " ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            System.out.print(arg + " ");
            if (!stringBuilder.toString().isEmpty())
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

    private static CollectionItem askUnit(Unit unit) throws Exception {
        String response = getMultipleChoice("What type of unit?", "Hero", "Minion");
        switch (response) {
            case "Hero":
                return addHero(unit);
            case "Minion":
                return addMinion(unit);
        }
        return null;
    }

    private static CollectionItem askCard(Card card) throws Exception {
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

    private static CollectionItem askItem(Item item) throws Exception {
        String response = getMultipleChoice("What type of item?", "Usable", "Collectible");
        switch (response) {
            case "Usable":
                return addUsable(item);
            case "Collectible":
                return addCollectible(item);
        }
        return null;
    }

    private static CollectionItem askCollectionItem(CollectionItem collectionItem) throws Exception {
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
        try {
            ShengdeBaoPrinter.println("-----------Add Collection Item Feature Console Section-----------");
            CollectionItem collectionItem = addCollectionItem();
            collectionItem = askCollectionItem(collectionItem);
            if (hasArg) {
                ShengdeBaoPrinter.println("************** I HAVA ARG!!! ********** " + arg3);
                saveCollectionItem(collectionItem);
            }
            if (!hasArg) {
                if (collectionItem == null) {
                    System.out.println("Collection Item not created correctly! Something went wrong");
                } else {
                    closeWriter();
                    Path original = Paths.get("./gameData/ManualFeatureInputLogs/tempLog.txt");
                    Path copied = Paths.get("./gameData/ManualFeatureInputLogs/" + collectionItem.getName() + ".txt");
                    CopyOption[] options = new CopyOption[]{
                            StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES
                    };
                    Files.copy(original, copied, options);
                }
            }
        } catch (Exception allException) {
            {
                closeWriter();
            }
        }
    }


    private static void saveCollectionItem(CollectionItem collectionItem) {
        try {
            YaGson yaGson = new  YaGsonBuilder().setPrettyPrinting().create();
            FileWriter out;
            if (collectionItem instanceof Hero) {
                out = new FileWriter("./gameData/Heroes/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Hero.class));
            } else if (collectionItem instanceof SpellCard) {
                out = new FileWriter("./gameData/SpellCards/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, SpellCard.class));
            } else if (collectionItem instanceof Minion) {
                out = new FileWriter("./gameData/Minions/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Minion.class));
            } else if (collectionItem instanceof Usable) {
                out = new FileWriter("./gameData/Usables/" + collectionItem.getName() + ".json", false);
                out.write(yaGson.toJson(collectionItem, Usable.class));
            } else if (collectionItem instanceof Collectible) {
                out = new FileWriter("./gameData/Collectibles/" + collectionItem.getName() + ".json", false);
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
