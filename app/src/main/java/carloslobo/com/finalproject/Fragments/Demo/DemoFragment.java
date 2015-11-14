package carloslobo.com.finalproject.Fragments.Demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import carloslobo.com.finalproject.Modules.Demo.Information;
import carloslobo.com.finalproject.R;
import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.Modules.Demo.ViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class DemoFragment extends Fragment implements RecyclerClickListener {

    private ViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;

    public DemoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.demo_fragment, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView){
        mViewAdapter = new ViewAdapter(getActivity(), initData());
        initRecyclerView(rootView);
    }

    private List<Information> initData(){
        List<Information> data = new ArrayList<>();
        String[] titulo = getResources().getStringArray(R.array.test);

        for(int i=0; i<titulo.length; i++){ data.add(new Information(titulo[i]) ); }

        return data;
    }

    private void initRecyclerView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle);
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListeners();
    }

    private void setListeners(){
        mViewAdapter.setRecyclerViewClickListener(this);
    }

    @Override
    public void itemClick(View view, int position) {
        System.out.println("Clicked " + position);
    }
}
