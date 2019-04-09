package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afinal.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerView_Frag_Adapter extends RecyclerView.Adapter<RecyclerView_Frag_Adapter.InsideHolde>{


    private final ArrayList<ReturnToRecycler> hereList;

    private Context mContext;

    public RecyclerView_Frag_Adapter(Context context, ArrayList<ReturnToRecycler> arrayList) {
        this.mContext = context;
        this.hereList = arrayList;
    }

    @NonNull
    @Override
    public InsideHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
         View rootView = LayoutInflater.from(mContext).inflate(R.layout.cardview_bottom_nav_time_stamp,parent,false);



        return new InsideHolde(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideHolde holder, int position) {

        holder.textViewName.setText(hereList.get(position).getName());
//        holder.textViewTime.setText("Time");
//        holder.textViewStatus.setText("Status");
//        holder.textViewDate.setText("Date");

        holder.textViewDate_input.setText(hereList.get(position).getDate());
        holder.textViewStatus_input.setText(hereList.get(position).getStatus());
        holder.textViewTime_input.setText(hereList.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        return hereList.size();
    }

    public class InsideHolde extends RecyclerView.ViewHolder {

        public TextView textViewName, textViewDate,textViewDate_input, textViewStatus,textViewStatus_input,
                        textViewTime, textViewTime_input;

        public CircleImageView circleImageView;

        public InsideHolde(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.cardview_timestamp_frag_textViewName);
            textViewDate = itemView.findViewById(R.id.cardview_timestamp_frag_textViewDate);
            textViewDate_input = itemView.findViewById(R.id.cardview_timestamp_frag_textViewDate_Input);
            textViewStatus = itemView.findViewById(R.id.cardview_timestamp_frag_textViewStatus);
            textViewStatus_input = itemView.findViewById(R.id.cardview_timestamp_frag_textViewStatus_Input);
            textViewTime = itemView.findViewById(R.id.cardview_timestamp_frag_textViewTime);
            textViewTime_input = itemView.findViewById(R.id.cardview_timestamp_frag_textViewTime_Input);

        }
    }
}
