package carloslobo.com.finalproject.Fragments.Teacher;


import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import carloslobo.com.finalproject.Modules.Interfaces.Async;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ReleaseLetterDialog extends DialogFragment implements Init,View.OnClickListener{

    private final String TAG = ReleaseLetterDialog.class.getName();

    private Button Cancel,Ok;
    private String targetObjectId, letterName, targetLetterId;

    public ReleaseLetterDialog() {  }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setStyle(R.style.AppCompatAlertDialogStyle, R.style.AppCompatAlertDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_release_letter, container, false);

        targetObjectId = getArguments().getString("objId");
        letterName = getArguments().getString("Letter");

        initViews(rootView);
        initListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getDialog().setTitle("Liberar Letra");
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.OK_button:
                new SendData().execute();
                this.dismiss();
                break;

            default:
                this.dismiss();
                break;
        }
    }

    @Override
    public void initViews(View rootview) {
        Ok = (Button) rootview.findViewById(R.id.OK_button);
        Cancel = (Button) rootview.findViewById(R.id.CANCEL_button);
    }

    @Override
    public void initListeners() {
        Ok.setOnClickListener(this);
        Cancel.setOnClickListener(this);
    }


    private class SendData extends AsyncTask<Void,Void,Void> implements Async {

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
            pDialog.setTitle("Releasing letter");
            pDialog.setMessage("This will take a moment.");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        public void Query() throws ParseException {

            ParseQuery<ParseObject> mParseQuery = ParseQuery.getQuery("Group");
            mParseQuery.whereEqualTo("objectId", targetObjectId);
            List<ParseObject> JSON = mParseQuery.find();

            ParseQuery<ParseObject> mParseQuery2 = ParseQuery.getQuery("Letter");
            mParseQuery2.whereEqualTo("Letter",letterName);
            List<ParseObject> JSON2 = mParseQuery2.find();

            if(JSON==null||JSON2==null){
                Finalize(false);}
            else{
                targetLetterId = JSON2.get(0).getObjectId();
                ProcessQuery(JSON.get(0));
                Finalize(true);
            }

        }

        @Override
        public void ProcessQuery(ParseObject JSON) {
            JSON.addUnique("ReleasedLetters", targetLetterId);
            JSON.saveInBackground();
            Log.d(TAG, "Parcel was sent to Parse");
        }

        @Override
        public void Finalize(boolean success) {
            pDialog.dismiss();

            if (success){
                Log.d(TAG,"Letter was released");
            }else {
                Log.d(TAG,"Letter could not be released");
            }
        }


    }
}
