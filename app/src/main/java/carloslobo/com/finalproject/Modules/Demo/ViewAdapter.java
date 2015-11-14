package carloslobo.com.finalproject.Modules.Demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import carloslobo.com.finalproject.R;

/**
 * Created by camilo on 11/6/15.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.CustomViewHolder>{

    private LayoutInflater mInflater;
    private List<Information> data= Collections.emptyList();
    private RecyclerClickListener mRecyclerClickListener;

    public ViewAdapter(Context context, List<Information> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_row, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        bindInfo(holder,position);
    }

    private void bindInfo(CustomViewHolder holder, int position){
        Information i = data.get(position);
        holder.mCardView.setText(i.title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setRecyclerViewClickListener( RecyclerClickListener recyclerClickListener){
        mRecyclerClickListener = recyclerClickListener;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mCardView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mCardView = (TextView) itemView.findViewById(R.id.listText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(mRecyclerClickListener!=null) {
                mRecyclerClickListener.itemClick(view,getAdapterPosition());
            }
        }
    }

}
