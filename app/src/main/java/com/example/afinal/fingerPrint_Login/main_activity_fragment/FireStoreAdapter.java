package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.oop.UserCheckIn;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

class FireStoreAdapter extends FirestoreRecyclerAdapter<UserCheckIn, FireStoreAdapter.InsideHolder> {

    public FireStoreAdapter(FirestoreRecyclerOptions<UserCheckIn> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull InsideHolder holder, int position, @NonNull UserCheckIn model) {

        //holder.circleImageView.setImageResource(model.getImage_url());

        holder.textView.setText(model.getName());
        holder.ratingBar.setRating(model.getRating());

    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bottom_nav_card_user_score, viewGroup,false);


        return new InsideHolder(v);
    }

    public class InsideHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView textView;
        public RatingBar ratingBar;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.bottom_nav_userScore_circleImageViewiD);
            textView = itemView.findViewById(R.id.bottom_nav_userScore_textViewiD);
            ratingBar = itemView.findViewById(R.id.bottom_nav_userScore_RatingBariD);
        }
    }
}
