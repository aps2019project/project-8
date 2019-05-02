package gen;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import model.*;

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

    public static void main(String[] args) {

        // set up args and writer and scanner
        {
            hasArg = args.length >= 3;
            if (hasArg)
                arg3 = args[2];
            setUpWriterAndScanner();
        }

        System.out.println("-----------Add Card Feature Console Section-----------");
        System.out.println("Here you can enter your own card:");

        String cardName;
        int price, manaCost;
        {
            System.out.print("\tEnter Card Name: ");
            cardName = getInput();
        }
        {
            System.out.print("\tEnter price: ");
            price = Integer.parseInt(getInput());
        }
        {
            System.out.print("\tEnter Mana Cost: ");
            manaCost = Integer.parseInt(getInput());
        }
        numberOfCards++;

        System.out.print ("What is your card type? (SpellCard, Unit) ");
        String cardType = getInput();
        while(!cardType.matches("(SpellCard|Unit)")) {
            System.out.print ("Enter again: (SpellCard, Unit) ");
            cardType = getInput();
        }

        Card myCard = null;
        switch (cardType) {
            case "SpellCard" :
                myCard = addSpellCard();
                break;
            case "Unit" :
                myCard = addUnit();
                break;
        }
        try {
            myCard.setManaCost(manaCost);
            myCard.setName(cardName);
            myCard.setPrice(price);
        } catch (NullPointerException except) {
            System.out.println("failed to get card");
        }
        if(hasArg) {
            saveCard(myCard);
        }

        // close the writer
        {
            closeWriter();
        }
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
