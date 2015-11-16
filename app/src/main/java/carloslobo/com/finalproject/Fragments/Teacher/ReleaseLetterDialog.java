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

import com.parse.ParseException;
import com.parse.ParseObject;

import carloslobo.com.finalproject.Modules.Interfaces.AsyncRequest;
import carloslobo.com.finalproject.Modules.Interfaces.AsyncResponse;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ReleaseLetterDialog extends DialogFragment implements Init,View.OnClickListener{

    private final String TAG = ReleaseLetterDialog.class.getName();

    //Layout
    private Button Cancel,Ok;

    //Variables
    private String TARGET_GROUP, TARGET_LETTER;

    public ReleaseLetterDialog() {  }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setStyle(R.style.AppCompatAlertDialogStyle, R.style.AppCompatAlertDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_release_letter, container, false);

        TARGET_GROUP = getArguments().getString("GroupId");
        TARGET_LETTER = getArguments().getString("LetterId");

        initViews(rootView);
        initListeners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getDialog().setTitle("Liberar Letra");
        }

        return rootView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.OK_button:
                new SendData().execute();
                break;
        }

        this.dismiss();
    }

    @Override
    public void initViews(View rootView) {
        Ok = (Button) rootView.findViewById(R.id.OK_button);
        Cancel = (Button) rootView.findViewById(R.id.CANCEL_button);
    }

    @Override
    public void initListeners() {
        Ok.setOnClickListener(this);
        Cancel.setOnClickListener(this);
    }


    private class SendData extends AsyncTask<Void,Void,Void> implements AsyncRequest {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MakeRequest();
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

        @Override
        public void MakeRequest() {
            ParseObject UpdateGroup = ParseObject.createWithoutData("Group",TARGET_GROUP);
            UpdateGroup.addUnique("ReleasedLetters", TARGET_LETTER);

            try {   UpdateGroup.save(); }
            catch (ParseException e) {  e.printStackTrace();    }

            Finalize(true);
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if (Success)
                Log.d(TAG,"The letter was released");
            else
                Log.d(TAG,"The letter could not be released");

        }



    }
}
