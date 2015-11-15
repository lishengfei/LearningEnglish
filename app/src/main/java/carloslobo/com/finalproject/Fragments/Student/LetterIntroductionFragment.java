package carloslobo.com.finalproject.Fragments.Student;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Modules.GameManager;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.Modules.Question;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LetterIntroductionFragment extends Fragment implements Init,View.OnClickListener {

    private final static String TAG = LetterIntroductionFragment.class.getName();

    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    private GameManager GM;

    public LetterIntroductionFragment() {   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_letter_introduction, container, false);

        GM = ((MainActivity) getActivity()).getgManager();

        initViews(rootView);
        initListeners();
        initGestures(rootView);
        HideAppBarLayout();

        return rootView;
    }


    @Override
    public void initViews(View rootview) {
        mImageView = (ImageView) rootview.findViewById(R.id.letterPreviewImage);
        mTextView = (TextView) rootview.findViewById(R.id.WordExample);
        mButton = (Button) rootview.findViewById(R.id.letterIntroductionButton);

        Question CurrentQuestion = GM.getQuestion(GM.getCurrentExercise());
        mImageView.setImageBitmap(CurrentQuestion.getImage());
        mTextView.setText(CurrentQuestion.getAnswer());
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private void HideAppBarLayout() {
        AppBarLayout v = (AppBarLayout) getActivity().findViewById(R.id.test);
        v.setExpanded(false, true);
    }

    private void initGestures(View rootView) {
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

                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i(TAG, "Right to Left");

                            if (GM.getCurrentExercise() < GM.getExercisesNumber())
                                GM.FetchNewQuestion();
                            else
                                GM.Terminate();

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

        switch (id) {
            case R.id.letterIntroductionButton:
                Log.d(TAG, "The student understood the exercise");

                if (GM.isCompleted())
                    GM.Finish();
                else
                    GM.FetchNewQuestion();
                break;
        }
    }


}