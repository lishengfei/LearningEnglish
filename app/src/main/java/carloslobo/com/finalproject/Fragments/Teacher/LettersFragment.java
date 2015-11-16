package carloslobo.com.finalproject.Fragments.Teacher;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import carloslobo.com.finalproject.Modules.Letter;
import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.Modules.Adapters.LettersViewAdapter;
import carloslobo.com.finalproject.Modules.Interfaces.AsyncResponse;
import carloslobo.com.finalproject.Modules.Interfaces.Communicator;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


public class LettersFragment extends Fragment implements Init, RecyclerClickListener, Communicator {

    private final String TAG = LettersFragment.class.getName();

    //Layout
    private LettersViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;
    private List<Letter> mData = new ArrayList<>();

    //Data Structures
    private String CurrentGroup;
    private String SelectedLetter;
    JSONArray ReleasedLetters;
    private String GroupName;

    public LettersFragment() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_letters, container, false);

        GroupName = getArguments().getString("GroupName");

        initViews(rootView);
        initListeners();

        new GetData().execute();

        return rootView;
    }


    @Override
    public void initViews(View rootView){
        mViewAdapter = new LettersViewAdapter(getActivity(),mData);
        initRecyclerView(rootView);
    }

    @Override
    public void initListeners() {
        mViewAdapter.setRecyclerViewClickListener(this);
    }

    private void initRecyclerView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.letterRecycle);
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void itemClick(View view, int position) {
        Letter L = mData.get(position);

        SelectedLetter = L.getLetterId();
        Send(SelectedLetter);

        Log.d(TAG, "The teacher will release a letter");
    }

    @Override
    public void Send(String Letter) {

        Bundle args = new Bundle();
        args.putString("GroupId", CurrentGroup);
        args.putString("LetterId",Letter);

        ReleaseLetterDialog Popup = new ReleaseLetterDialog();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Popup.setArguments(args);
            Popup.show(getActivity().getFragmentManager(), "Dialog Fragment");
        }

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
            try {   Query();    }

            catch (ParseException e) {  e.printStackTrace();    }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mViewAdapter.notifyDataSetChanged();
        }

        @Override
        public void Setup() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("Loading group's info.");
            pDialog.setMessage("This may take a while.");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public void Query() throws ParseException {
            ParseQuery<ParseObject> GroupQuery = new ParseQuery<>("Group");
            GroupQuery.whereEqualTo("Teacher", ParseUser.getCurrentUser());
            GroupQuery.whereEqualTo("GroupName", GroupName);
            GroupQuery.whereEqualTo("Open", true);
            List<ParseObject> Group_List = GroupQuery.find();

            ParseQuery<ParseObject> LettersQuery = new ParseQuery<>("Letter");
            List<ParseObject> Letters_List = LettersQuery.find();

            if(Group_List == null){
                Finalize(false);}
            else {
                ParseObject GroupJSON = Group_List.get(0);
                CurrentGroup = GroupJSON.getObjectId();
                ReleasedLetters = GroupJSON.getJSONArray("ReleasedLetters");

                for (ParseObject JSON:Letters_List){
                    ProcessQuery(JSON);
                }

                Finalize(true);}
        }

        @Override
        public void ProcessQuery(ParseObject JSON) throws ParseException {

            String objId = JSON.getObjectId();
            String letter = JSON.getString("Letter");
            boolean locked = false;

            if(ReleasedLetters!=null) {
                if (ReleasedLetters.toString().contains(objId))
                    locked = true;
            }

            //I should fix this part, a double request per question seems bad.
            ParseQuery<ParseObject> GradesQuery = new ParseQuery<>("Grades");
            GradesQuery.whereEqualTo("Letter", JSON);
            GradesQuery.whereEqualTo("Group", ParseObject.createWithoutData("Group",CurrentGroup) );
            List<ParseObject> GradesJSON = GradesQuery.find();

            mData.add( new Letter(objId, letter, GradesJSON.size(), locked) );

            Log.d(TAG, "A JSON object was processed");
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(Success)
                Log.d(TAG,"Letters were loaded successfully");
            else
                Log.d(TAG,"The group's letters could not be loaded.");
        }

    }
}
