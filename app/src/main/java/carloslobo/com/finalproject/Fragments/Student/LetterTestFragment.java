package carloslobo.com.finalproject.Fragments.Student;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
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

import org.w3c.dom.Text;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LetterTestFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = LetterTestFragment.class.getName();

    private TextInputLayout Answer;
    private Button mButton;

    public LetterTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_letter_test, container, false);

        initViews(rootView);
        initListeners();
        HideAppBarLayout();
        initGestures(rootView);

        return rootView;
    }


    @Override
    public void initViews(View rootview) {
        Answer = (TextInputLayout) rootview.findViewById(R.id.studentAnswer);
        mButton = (Button) rootview.findViewById(R.id.saveTestButton);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private void validateInput() {
        String answer = Answer.getEditText().getText().toString();

        if (answer.isEmpty()) {
            if (answer.isEmpty())
                Answer.setError("La respuesta no puede estar vacía");
            return;
        }
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

                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i(TAG, "Right to Left");
                            Transaction();
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
            case R.id.saveTestButton:
                validateInput();
                getActivity().onBackPressed();

                Log.d(TAG,"Student answered the question.");
                break;
        }
    }

    private void Transaction(){
        /*FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        mTransaction.replace(R.id.main_container, new LetterTestFragment(),"Test");
        mTransaction.addToBackStack("Test");
        mTransaction.commit();

        ((MainActivity)getActivity()).FragmentCount +=1;*/


        int i = ((MainActivity)getActivity()).FragmentCount;
        FragmentManager FM = getActivity().getSupportFragmentManager();

        //Add the new fragment
        FragmentTransaction mTransaction = FM.beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        mTransaction.replace(R.id.main_container, new LetterTestFragment(), "Test " + i);
        mTransaction.commit();
        Log.d(TAG, "Fragment Test " + i + " in");


        //Delete every old fragment
        FragmentTransaction nTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        nTransaction.remove(FM.findFragmentByTag( "Test " + (i-1) ));
        nTransaction.commit();
        FM.executePendingTransactions();
        Log.d(TAG, "Fragment Test " + (i-1) + " out");

        ((MainActivity) getActivity()).FragmentCount +=1;

    }
}
