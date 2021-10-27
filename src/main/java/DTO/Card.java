package DTO;

import enums.Suit;

public class Card {
    private String nominal;
    private Suit suit;
    private final int number;

    public Card(int number){
        number -= 2;
        this.number = number;
        int n = number % 13 + 2;
        switch (n){
            case 11 -> nominal = "V";
            case 12 -> nominal = "Q";
            case 13 -> nominal = "K";
            case 14 -> nominal = "T";
            default -> nominal = Integer.toString(n);
        }
        switch (number / 13) {
            case 0 -> suit = Suit.Bubi;
            case 1 -> suit = Suit.Chervi;
            case 2 -> suit = Suit.Kresty;
            case 3 -> suit = Suit.Picky;
            case 4 -> {
                nominal = "Joker";
                if (number == 53)
                    suit = Suit.Picky;
                else
                    suit = Suit.Chervi;
            }
        }
    }

    public long getNumber() {
        return number;
    }

    public String getNominal() {
        return nominal;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return nominal + " " + suit.name();
    }
}
