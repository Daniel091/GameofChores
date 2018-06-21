package sbd.pemgami.Gambling;

import java.util.List;

/**
 * This class works as an active rule book for the gambling mechanism.
 */
class Referee {

    /**
     * @param notSelected
     * @param selectedCard
     * @return 0 = lose, 1 = win, 666 = jackpot
     */
    public static int checkCards(PlayingCard notSelected1, PlayingCard notSelected2, PlayingCard selectedCard) {
        int selected = selectedCard.getValue();
        int not1 = notSelected1.getValue();
        int not2 = notSelected2.getValue();

        //jackpot
        if(selected == not1 && selected == not2){
            return 666;
        }

        //highest card
        if(selected > not1 && selected > not2 && not1 != not2){
            return 1;
        }

        //lowest card high pair
        if(selected < not1 && selected < not2 && not1 == not2){
            return 1;
        }


        return 0;
    }

}
