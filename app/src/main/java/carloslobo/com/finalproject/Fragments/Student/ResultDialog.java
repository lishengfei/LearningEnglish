package carloslobo.com.finalproject.Fragments.Student;


import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ResultDialog extends DialogFragment implements Init, View.OnClickListener{

    //Layout
    private TextView mGood, mWrong, mScore;
    private Button mButton;

    //Variables
    private String Good, Wrong, Score;

    public ResultDialog() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_dialog, container, false);

        Good = getArguments().getString("Good");
        Wrong = getArguments().getString("Wrong");
        Score = getArguments().getString("Score");

        initViews(rootView);
        initListeners();

        return rootView;
    }


    @Override
    public void initViews(View rootView) {
        mButton = (Button) rootView.findViewById(R.id.OkButton);

        mGood = (TextView) rootView.findViewById(R.id.Good);
        mWrong = (TextView) rootView.findViewById(R.id.Wrong);
        mScore = (TextView) rootView.findViewById(R.id.Score);

        mGood.setText(Good);
        mWrong.setText(Wrong);
        mScore.setText(Score);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
