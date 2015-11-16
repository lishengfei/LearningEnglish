package carloslobo.com.finalproject.Fragments.Student;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Modules.GameManager;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.Modules.Question;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LetterTestFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = LetterTestFragment.class.getName();

    //Layout
    private TextInputLayout Answer;
    private ImageView mImageView;
    private Button mButton;

    //Manager
    private GameManager GM;

    public LetterTestFragment() {   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_letter_test, container, false);

        GM = ((MainActivity) getActivity()).getGManager();

        initViews(rootView);
        initListeners();
        HideAppBarLayout();

        return rootView;
    }


    @Override
    public void initViews(View rootView) {
        Answer = (TextInputLayout) rootView.findViewById(R.id.studentAnswer);
        mButton = (Button) rootView.findViewById(R.id.saveTestButton);

        mImageView = (ImageView) rootView.findViewById(R.id.testImage);

        Question CurrentQuestion = GM.getQuestion(GM.getCurrentExercise());
        mImageView.setImageBitmap(CurrentQuestion.getImage());
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private boolean validateInput() {
        String answer = Answer.getEditText().getText().toString();

        if (answer.isEmpty()) {
            if (answer.isEmpty())
                Answer.setError("La respuesta no puede estar vacía");
            return false;
        }

        return true;
    }

    private void HideAppBarLayout(){
        AppBarLayout APL = (AppBarLayout) getActivity().findViewById(R.id.test);
        APL.setExpanded(false, true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.saveTestButton:
                if(validateInput()){
                    Log.d(TAG,"The student answered the question.");

                    GM.SaveAnswer(Answer.getEditText().getText().toString());

                    break;}
        }
    }


}
