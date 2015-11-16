package carloslobo.com.finalproject.Fragments.Teacher;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.parse.ParseUser;

import carloslobo.com.finalproject.Modules.Interfaces.AsyncResponse;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewGroupFragment extends Fragment implements Init, View.OnClickListener{

    private final static String TAG = NewGroupFragment.class.getName();

    //Layout
    TextInputLayout mGroup;
    Button mButton;

    //Variables
    String Group;


    public NewGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_group, container, false);

        initViews(rootView);
        initListeners();

        return rootView;
    }


    @Override
    public void initViews(View rootView){
        mGroup = (TextInputLayout) rootView.findViewById(R.id.newGroupName);
        mButton = (Button) rootView.findViewById(R.id.newGroupButton);
    }

    @Override
    public void initListeners() {
        mButton.setOnClickListener(this);
    }

    private boolean validateInput() {
        Group = mGroup.getEditText().getText().toString();

        if (Group.isEmpty()) {
            mGroup.setError("El campo de nombre esta vacío");
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.newGroupButton:

                if(validateInput()) {
                    new SendData().execute();
                    Log.d(TAG,"Creating new group");}
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

            ParseObject mParcel = new ParseObject("Group");
            mParcel.put("GroupName",Group);
            mParcel.put("Teacher", ParseUser.getCurrentUser());
            mParcel.put("Open", true);
            mParcel.saveInBackground();

            Log.d(TAG,"Parcel was sent to Parse");

            Finalize(true);

            return null;
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Creating new group");
            pDialog.setMessage("This will take a moment.");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public void Query() throws ParseException {
            return;
        }

        @Override
        public void ProcessQuery(ParseObject JSON) {
            return;
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(Success) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), Group + " was created", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }
        }

    }
}
