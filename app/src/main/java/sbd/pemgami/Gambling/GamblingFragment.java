package sbd.pemgami.Gambling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sbd.pemgami.R;
import sbd.pemgami.SharedPrefsUtils;

public class GamblingFragment extends Fragment {
    private static final String TAG = "GamblingFragment";

    public GamblingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gambling, container, false);
        Button newGame = view.findViewById(R.id.gamb_startGame);


        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Button is clicked");
                Intent intent = new Intent(getActivity(), GamblingActivity.class);
                startActivity(intent);
            }
        });

        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle("Gambling");
        }

        return view;
    }
}
