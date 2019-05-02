package gen;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;
import model.Collection;

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

    private static String getInput() {
        String string = scanner.nextLine();
        if (!hasArg) {

            try {
                writer.write(string + "\n");
            } catch (IOException io) {
                System.out.println("IO Exception while reading");
            }
        }
        return string;
    }

    private static Buff addBuff() {
        System.out.println("***Enter Buff***");
        int duration, holy, power, poison, weaknessAP, weaknessHP, unholy;
        boolean stun, disarm;

        {
            System.out.println("Enter duration of the buff");
            duration = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter holy");
            holy = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter power");
            power = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter poison");
            poison = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter weakness AP");
            weaknessAP = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter weakness HP");
            weaknessHP = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter unholy");
            unholy = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter can stun (yes/no)");
            String feedBack = getInput();
            stun = feedBack.equals("yes");
        }
        {
            System.out.println("Enter can disarm (yes/no)");
            String feedBack = getInput();
            disarm = feedBack.equals("yes");
        }

        System.out.println("Add extra features to buff \"none\" to end!");
        String command = getInput();
        while (!command.matches("none")) {
            command = getInput();
        }

        System.out.println("buff created");
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
        System.out.println("***Enter Spell***");
        Buff buff;
        SpellTarget spellTarget;
        {
            System.out.println("Enter spell type:");
            int i = 1;
            ArrayList<SpellTarget> spellTargets = new ArrayList<>(EnumSet.allOf(SpellTarget.class));
            for (SpellTarget st : spellTargets) {
                System.out.println(i + ". " + st);
                i++;
            }
            int selectedIndex = Integer.parseInt(getInput()) - 1;
            spellTarget = spellTargets.get(selectedIndex);
        }
        {
            buff = addBuff();
        }

        System.out.println("Add extra features to spell \"none\" to end!");
        String command = getInput();
        while (!command.matches("none")) {
            command = getInput();
        }

        System.out.println("spell created!");
        return new Spell.SpellBuilder()
                .setSpellTarget(spellTarget)
                .setBuff(buff)
                .build();
    }

    private static SpellCard addSpellCard() {
        System.out.println("***Enter Spell Card***");
        Spell spell;
        {
            spell = addSpell();
        }
        System.out.println("spell card created!");
        return new SpellCard(spell);
    }

    private static Unit addUnit() {
        System.out.println("***Enter Unit***");
        int hitPoint, attackPoint;
        UnitType unitType;
        SpecialPowerType specialPowerType;
        Spell specialPower;
        boolean canFly;
        Faction faction;
        int attackRange;

        {
            System.out.println("Enter hit point");
            hitPoint = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter attack point");
            attackPoint = Integer.parseInt(getInput());
        }
        {
            System.out.println("Enter unit type");
            ArrayList<UnitType> unitTypes = new ArrayList<>(EnumSet.allOf(UnitType.class));
            int i = 1;
            for (UnitType ut : unitTypes) {
                System.out.println(i + ". " + ut);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            unitType = unitTypes.get(index);
        }
        {
            System.out.println("Enter Special power type");
            ArrayList<SpecialPowerType> specialPowerTypes = new ArrayList<>(EnumSet.allOf(SpecialPowerType.class));
            int i = 1;
            for (SpecialPowerType spt : specialPowerTypes) {
                System.out.println(i + ". " + spt);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            specialPowerType = specialPowerTypes.get(index);
        }
        {
            System.out.println("Enter Unit Special power");
            specialPower = addSpell();
        }
        {
            System.out.println("Enter can fly (yes/no)");
            String feedBack = getInput();
            canFly = feedBack.equals("yes");
        }
        {
            System.out.println("Enter Faction");
            ArrayList<Faction> factions = new ArrayList<>(EnumSet.allOf(Faction.class));
            int i = 1;
            for (Faction f : factions) {
                System.out.println(i + ". " + f);
                i++;
            }
            int index = Integer.parseInt(getInput()) - 1;
            faction = factions.get(index);
        }
        {
            System.out.println("Enter attack range");
            attackRange = Integer.parseInt(getInput());
        }

        return new Unit.UnitBuilder()
                .setAttackPoint(attackPoint)
                .setHitPoint(hitPoint)
                .setAttackRange(attackRange)
                .setUnitType(unitType)
                .setFaction(faction)
                .setCanFly(canFly)
                .setSpecialPowerType(specialPowerType)
                .setSpell(specialPower)
                .build();
    }

    private static void setUpWriterAndScanner() {
        if (!hasArg) {
            scanner = new Scanner(System.in);
            try {
                System.out.println("Enter Log File Name:");
                String string = scanner.nextLine();
                writer = new BufferedWriter(new FileWriter("./gameData/ManualFeatureInputLogs/" + string + ".txt"));
                System.out.println("./gameData/ManualFeatureInputLogs/" + string + ".txt " + " FILE CREATED!");
            } catch (IOException io) {
                System.out.println("IO Exception : No such file name");
            }
        } else {
            try {
                scanner = new Scanner(new File(arg3));
            } catch (IOException io) {
                System.out.println("No Such File exists");
            }
        }
    }

    private static void closeWriter() {
        if (!hasArg) {
            try {
                writer.close();
            } catch (IOException io) {
                System.out.println("IO Exception while closing");
            }
        }
    }

    private static String getMultipleChoice(String message, String... args) {
        System.out.print(message + " ");
        String reg = "";
        for (String arg: args) {
            System.out.print(arg + " ");
            if(!reg.isEmpty())
                reg += "|";
            reg += arg;
        }
        String response = getInput();
        while (!response.matches(reg)) {
            System.out.print("Again! " + reg);
            response = getInput();
        }
        return response;

    }

    public static void main(String[] args) {

        // set up args and writer and scanner
        {
            hasArg = args.length >= 3;
            if (hasArg)
                arg3 = args[2];
            setUpWriterAndScanner();
        }

        System.out.println("-----------Add Collection Item Feature Console Section-----------");
        System.out.println("What do you want to enter? (SpellCard/UsableItem/Minion/Hero)");
        String response = getMultipleChoice("Whaat do you want to enter?", "Card", "Item");
        switch (response) {
            case "Card":
                Card card = addCard();
                SpellCard spellCard = addSpellCard();

                break;
            case "Item":
                Item item = addItem();
                saveItem(item)
                break;
        }


        // close the writer
        {
            closeWriter();
        }
    }


    private static void saveCollectionItem(CollectionItem collectionItem) {
        try {
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            FileWriter out;
            if (collectionItem instanceof Hero) {
                out = new FileWriter("./gameData/heros/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Hero.class));
            }
            if (collectionItem instanceof SpellCard) {
                out = new FileWriter("./gameData/spellCards/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, SpellCard.class));
            }
            if (collectionItem instanceof Minion) {
                out = new FileWriter("./gameData/minions/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Minion.class));
            }
            if (collectionItem instanceof Usable) {
                out = new FileWriter("./gameData/usableItems/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Usable.class));
            }
            if (collectionItem instanceof Collectible) {
                out = new FileWriter("./gameData/collectibles/" + collectionItem.getName() + ".txt", false);
                out.write(yaGson.toJson(collectionItem, Collectible.class));
            } else {
                throw new IOException();
            }
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Can't Read file for some reason: ");
            System.out.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }

    private static void saveHero(Hero hero) {
    }

    private static void saveSpellCard(SpellCard spellCard) {
    }

    private static void saveMinion(Minion minion) {
    }

    private static void saveUsable(Usable usable) {
    }

    private static void saveCard(Card card) {
        try {
            FileWriter out = new FileWriter("./gameData/cards/" + card.getName() + ".txt", false);
            YaGson yaGson = new YaGsonBuilder().setPrettyPrinting().create();
            out.write(yaGson.toJson(card, Card.class));
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Can't Read file for some reason: ");
            System.out.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }
}
