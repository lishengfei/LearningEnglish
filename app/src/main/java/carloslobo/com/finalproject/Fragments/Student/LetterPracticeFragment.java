package carloslobo.com.finalproject.Fragments.Student;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Modules.GameManager;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.Modules.Question;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LetterPracticeFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = LetterPracticeFragment.class.getName();

    private TextView mTextView;
    private ImageView mImageView;
    private RadioButton OptionA,OptionB,OptionC,OptionD;
    private Button mButton;

    private GameManager GM;

    public LetterPracticeFragment() {   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_letter_practice, container, false);

        GM = ((MainActivity) getActivity()).getgManager();

        initViews(rootview);
        initListeners();
        HideAppBarLayout();
        initGestures(rootview);

        return rootview;
    }


    @Override
    public void initViews(View rootview) {
        mTextView = (TextView) rootview.findViewById(R.id.chosenLetter);
        mImageView = (ImageView) rootview.findViewById(R.id.letterPreviewImage);
        OptionA = (RadioButton) rootview.findViewById(R.id.optionA);
        OptionB = (RadioButton) rootview.findViewById(R.id.optionB);
        OptionC = (RadioButton) rootview.findViewById(R.id.optionC);
        OptionD = (RadioButton) rootview.findViewById(R.id.optionD);
        mButton = (Button) rootview.findViewById(R.id.finishPracticeButton);

        Question CurrentQuestion = GM.getQuestion(GM.getCurrentExercise());
        mTextView.setText("Letra: " + GM.getLetter());
        mImageView.setImageBitmap(CurrentQuestion.getImage());
        OptionA.setText(CurrentQuestion.getOption(0));
        OptionB.setText(CurrentQuestion.getOption(1));
        OptionC.setText(CurrentQuestion.getOption(2));
        OptionD.setText(CurrentQuestion.getOption(3));
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private void HideAppBarLayout(){
        AppBarLayout v = (AppBarLayout) getActivity().findViewById(R.id.test);
        v.setExpanded(false, true);
    }

    private void initGestures(View rootView){
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        return super.onScroll(e1, e2, distanceX, distanceY);
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                        Log.i(TAG, "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        if(e1!=null && e2!=null) {
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i(TAG, "Right to Left");

                                if (GM.getCurrentExercise() < GM.getExercisesNumber())
                                    GM.FetchNewQuestion();
                                else
                                    GM.Terminate();
                            }
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.finishPracticeButton:

                Log.d(TAG,"Student chose an answer");

                if (GM.isCompleted())
                    GM.Finish();
                else
                    GM.FetchNewQuestion();

                break;
        }
    }

}
