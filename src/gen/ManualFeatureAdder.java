package gen;

import model.Buff;
import model.Card;
import model.Spell;
import model.SpellCard;

import java.util.Scanner;

public class ManualFeatureAdder {
    private static int numberOfCards = 0;
    private static Scanner scanner = new Scanner(System.in);

    private static Buff addBuff() {
        System.out.println("Enter Buff:");
        System.out.println("Enter duration of the buff");
        int duration = scanner.nextInt();
        System.out.println("Enter holy");
        int holy = scanner.nextInt();
        System.out.println("Enter power");
        int power = scanner.nextInt();
        System.out.println("Enter poison");
        int poison = scanner.nextInt();
        System.out.println("Enter weakness AP");
        int weaknessAP = scanner.nextInt();
        System.out.println("Enter weakness HP");
        int weaknessHP = scanner.nextInt();
        System.out.println("Enter unholy");
        int unholy = scanner.nextInt();
        System.out.println("Enter can stun (yes/no)");
        String feedBack = scanner.next();
        boolean stun = false;
        if (feedBack.equals("yes")) {
            stun = true;
        }
        System.out.println("Enter can disarm (yes/no)");
        feedBack = scanner.next();
        boolean disarm = false;
        if (feedBack.equals("yes")) {
            disarm = true;
        }
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

    private static SpellCard addSpellCard() {
        System.out.println("Here You should enter spell:");
        return new SpellCard();
    }

    public static void main(String[] args) {
        System.out.println("-----------Add Card Feature Console Section-----------");
        System.out.println("Here you can enter your own card:");
        System.out.print("\tEnter Card Name: ");
        String cardName = scanner.next();
        System.out.print("\tEnter price: ");
        int price = scanner.nextInt();
        System.out.print("\tEnter Mana Cost: ");
        int manaCost = scanner.nextInt();
        numberOfCards++;
        Card myCard = new Card(price, numberOfCards, cardName, manaCost);
        System.out.println("Card created!");
        System.out.print ("What is your card type? (SpellCard, Unit) ");
        String cardType = scanner.next();
        while(!cardType.matches("(SpellCard|Unit)")) {
            System.out.print ("Enter again: (SpellCard, Unit) ");
            cardType = scanner.next();
        }

        switch (cardType) {
            case "SpellCard" :
                SpellCard spellCard = addSpellCard(myCard);
                break;
            case "Unit" :
                break;
        }
    }
}
