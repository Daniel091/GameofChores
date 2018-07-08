package sbd.pemgami.Gambling;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import sbd.pemgami.R;
import sbd.pemgami.SharedPrefsUtils;
import sbd.pemgami.User;

public class GamblingFragment extends Fragment {
    private static final String TAG = "GamblingFragment";

    public GamblingFragment() {
        // Required empty public constructor
    }

    Button newGame;
    TextView gambleText;
    TextView gambleText2;
    TextView ruleheader;
    TextView rules;
    LottieAnimationView sadface;
    TextView nopoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gambling, container, false);
        newGame = view.findViewById(R.id.gamb_startGame);
        gambleText = view.findViewById(R.id.gamb_gambleText);
        gambleText2 = view.findViewById(R.id.gamb_gambleText2);
        ruleheader = view.findViewById(R.id.gamb_ruleheader);
        rules = view.findViewById(R.id.gamb_rulebook);
        sadface = view.findViewById(R.id.gamb_sadface);
        nopoints = view.findViewById(R.id.gamb_nopoints);

        //let it blink
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
        anim.setStartOffset(30);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        gambleText.startAnimation(anim);

        Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
        anim2.setDuration(30);
        anim2.setStartOffset(9);
        anim2.setRepeatMode(Animation.REVERSE);
        anim2.setRepeatCount(Animation.INFINITE);
        gambleText2.startAnimation(anim2);

        playerPointChecker();

        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle("Gambling");
        }

        return view;
    }

    private void playerPointChecker() {
        User user = SharedPrefsUtils.readLastUserFromSharedPref(getContext());

        if (user.getPoints() == 0) {
            newGame.setTextColor(Color.GRAY);

            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ruleheader.setVisibility(View.GONE);
                    rules.setVisibility(View.GONE);
                    sadface.setVisibility(View.VISIBLE);
                    sadface.animate();
                    nopoints.setVisibility(View.VISIBLE);
                }
            });

        } else {

            newGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: Button is clicked");
                    Intent intent = new Intent(getActivity(), GamblingActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        playerPointChecker();

    }
}
