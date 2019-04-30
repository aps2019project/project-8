package gen;

import com.gilecode.yagson.YaGson;
import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Scanner;

// Incomplete!

public class ManualFeatureAdder {
    private static int numberOfCards = 0;
    private static Scanner scanner = new Scanner(System.in);

    private static Buff addBuff() {
        System.out.println("***Enter Buff***");
        int duration, holy, power, poison, weaknessAP, weaknessHP, unholy;
        boolean stun, disarm;

        {
            System.out.println("Enter duration of the buff");
            duration = scanner.nextInt();
        }
        {
            System.out.println("Enter holy");
            holy = scanner.nextInt();
        }
        {
            System.out.println("Enter power");
            power = scanner.nextInt();
        }
        {
            System.out.println("Enter poison");
            poison = scanner.nextInt();
        }
        {
            System.out.println("Enter weakness AP");
            weaknessAP = scanner.nextInt();
        }
        {
            System.out.println("Enter weakness HP");
            weaknessHP = scanner.nextInt();
        }
        {
            System.out.println("Enter unholy");
            unholy = scanner.nextInt();
        }
        {
            System.out.println("Enter can stun (yes/no)");
            String feedBack = scanner.next();
            stun = feedBack.equals("yes");
        }
        {
            System.out.println("Enter can disarm (yes/no)");
            String feedBack = scanner.next();
            disarm = feedBack.equals("yes");
        }

        System.out.println("Add extra features to buff \"none\" to end!");
        String command = scanner.nextLine();
        while (!command.matches("none")) {
            command = scanner.nextLine();
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
            int selectedIndex = scanner.nextInt() - 1;
            spellTarget = spellTargets.get(selectedIndex);
        }
        {
            buff = addBuff();
        }

        System.out.println("Add extra features to spell \"none\" to end!");
        String command = scanner.nextLine();
        while (!command.matches("none")) {
            command = scanner.nextLine();
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
        return new Unit();
    }

    public static void main(String[] args) {
        System.out.println("-----------Add Card Feature Console Section-----------");
        System.out.println("Here you can enter your own card:");

        String cardName;
        int price, manaCost;
        {
            System.out.print("\tEnter Card Name: ");
            cardName = scanner.next();
        }
        {
            System.out.print("\tEnter price: ");
            price = scanner.nextInt();
        }
        {
            System.out.print("\tEnter Mana Cost: ");
            manaCost = scanner.nextInt();
        }
        numberOfCards++;

        System.out.print ("What is your card type? (SpellCard, Unit) ");
        String cardType = scanner.next();
        while(!cardType.matches("(SpellCard|Unit)")) {
            System.out.print ("Enter again: (SpellCard, Unit) ");
            cardType = scanner.next();
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
        saveCard(myCard);
    }


    private static void saveCard(Card card) {
        try {
            FileWriter out = new FileWriter("./gameData/cards/" + card.getName() + ".txt", false);
            YaGson yaGson = new YaGson();
            out.write(yaGson.toJson(card, Card.class));
            out.flush();
        } catch (IOException ignored) {
            System.out.println("Can't Read file for some reason: ");
            System.out.println("File can't be created / File can't be opened / A directory rather than file");
        }
    }
}
