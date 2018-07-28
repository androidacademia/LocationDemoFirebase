package login.com.girish.locationdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<MyLocation> myLocationArrayList;

    public MyAdapter(Context context,ArrayList<MyLocation> myLocationArrayList){
        this.context = context;
        this.myLocationArrayList = myLocationArrayList;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.list_item,null,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.textViewLoc.setText(myLocationArrayList.get(position).getLat()+","+myLocationArrayList.get(position).getLng());
        myViewHolder.textViewDate.setText(myLocationArrayList.get(position).getLocDate());
    }
    @Override
    public int getItemCount() {
        return myLocationArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLoc;
        TextView textViewDate;
        public MyViewHolder(@NonNull View view) {
            super(view);
            textViewLoc = (TextView)view.findViewById(R.id.textViewLOC);
            textViewDate = (TextView)view.findViewById(R.id.textViewDate);
        }
    }
}
