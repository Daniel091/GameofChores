package sbd.pemgami.Gambling;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.Duration;

import sbd.pemgami.Constants;
import sbd.pemgami.R;
import sbd.pemgami.SharedPrefsUtils;
import sbd.pemgami.User;
import sbd.pemgami.WG;

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
    TextView description2;
    TextView betAmountDisplay;

    ScrollableNumberPicker numberPicker;

    ImageView arrow1;
    ImageView arrow2;
    ImageView arrow3;

    TextView jackpotDisplay;
    TextView jackpotDisplay2;
    TextView playerPointsDisplay;

    TextView jackpotTitle;

    int jackpot;
    int playersMoney;

    int betAmount = 100;

    User user;
    WG wg;

    DatabaseReference wgJackpotRef;
    DatabaseReference userPointsRef;

    LottieAnimationView winAnim;
    LottieAnimationView loseAnim;
    LottieAnimationView fireworksAnim;
    LottieAnimationView jackpotAnim;
    LottieAnimationView star1;
    LottieAnimationView star2;
    LottieAnimationView star3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gambling);


        //get user and wg
        user = SharedPrefsUtils.readLastUserFromSharedPref(getBaseContext());
        wg = SharedPrefsUtils.readLastWGFromSharedPref(getBaseContext());
        jackpot = wg.getJackpot();
        playersMoney = user.getPoints();

        //in case someone has lost all of the money
        if(playersMoney == 0){
            finish();
        }


        //setup of all view elements

        winAnim = findViewById(R.id.gambl_winAnim);
        loseAnim = findViewById(R.id.gambl_loseAnim);
        fireworksAnim = findViewById(R.id.gambl_fireworksAnim);
        jackpotAnim = findViewById(R.id.gambl_jackpotAnim);
        star1 = findViewById(R.id.gambl_star1);
        star2 = findViewById(R.id.gambl_star2);
        star3 = findViewById(R.id.gambl_star3);

        jackpotTitle = findViewById(R.id.gambl_jackpotTitle1);

        card1 = findViewById(R.id.gambl_card1);
        card2 = findViewById(R.id.gambl_card2);
        card3 = findViewById(R.id.gambl_card3);

        arrow1 = findViewById(R.id.gambl_arrow1);
        arrow2 = findViewById(R.id.gambl_arrow2);
        arrow3 = findViewById(R.id.gambl_arrow3);

        jackpotDisplay = findViewById(R.id.gambl_wgJackpot);
        jackpotDisplay.setText(String.valueOf(jackpot));
        jackpotDisplay2 = findViewById(R.id.gambl_wgJackpot2);


        submitBet = findViewById(R.id.gambl_submit);
        buySwitch = findViewById(R.id.gambl_buySwitch);
        stay = findViewById(R.id.gambl_stay);
        nextRound = findViewById(R.id.gambl_nextRound);

        description = findViewById(R.id.gambl_descriptionText);
        description2 = findViewById(R.id.gambl_descriptionText2);
        betAmountDisplay = findViewById(R.id.gambl_betamount);

        playerPointsDisplay = findViewById(R.id.gambl_playersPoints);
        playerPointsDisplay.setText(String.format(getString(R.string.gambl_playerpoints), playersMoney));

        numberPicker = findViewById(R.id.gambl_scrollableNumberPicker);
        numberPicker.setMaxValue(playersMoney);
        numberPicker.setListener(new ScrollableNumberPickerListener() {
            @Override
            public void onNumberPicked(int value) {
                if(value > playersMoney){
                    numberPicker.setValue(playersMoney);
                }
                if(value < 1){
                    numberPicker.setValue(1);
                }
            }
        });


        drawnCards = drawCardsFromDeck();

        //firebase stuff
        wgJackpotRef = FirebaseDatabase.getInstance().getReference().child("wgs").child(wg.getUid()).child("jackpot");
        userPointsRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("points");

        wgJackpotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jackpotDisplay.setText(String.valueOf(jackpot));
                jackpotDisplay2.setText(String.valueOf(jackpot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }
        });

        userPointsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playerPointsDisplay.setText(String.format(getString(R.string.gambl_playerpoints), playersMoney));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectable1) {
                    card1.setImageResource(R.drawable.card_selectedback);
                    cFlag1 = true;
                    arrow1.setVisibility(View.VISIBLE);
                    arrow2.setVisibility(View.GONE);
                    arrow3.setVisibility(View.GONE);

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
                    arrow1.setVisibility(View.GONE);
                    arrow2.setVisibility(View.VISIBLE);
                    arrow3.setVisibility(View.GONE);

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
                    arrow1.setVisibility(View.GONE);
                    arrow2.setVisibility(View.GONE);
                    arrow3.setVisibility(View.VISIBLE);

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

                    Log.d(TAG, "onClick: submit bet: " + betAmount);
                    playersMoney = playersMoney - betAmount;
                    user.setPoints(playersMoney);
                    userPointsRef.setValue(playersMoney);
                    writeWGAndUserToSharedPref();

                    betAmountDisplay.setText(String.valueOf(betAmount));
                    numberPicker.setVisibility(View.GONE);
                    betAmountDisplay.setVisibility(View.VISIBLE);
                    submitBet.setVisibility(View.GONE);
                    stay.setVisibility(View.VISIBLE);

                    if(playersMoney >= betAmount ){
                        buySwitch.setVisibility(View.VISIBLE);
                        description.setText(R.string.gambl_stayOrSwitch);
                    } else {
                        stay.performClick();
                    }

                }
            }
        });

        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectable1) {
                    arrow1.setVisibility(View.VISIBLE);
                    arrow2.setVisibility(View.GONE);
                    arrow3.setVisibility(View.GONE);
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                }

                if (!selectable2) {
                    arrow1.setVisibility(View.GONE);
                    arrow2.setVisibility(View.VISIBLE);
                    arrow3.setVisibility(View.GONE);
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                    card3.setImageResource(getResources().getIdentifier(drawnCards.get(2).getFileName(), "drawable", getPackageName()));
                }

                if (!selectable3) {
                    arrow1.setVisibility(View.GONE);
                    arrow2.setVisibility(View.GONE);
                    arrow3.setVisibility(View.VISIBLE);
                    card2.setImageResource(getResources().getIdentifier(drawnCards.get(1).getFileName(), "drawable", getPackageName()));
                    card1.setImageResource(getResources().getIdentifier(drawnCards.get(0).getFileName(), "drawable", getPackageName()));
                }

                winOrLose();
                buySwitch.setVisibility(View.GONE);
                stay.setVisibility(View.GONE);
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
                    buySwitch.setVisibility(View.GONE);
                    stay.setVisibility(View.GONE);

                    playersMoney = playersMoney - betAmount;
                    userPointsRef.setValue(playersMoney);
                    writeWGAndUserToSharedPref();

                    betAmount = betAmount * 2;

                    Log.d(TAG, "onClick: double betamount:" + betAmount);
                    Log.d(TAG, "onClick: double playersmoney" + playersMoney);

                    winOrLose();

                    betAmountDisplay.setText(String.valueOf(betAmount));

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

        Log.d(TAG, "winOrLose: betamount: " + betAmount );

        //gets rid of card selection bug after all cards are face up
        selectable1 = false;
        selectable2 = false;
        selectable3 = false;

        //0 = loose, 1 = win, 666 = jackpot
        int win = Referee.checkCards(notSelected1, notSelected2, selectedCard);
        description.setTextSize(40);

        switch (win) {
            case 1:
                Toast.makeText(getBaseContext(), "Well done champ!! Win some more?", Toast.LENGTH_LONG).show();
                playersMoney = playersMoney + betAmount * 2;
                user.setPoints(playersMoney);
                userPointsRef.setValue(playersMoney);
                winAnim.setVisibility(View.VISIBLE);
                fireworksAnim.setVisibility(View.VISIBLE);
                description.setText(R.string.winner_text_description);
                break;
            case 666:
                description.setText(R.string.jackpot_text_description);
                description2.setText(R.string.jackpot_text_description);
                Toast.makeText(getBaseContext(), "JACKPOT BABY!!!!!", Toast.LENGTH_LONG).show();
                playersMoney = playersMoney + betAmount + jackpot;
                user.setPoints(playersMoney);
                userPointsRef.setValue(playersMoney);
                wg.setJackpot(1000);
                wgJackpotRef.setValue(1000);

                playJackpotAnimations();

                writeWGAndUserToSharedPref();
                break;
            default:
                description.setText(R.string.loser_text_description);
                Toast.makeText(getBaseContext(), "YOU LOSE!!! NOBODY LOVES YOU!!! Try again :)", Toast.LENGTH_LONG).show();
                betAmountDisplay.setTextColor(Color.RED);
                user.setPoints(playersMoney);
                user.setPoints(playersMoney);
                jackpot = jackpot + betAmount;
                wg.setJackpot(jackpot);
                wgJackpotRef.setValue(jackpot);
                loseAnim.setVisibility(View.VISIBLE);


                if (playersMoney == 0){
                    nextRound.setText(R.string.gambl_quit);
                }

        }
        writeWGAndUserToSharedPref();
        nextRound.setVisibility(View.VISIBLE);


    }

    private void playJackpotAnimations() {
        jackpotAnim.setVisibility(View.VISIBLE);
        fireworksAnim.setVisibility(View.VISIBLE);
        star1.setVisibility(View.VISIBLE);
        star2.setVisibility(View.VISIBLE);
        star3.setVisibility(View.VISIBLE);
        description2.setVisibility(View.VISIBLE);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50);
        anim.setStartOffset(30);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        jackpotDisplay.startAnimation(anim);

        Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
        anim2.setDuration(50);
        anim2.setStartOffset(15);
        anim2.setRepeatMode(Animation.REVERSE);
        anim2.setRepeatCount(Animation.INFINITE);
        jackpotTitle.startAnimation(anim2);

        Animation anim3 = new AlphaAnimation(0.0f, 1.0f);
        anim3.setDuration(50);
        anim3.setStartOffset(10);
        anim3.setRepeatMode(Animation.REVERSE);
        anim3.setRepeatCount(Animation.INFINITE);
        description.startAnimation(anim3);
    }

    private void writeWGAndUserToSharedPref(){
        SharedPrefsUtils.writeUserToSharedPrefJava(getBaseContext(), user);
        SharedPrefsUtils.writeWGToSharedPrefJava(getBaseContext(), wg);
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
