package carloslobo.com.finalproject.Fragments.Student;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import carloslobo.com.finalproject.Core.MainActivity;
import carloslobo.com.finalproject.Modules.Interfaces.AsyncResponse;
import carloslobo.com.finalproject.Modules.Interfaces.Communicator;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentLetterMenuFragment extends Fragment implements Init,View.OnClickListener{

    private final static String TAG = StudentLetterMenuFragment.class.getName();

    //Layout
    private ImageButton Introduction,Practice,Test;
    private FloatingActionButton JoinGroup;
    private TextView mUser;
    private ImageView mBanner;

    //Variables
    private boolean NoGroup = true;
    private boolean TestLock = true;

    public StudentLetterMenuFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_student_letter_menu, container, false);

        initViews(rootView);
        initListeners();

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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        MainActivity Activity = ((MainActivity) getActivity());

        switch (id){
            case R.id.introductionButton:
                if(!NoGroup) {
                    Activity.createGameManager();

                    try {   Activity.getGManager().setGameLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getGManager().Setup("Introduction");
                }
                else {
                    AlertStudent();}
            break;

            case R.id.practiceButton:
                if(!NoGroup)
                {   Activity.createGameManager();

                    try {   Activity.getGManager().setGameLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getGManager().Setup("Practice");
                }
                else {
                    AlertStudent();}

            break;

            case R.id.testButton:
                if(NoGroup)
                    AlertStudent();
                else if(TestLock)
                    PracticePending();
                else {
                    Activity.createGameManager();

                    try {   Activity.getGManager().setGameLetter("8RBZaC0a0Z");}
                    catch (ParseException e) {    e.printStackTrace();    }

                    Activity.getGManager().Setup("Test");
                }

            break;

            case R.id.joinGroupButton:
                if(NoGroup)
                {   FragmentTransaction mTransaction = Activity.getSupportFragmentManager().beginTransaction();
                    mTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    mTransaction.replace(R.id.main_container, new JoinGroupFragment(), "JoinGroup");
                    mTransaction.addToBackStack("JoinGroup");
                    mTransaction.commit();

                    Log.d(TAG, "Student will join a group");
                }
                else{
                    StopStudent();}
                break;
        }
    }


    private void AlertStudent(){
        Toast mToast = Toast.makeText(getActivity(), "Debes pertenecer a un grupo primero.", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 120);
        mToast.show();
    }

    private void StopStudent(){
        Toast mToast =  Toast.makeText(getActivity(),"Ya te has unido a un grupo." , Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 120);
        mToast.show();
    }

    private void PracticePending(){
        Toast mToast =  Toast.makeText(getActivity(),"Debes realizar la práctica primero." , Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 120);
        mToast.show();
    }

    private class GetData extends AsyncTask<Void,Void,Void> implements AsyncResponse {
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

            if(JSON_List == null || JSON_List.size() == 0){
                Finalize(false);
            }
            else {
                ProcessQuery(JSON_List.get(0));
                Finalize(true);
            }

        }

        @Override
        public void ProcessQuery(ParseObject JSON) throws ParseException {
            ((MainActivity) getActivity()).setCurrentGroup(JSON.getObjectId());
            ((MainActivity) getActivity()).setStudentTeacher( ((ParseObject) JSON.get("Teacher")).getObjectId()  );

            ParseQuery<ParseObject> GradesQuery = new ParseQuery<>("Grades");
            GradesQuery.whereEqualTo("Module", "Practice");
            List<ParseObject> GradesJSON = GradesQuery.find();

            if(GradesJSON!=null) {
                if(GradesJSON.size()!=0)
                    TestLock = false;
                else
                    Log.d(TAG,"The student has the introduction pending.");
            }

            Log.d(TAG, "A JSON object was processed");
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(!Success){
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast mToast = Toast.makeText(getActivity(), "No se encontró un grupo asociado a tu ID.", Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.TOP, 0, 120);
                        mToast.show();
                    }
                });

                Log.d(TAG, "Student doesn't belong to a group");
            }
            else {
                NoGroup = false;
                Log.d(TAG, "Student belongs to a group");
            }
        }

    }
}
