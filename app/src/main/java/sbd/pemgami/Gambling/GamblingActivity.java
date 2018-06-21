package sbd.pemgami.Gambling;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.Duration;

import sbd.pemgami.R;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.TRANSPARENT;

public class GamblingActivity extends AppCompatActivity {

    private static final String TAG = "GamblingActivity";

    ImageButton card1;
    ImageButton card2;
    ImageButton card3;

    List<PlayingCard> drawnCards;

    PlayingCard selectedCard;
    PlayingCard notSelected1;
    PlayingCard notSelected2;

    boolean cFlag1 = false;
    boolean cFlag2 = false;
    boolean cFlag3 = false;

    boolean selectable1 = true;
    boolean selectable2 = true;
    boolean selectable3 = true;

    Button submitBet;
    Button buySwitch;
    Button stay;
    Button nextRound;

    TextView description;
    TextView betAmountDisplay;

    ScrollableNumberPicker numberPicker;

    int betAmount = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambling);

        card1 = findViewById(R.id.gambl_card1);
        card2 = findViewById(R.id.gambl_card2);
        card3 = findViewById(R.id.gambl_card3);

        submitBet = findViewById(R.id.gambl_submit);
        buySwitch = findViewById(R.id.gambl_buySwitch);
        stay = findViewById(R.id.gambl_stay);
        nextRound = findViewById(R.id.gambl_nextRound);

        description = findViewById(R.id.gambl_descriptionText);
        betAmountDisplay = findViewById(R.id.gambl_betamount);

        numberPicker = findViewById(R.id.gambl_scrollableNumberPicker);

        drawnCards = drawCardsFromDeck();

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectable1) {
                    card1.setImageResource(R.drawable.card_selectedback);
                    cFlag1 = true;

                    if (cFlag2 && selectable2) {
                        card2.setImageResource(R.drawable.card_back);
                        cFlag2 = false;
                    }

                    if (cFlag3 && selectable3) {
                        card3.setImageResource(R.drawable.card_back);
                        cFlag3 = false;
                    }
                }
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectable2) {
                    card2.setImageResource(R.drawable.card_selectedback);
                    cFlag2 = true;

                    if (cFlag1 && selectable1) {
                        card1.setImageResource(R.drawable.card_back);
                        cFlag1 = false;
                    }

                    if (cFlag3 && selectable3) {
                        card3.setImageResource(R.drawable.card_back);
                        cFlag3 = false;
                    }
                }
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectable3) {
                    card3.setImageResource(R.drawable.card_selectedback);
                    cFlag3 = true;

                    if (cFlag1 && selectable1) {
                        card1.setImageResource(R.drawable.card_back);
                        cFlag1 = false;
                    }

                    if (cFlag2 && selectable2) {
                        card2.setImageResource(R.drawable.card_back);
                        cFlag2 = false;
                    }
                }
            }
        });

        submitBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cFlag1) {
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                    selectable1 = false;

                    selectedCard = drawnCards.get(0);
                    notSelected1 = drawnCards.get(1);
                    notSelected2 = drawnCards.get(2);

                    selectedCard.setSelected(true);
                }

                if (cFlag2) {
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    selectable2 = false;

                    selectedCard = drawnCards.get(1);
                    notSelected1 = drawnCards.get(0);
                    notSelected2 = drawnCards.get(2);

                    selectedCard.setSelected(true);
                }

                if (cFlag3) {
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                    selectable3 = false;

                    selectedCard = drawnCards.get(2);
                    notSelected1 = drawnCards.get(0);
                    notSelected2 = drawnCards.get(1);

                    selectedCard.setSelected(true);
                }

                if (cFlag1 || cFlag2 || cFlag3) {
                    betAmount = numberPicker.getValue();
                    betAmountDisplay.setText(String.valueOf(betAmount));
                    numberPicker.setVisibility(View.GONE);
                    betAmountDisplay.setVisibility(View.VISIBLE);
                    submitBet.setVisibility(View.GONE);
                    stay.setVisibility(View.VISIBLE);
                    buySwitch.setVisibility(View.VISIBLE);
                    description.setText(R.string.gambl_stayOrSwitch);
                }
            }
        });

        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectable1) {
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                }

                if (!selectable2) {
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                }

                if (!selectable3) {
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                }

                winOrLose();
                buySwitch.setVisibility(View.GONE);
                stay.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }
        });

        buySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: fix bug
                if (!selectable1) {
                    if (cFlag2) {
                        selectedCard = drawnCards.get(1);
                        notSelected1 = drawnCards.get(0);
                        notSelected2 = drawnCards.get(2);
                    }
                    if (cFlag3) {
                        selectedCard = drawnCards.get(2);
                        notSelected1 = drawnCards.get(0);
                        notSelected2 = drawnCards.get(1);
                    }
                }

                if (!selectable2) {

                    if (cFlag1) {
                        selectedCard = drawnCards.get(0);
                        notSelected1 = drawnCards.get(1);
                        notSelected2 = drawnCards.get(2);
                    }
                    if (cFlag3) {
                        selectedCard = drawnCards.get(2);
                        notSelected1 = drawnCards.get(0);
                        notSelected2 = drawnCards.get(1);
                    }
                }

                if (!selectable3) {
                    if (cFlag1) {
                        selectedCard = drawnCards.get(0);
                        notSelected1 = drawnCards.get(1);
                        notSelected2 = drawnCards.get(2);
                    }
                    if (cFlag2) {
                        selectedCard = drawnCards.get(1);
                        notSelected1 = drawnCards.get(0);
                        notSelected2 = drawnCards.get(2);
                    }
                }
                if (!selectedCard.isSelected()) {
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                    winOrLose();
                    buySwitch.setVisibility(View.GONE);
                    stay.setVisibility(View.GONE);
                    betAmount = betAmount * 2;
                    betAmountDisplay.setText(String.valueOf(betAmount));
                    description.setVisibility(View.GONE);
                }
            }
        });

        nextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRound.setVisibility(View.GONE);
                GamblingActivity.this.recreate();
            }
        });
    }

    private void winOrLose() {
        betAmountDisplay.setTextSize(40);

        //gets rid of card selection bug after all cards are face up
        selectable1 = false;
        selectable2 = false;
        selectable3 = false;

        //0 = loose, 1 = win, 666 = jackpot
        int win = Referee.checkCards(notSelected1, notSelected2, selectedCard);

        switch (win) {
            case 1:
                Toast.makeText(getBaseContext(), "Well done champ!! Win some more?", Toast.LENGTH_LONG).show();
                break;
            case 666:
                Toast.makeText(getBaseContext(), "JACKPOT BABY!!!!!", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getBaseContext(), "YOU LOSE!!! NOBODY LOVES YOU!!! Try again :)", Toast.LENGTH_LONG).show();
                betAmountDisplay.setTextColor(Color.RED);

        }
        nextRound.setVisibility(View.VISIBLE);


    }


    private List<PlayingCard> drawCardsFromDeck() {
        List<PlayingCard> deck = PlayingCard.getCardDeck();
        Collections.shuffle(deck);
        List<PlayingCard> cards = new ArrayList<>();

        cards.add(deck.get(0));
        cards.add(deck.get(15));
        cards.add(deck.get(30));

        Log.d(TAG, "drawCardsFromDeck: A new deck is born");

        return cards;
    }
}
