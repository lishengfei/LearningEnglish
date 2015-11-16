package carloslobo.com.finalproject.Fragments.Student;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import carloslobo.com.finalproject.Modules.Interfaces.AsyncResponse;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class JoinGroupFragment extends Fragment implements Init,View.OnClickListener {

    public final static String TAG = JoinGroupFragment.class.getName();

    //Layout
    private TextInputLayout mGroup;
    private String Group;
    private Button mButton;

    public JoinGroupFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_join_group, container, false);

        initViews(rootView);
        initListeners();
        HideAppBarLayout();

        return rootView;
    }


    @Override
    public void initViews(View rootView) {
        mButton = (Button) rootView.findViewById(R.id.joinGroupButton);
        mGroup = (TextInputLayout) rootView.findViewById(R.id.GroupName);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private boolean validateInput() {
        Group = mGroup.getEditText().getText().toString();

        if (Group.isEmpty()) {
            if (Group.isEmpty())
                mGroup.setError("El campo de nombre esta vacío");

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
            case R.id.joinGroupButton:
                if (validateInput()) {
                    Log.d(TAG,"Student is joining a group");
                    new SendData().execute();}
                break;
        }
    }


    private class SendData extends AsyncTask<Void,Void,Void> implements AsyncResponse {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {   Query();    }
            catch (ParseException e) {  e.printStackTrace();    }

            return null;
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Joining group");
            pDialog.setMessage("This will take a moment.");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        public void Query() throws ParseException {
            ParseQuery<ParseObject> mParseQuery = ParseQuery.getQuery("Group");
            mParseQuery.whereEqualTo("GroupName", Group);

            List<ParseObject> JSON = mParseQuery.find();

            if(JSON==null||JSON.size()==0){
                Finalize(false);}
            else{
                Log.d(TAG, "A group was found");
                ProcessQuery(JSON.get(0));
                Finalize(true);
            }

        }

        @Override
        public void ProcessQuery(ParseObject JSON) throws ParseException {
            JSON.addUnique("Students", ParseUser.getCurrentUser());
            JSON.save();
            Log.d(TAG, "Parcel was sent to Parse");
        }

        @Override
        public void Finalize(boolean success) {
            pDialog.dismiss();

            if (!success){
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "No se encontró un grupo.", Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                Log.d(TAG, Group + " was updated");

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Te uniste a " + Group, Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        }


    }

}
