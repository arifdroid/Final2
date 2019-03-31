package com.example.afinal.fingerPrint_Login.register.register_as_admin_add_userList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afinal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewAdapter_UserList extends RecyclerView.Adapter<RecyclerViewAdapter_UserList.InsideHolder> {

    private ArrayList<UserFromAdmin> userFromAdmins;

    private Context mContext;

    public RecyclerViewAdapter_UserList(Add_User_Activity add_user_activity, ArrayList<UserFromAdmin> userList) {

        Log.i("checkAddingUser, ","2 ");
        this.userFromAdmins=userList;
        this.mContext = add_user_activity;
    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_user_adapter_simplelist,parent,false);

        return new InsideHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideHolder holder, int position) {

        Log.i("checkAddingUser, ","10 onBindViewHolder, position: " +position);

        Log.i("checkAddingUser, ","11 onBindViewHolder, list size: " +userFromAdmins.size());

        holder.textViewNum.setText(""+(position+1));
        holder.textViewPhone.setText(userFromAdmins.get(position).getPhone());
        holder.textViewName.setText(userFromAdmins.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return userFromAdmins.size();
    }


    public class InsideHolder extends RecyclerView.ViewHolder {

        public TextView textViewPhone, textViewName, textViewNum;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.add_user_nameTextViewiD);
            textViewPhone = itemView.findViewById(R.id.add_user_phoneTextViewiD);
            textViewNum = itemView.findViewById(R.id.add_user_numberTextViewiD);



        }
    }
}
