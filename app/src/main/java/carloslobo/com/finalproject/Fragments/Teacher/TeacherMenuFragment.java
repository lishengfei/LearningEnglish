package carloslobo.com.finalproject.Fragments.Teacher;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherMenuFragment extends Fragment implements Init, View.OnClickListener{

    private final static String TAG = TeacherMenuFragment.class.getName();

    //Layout
    private ImageView Groups,mBanner;
    private TextView TeacherName;

    public TeacherMenuFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_teacher_menu, container, false);

        initViews(rootView);
        initListeners();

        return rootView;
    }


    @Override
    public void initViews(View rootView) {
        Groups = (ImageView) rootView.findViewById(R.id.groupButton);
        TeacherName = (TextView) rootView.findViewById(R.id.teacherName);
        TeacherName.setText("Bienvenido " + ParseUser.getCurrentUser().getUsername());

        AppBarLayout APL = (AppBarLayout) getActivity().findViewById(R.id.test);
        APL.setExpanded(false, false);

        mBanner = (ImageView) getActivity().findViewById(R.id.collapsable_image);
        Drawable res = ContextCompat.getDrawable(getActivity(), R.drawable.turqouise7);
        mBanner.setImageDrawable(res);


        getActivity().setTitle("BrainSmart");
    }

    @Override
    public void initListeners() {
        Groups.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.groupButton:
                FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                mTransaction.replace(R.id.main_container, new GroupListFragment(), "GroupList");
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                Log.d(TAG,"Teacher groups  will be displayed.");
            break;
        }


    }

}
