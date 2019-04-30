package gen;

import model.Card;

import java.util.Scanner;

public class ManualFeatureAdder {
    private static int numberOfCards = 0;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
                break;
            case "Unit" :
                break;
        }
    }
}
