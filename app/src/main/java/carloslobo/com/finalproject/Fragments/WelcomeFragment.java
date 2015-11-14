package carloslobo.com.finalproject.Fragments;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import carloslobo.com.finalproject.Fragments.Student.StudentLoginFragment;
import carloslobo.com.finalproject.Fragments.Teacher.TeacherLoginFragment;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;



public class WelcomeFragment extends Fragment implements Init, View.OnClickListener {

    private final static String TAG = WelcomeFragment.class.getName();

    private RadioButton Student,Teacher;

    public WelcomeFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Student.setChecked(false);
        Teacher.setChecked(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_welcome, container, false);

        initViews(rootView);
        initListeners();

        return rootView;
    }


    @Override
    public void initViews(View rootview) {
        Student = (RadioButton) rootview.findViewById(R.id.studentRadio);
        Teacher = (RadioButton) rootview.findViewById(R.id.teacherRadio);
    }

    @Override
    public void initListeners() {
        Student.setOnClickListener(this);
        Teacher.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        switch (id){
            case R.id.studentRadio:
                mTransaction.replace(R.id.main_container, new StudentLoginFragment(), "StudentLogin");
                mTransaction.addToBackStack("StudentLogin");
                Log.d(TAG,"A Student will log in.");
                break;

            case R.id.teacherRadio:
                Log.d(TAG, "A Teacher will log in.");
                mTransaction.replace(R.id.main_container, new TeacherLoginFragment(),"TeacherLogin");
                mTransaction.addToBackStack(null);
                break;
        }

        mTransaction.commit();

    }
}
