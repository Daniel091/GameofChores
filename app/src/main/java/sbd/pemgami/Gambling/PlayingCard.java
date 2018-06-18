package sbd.pemgami.Gambling;

import java.util.ArrayList;
import java.util.List;

public class PlayingCard {

    public enum Rank{
        DEUCE(1), THREE(2), FOUR(3), FIVE(4), SIX(5), SEVEN(6), EIGHT(7), NINE(8), TEN(9),
        JACK(10), QUEEN(11), KING(12), ACE(13);

        int value;

        Rank(int value) {
            this.value = value;
        }

        public int getNumericValue() {
            return value;
        }

    }


    public enum Suit{
        HEARTS, DIAMONDS, CLUBS, SPADES;

    }
    private final Rank rank;

    private boolean selected = false;

    private final Suit suit;
    private final String fileName;
    private static final List<PlayingCard> cardDeck = new ArrayList<>();
    private PlayingCard(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        this.fileName = "card_" + getRankString(rank) + getSuitString(suit);

    }

    private String getSuitString(Suit suit) {
        switch(suit){
            case HEARTS:
                return "h";
            case DIAMONDS:
                return "d";
            case CLUBS:
                return "c";
            case SPADES:
                return "s";
        }

        return "E";
    }

    private String getRankString(Rank rank) {

        switch (rank){
            case DEUCE:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case JACK:
                return "j";
            case QUEEN:
                return "q";
            case KING:
                return "k";
            case ACE:
                return "a";
        }

        return "E";
    }

    static {
        for (Suit suit : Suit.values()
             ) {
            for (Rank rank : Rank.values()
                 ) {
                cardDeck.add(new PlayingCard(rank, suit));
            }
        }
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue(){
        return rank.getNumericValue();
    }

    public static List<PlayingCard> getCardDeck() {
        return new ArrayList<PlayingCard>(cardDeck);
    }

    public String getFileName() {
        return fileName.equals("card_EE") ? "card_back" : fileName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
