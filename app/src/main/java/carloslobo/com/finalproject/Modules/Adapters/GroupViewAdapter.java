package carloslobo.com.finalproject.Modules.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.HashMap;

import carloslobo.com.finalproject.Modules.Demo.RecyclerClickListener;
import carloslobo.com.finalproject.R;


public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.CustomViewHolder>{

    private LayoutInflater mInflater;;
    private HashMap<String,String> data = new HashMap();
    private RecyclerClickListener mRecyclerClickListener;

    public GroupViewAdapter(Context context, HashMap data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.group_row, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        bindInfo(holder,position);
    }

    private void bindInfo(CustomViewHolder holder, int position){
        String Key = data.keySet().toArray()[position].toString();
        String Value = data.get(Key);

        holder.mGroup.setText(Key);
        holder.mStudents.setText(Value + " Est.");
        holder.mTeacher.setText(ParseUser.getCurrentUser().getUsername());

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
        private TextView mGroup, mStudents,mTeacher;

        public CustomViewHolder(View itemView) {
            super(itemView);

            mGroup = (TextView) itemView.findViewById(R.id.groupNameTV);
            mStudents = (TextView) itemView.findViewById(R.id.StudentsNumber);
            mTeacher = (TextView) itemView.findViewById(R.id.teacherNameTV);

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
