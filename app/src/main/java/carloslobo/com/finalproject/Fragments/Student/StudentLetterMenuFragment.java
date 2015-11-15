package carloslobo.com.finalproject.Fragments.Student;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Fragments.Teacher.TeacherMenuFragment;
import carloslobo.com.finalproject.Modules.Interfaces.Async;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLetterMenuFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = StudentLetterMenuFragment.class.getName();

    private ImageButton Introduction,Practice,Test;

    private FloatingActionButton JoinGroup;
    private TextView mUser;
    private ImageView mBanner;
    private boolean NoGroup = false;

    public StudentLetterMenuFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_letter_menu, container, false);

        initViews(rootView);
        initListeners();
        ShowAppBarLayout();

        new GetData().execute();

        return rootView;
    }


    @Override
    public void initViews(View rootView) {
        Introduction= (ImageButton) rootView.findViewById(R.id.introductionButton);
        Practice = (ImageButton) rootView.findViewById(R.id.practiceButton);
        Test = (ImageButton) rootView.findViewById(R.id.testButton);
        JoinGroup = (FloatingActionButton) rootView.findViewById(R.id.joinGroupButton);
        mUser = (TextView) rootView.findViewById(R.id.userName);
        mUser.setText("Bienvenido " + ParseUser.getCurrentUser().getUsername());

        mBanner = (ImageView) getActivity().findViewById(R.id.collapsable_image);
        Drawable res = ContextCompat.getDrawable(getActivity(), R.drawable.turqouise4);
        mBanner.setImageDrawable(res);
        getActivity().setTitle("BrainSmart");
    }

    @Override
    public void initListeners() {
        Introduction.setOnClickListener(this);
        Practice.setOnClickListener(this);
        Test.setOnClickListener(this);
        JoinGroup.setOnClickListener(this);
    }

    private void ShowAppBarLayout(){
        AppBarLayout v = (AppBarLayout) getActivity().findViewById(R.id.test);
        v.setExpanded(true, true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        MainActivity Activity = ((MainActivity) getActivity());

        switch (id){
            case R.id.introductionButton:
                if(!NoGroup) {
                     Activity.createGameManager();
                    try {   Activity.getgManager().setWorkingLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getgManager().StartMode("Introduction");
                }
                else {
                    AlertStudent();}
            break;

            case R.id.practiceButton:
                if(!NoGroup)
                {   Activity.createGameManager();
                    try {   Activity.getgManager().setWorkingLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getgManager().StartMode("Practice");

                }
                else {
                    AlertStudent();}

            break;

            case R.id.testButton:
                if(!NoGroup)
                {   Activity.createGameManager();
                    try {   Activity.getgManager().setWorkingLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getgManager().StartMode("Test");

                }
                else {
                    AlertStudent();}

            break;

            case R.id.joinGroupButton:
                if(NoGroup)
                {   mTransaction.replace(R.id.main_container, new JoinGroupFragment(), "JoinGroup");
                    mTransaction.addToBackStack("JoinGroup");
                    Log.d(TAG,"Student will join a group");}
                else{
                    StopStudent();}

                break;
        }

        mTransaction.commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();

    }


    private void AlertStudent(){
        Toast.makeText(getActivity(),"Debes pertenecer a un grupo primero." , Toast.LENGTH_SHORT).show();
    }

    private void StopStudent(){
        Toast.makeText(getActivity(),"Ya te has unido a un grupo." , Toast.LENGTH_SHORT).show();
    }


    private class GetData extends AsyncTask<Void,Void,Void> implements Async {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try { Query(); }

            catch (ParseException e) {  e.printStackTrace();    }

            return null;
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Looking for your group");
            pDialog.setMessage("This may take a while");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public void Query() throws ParseException {
            ParseQuery<ParseObject> mParseQuery = new ParseQuery<>("Group");

            List< ParseUser > User = new ArrayList<>();
            User.add(ParseUser.getCurrentUser());

            mParseQuery.whereContainsAll("Students", User);

            List<ParseObject> JSON_List = mParseQuery.find();

            if(JSON_List==null||JSON_List.size()==0){
                Finalize(false);
            }
            else {
                ProcessQuery(JSON_List.get(0));
                Finalize(true);
            }

        }

        @Override
        public void ProcessQuery(ParseObject JSON) {
            ((MainActivity) getActivity()).setStudentTeacher(JSON.getObjectId());
            Log.d(TAG, "A JSON object was processed");
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(!Success){
                Log.d(TAG,"Student doesn't belong to a group");
                NoGroup = true;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "No se encuentró un grupo asociado a tu ID.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Log.d(TAG, "Student belongs to a group");

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),"Bienvenido " + ParseUser.getCurrentUser().getUsername() , Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }

    }
}
