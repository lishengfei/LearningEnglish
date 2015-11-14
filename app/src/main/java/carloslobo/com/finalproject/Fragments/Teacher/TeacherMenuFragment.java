package carloslobo.com.finalproject.Fragments.Teacher;

import android.graphics.drawable.Drawable;
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

    private ImageView Groups,mBanner;
    private TextView TeacherName;

    public TeacherMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_teacher_menu, container, false);

        initViews(rootView);
        initListeners();

        return rootView;
    }


    @Override
    public void initViews(View rootview) {
        Groups = (ImageView) rootview.findViewById(R.id.groupButton);
        TeacherName = (TextView) rootview.findViewById(R.id.teacherName);
        TeacherName.setText("Bienvenido " + ParseUser.getCurrentUser().getUsername());

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
    public void onClick(View v) {
        int id = v.getId();

        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        switch (id){
            case R.id.groupButton:
                mTransaction.replace(R.id.main_container, new GroupListFragment(),"GroupList");
                mTransaction.addToBackStack(null);
                Log.d(TAG,"Groups will be listed");
                break;
        }

        mTransaction.commit();
    }

}
