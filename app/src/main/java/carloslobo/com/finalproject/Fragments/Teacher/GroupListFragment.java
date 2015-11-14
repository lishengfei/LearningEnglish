package carloslobo.com.finalproject.Fragments.Teacher;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import carloslobo.com.finalproject.Modules.Adapters.GroupViewAdapter;
import carloslobo.com.finalproject.Modules.Interfaces.Async;
import carloslobo.com.finalproject.Modules.Interfaces.Communicator;
import carloslobo.com.finalproject.Modules.Interfaces.Init;
import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends Fragment implements Init, RecyclerClickListener, View.OnClickListener, Communicator {

    private final static String TAG = GroupListFragment.class.getName();

    private FloatingActionButton mButton;
    private GroupViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;

    HashMap<String,String> mData = new HashMap();

    public GroupListFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_group_list, container, false);

        initViews(rootView);
        initListeners();

        new GetData().execute();

        return rootView;
    }

    @Override
    public void initViews(View rootView){
        mButton = (FloatingActionButton) rootView.findViewById(R.id.addNewGroupButton);
        mViewAdapter = new GroupViewAdapter(getActivity(), mData);
        initRecyclerView(rootView);

    }

    @Override
    public void initListeners() {
        mViewAdapter.setRecyclerViewClickListener(this);
        mButton.setOnClickListener(this);
    }

    private void initRecyclerView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.groupRecycle);
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void itemClick(View view, int position) {

        String Key = mData.keySet().toArray()[position].toString(); //Curso Name
        String Value = mData.get(Key);//Estds.

        Log.d(TAG, Key+", "+Value);
        Toast.makeText(getActivity(),Key+", "+Value,Toast.LENGTH_SHORT).show();

        onFordward(Key);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.addNewGroupButton:

                FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mTransaction.replace(R.id.main_container, new NewGroupFragment(),"NewGroup");
                mTransaction.addToBackStack(null);
                mTransaction.commit();

                Log.d(TAG, "A new group will be created");
            break;
        }
    }

    @Override
    public void onFordward(String GroupName) {
        Bundle args = new Bundle();
        args.putString("GroupName", GroupName);

        FragmentTransaction mTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        Fragment Target = new  LettersFragment();
        Target.setArguments(args);

        mTransaction.replace(R.id.main_container, Target).addToBackStack("GroupLetters");
        mTransaction.commit();
    }

    private class GetData extends AsyncTask<Void,Void,Void> implements Async {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Setup();
        }

        @Override
        protected Void doInBackground(Void... params){

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
            pDialog.setTitle("Loading from groups");
            pDialog.setMessage("This may take a while");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        public void Query() throws ParseException {
            ParseQuery<ParseObject> mParseQuery = new ParseQuery<>("Group");
            mParseQuery.whereEqualTo("Teacher", ParseUser.getCurrentUser());
            mParseQuery.whereEqualTo("Open", true);



            List<ParseObject> JSON_List = mParseQuery.find();

            if(JSON_List == null){
                Log.d(TAG,"No groups found.");
                Finalize(false);
            }
            else {

                for (ParseObject JSON:JSON_List){
                    ProcessQuery(JSON);
                }

                Finalize(true);
            }
        }

        @Override
        public void ProcessQuery(ParseObject JSON) {
            String GroupName = JSON.get("GroupName").toString();

            JSONArray Students = JSON.getJSONArray("Students");
            int nStudents = (Students==null)? 0:Students.length();

            mData.put(GroupName, nStudents + "");

            Log.d(TAG, "A JSON object was processed");
        }

        @Override
        public void Finalize(boolean Success) {
            pDialog.dismiss();

            if(!Success) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "No se encontraron grupos.", Toast.LENGTH_SHORT).show();;
                    }
                });
            }
        }


    }
}
