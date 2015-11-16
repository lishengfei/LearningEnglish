package carloslobo.com.finalproject.Modules.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import carloslobo.com.finalproject.Modules.Letter;
import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.R;

/**
 * Created by camilo on 11/6/15.
 */
public class LettersViewAdapter extends RecyclerView.Adapter<LettersViewAdapter.CustomViewHolder>{

    private LayoutInflater mInflater;
    private List<Letter> data = new ArrayList<>();
    private RecyclerClickListener mRecyclerClickListener;

    public LettersViewAdapter(Context context, List<Letter> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.letter_row, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        bindInfo(holder,position);
    }

    private void bindInfo(CustomViewHolder holder, int position){
        Letter i = data.get(position);
        holder.mLetter.setText("Letra: "+i.getLetter());
        holder.StudentTries.setText(i.getEntries()+" envíos");

        if(i.isLocked()) {
            holder.mLetter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_open, 0, 0, 0);}
        else {
            holder.mLetter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_closed, 0, 0, 0);}
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
        private TextView mLetter, StudentTries;

        public CustomViewHolder(View itemView) {
            super(itemView);

            mLetter = (TextView) itemView.findViewById(R.id.letterTextView);
            StudentTries = (TextView) itemView.findViewById(R.id.studentTries);

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
