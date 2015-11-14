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

import carloslobo.com.finalproject.Modules.ClassLetter;
import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.Modules.Adapters.LettersViewAdapter;
import carloslobo.com.finalproject.Modules.Interfaces.Async;
import carloslobo.com.finalproject.Modules.Interfaces.Communicator;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.R;


public class LettersFragment extends Fragment implements Init, RecyclerClickListener, Communicator {

    private final String TAG = LettersFragment.class.getName();

    private LettersViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;
    private List<ClassLetter> mData = new ArrayList<>();

    private ParseObject CurrentGroup;
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
        Log.d(TAG,"A letter was clicked.");
        onFordward(mData.get(position).getLetter());

    }

    @Override
    public void onFordward(String Letter) {

        Bundle args = new Bundle();
        args.putString("objId", CurrentGroup.getObjectId());
        args.putString("Letter",Letter);

        ReleaseLetterDialog dialogFragment = new ReleaseLetterDialog();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialogFragment.setArguments(args);
            dialogFragment.show(getActivity().getFragmentManager(), "Dialog Fragment");
        }

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
            ParseQuery<ParseObject> mParseQuery = new ParseQuery<>("Group");
            mParseQuery.whereEqualTo("Teacher", ParseUser.getCurrentUser());
            mParseQuery.whereEqualTo("GroupName", GroupName);
            mParseQuery.whereEqualTo("Open", true);
            List<ParseObject> JSON_List = mParseQuery.find();

            ParseQuery<ParseObject> mParseQuery2 = new ParseQuery<>("Letter");
            List<ParseObject> JSON_List2 = mParseQuery2.find();

            if(JSON_List == null){
                Log.d(TAG,"The selected group coudln't be found for some reason.");
                Finalize(false);
            }
            else {

                CurrentGroup = JSON_List.get(0);
                ReleasedLetters = CurrentGroup.getJSONArray("ReleasedLetters");

                for (ParseObject JSON:JSON_List2){
                    ProcessQuery(JSON);
                }

                Finalize(true);
            }
        }

        @Override
        public void ProcessQuery(ParseObject JSON) {
            String letter = JSON.get("Letter").toString();
            String objId = JSON.getObjectId();
            boolean locked = false;

            if(ReleasedLetters!=null) {
                if (ReleasedLetters.toString().contains(objId))
                    locked = true;
            }

            ClassLetter i = new ClassLetter(letter, 0, locked);

            mData.add(i);

            Log.d(TAG, "A JSON object was processed");
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(Success)
                Log.d(TAG,"Groups were loaded successfully");
            else
                Log.d(TAG,"Groups loading was unsuccessful");
        }



    }
}
