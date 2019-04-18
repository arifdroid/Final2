package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.afinal.R;

import java.util.ArrayList;

class RecyclerView_Admin_Profile_Adapter extends RecyclerView.Adapter<RecyclerView_Admin_Profile_Adapter.InsideHolder> {

    private Context mContext;

    private ArrayList<AdminDetail> adminDetails;


    //setup return list, and interface to pass back data.
    private ArrayList<AdminDetail> returnAdminDetails;

    int j=0;

    private PassResult_CheckBox_Interface passResult_checkBox_interface;

    public void setPassResult_checkBox_interface(PassResult_CheckBox_Interface passResult_checkBox_interface){
            this.passResult_checkBox_interface=passResult_checkBox_interface;
    }

    //public static boolean sentCheck;




    public RecyclerView_Admin_Profile_Adapter(Context context, ArrayList<AdminDetail> adminDetails) {

        this.mContext = context;
        this.adminDetails = adminDetails;
        this.returnAdminDetails = new ArrayList<>();

    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.cardview_admin_profile,viewGroup,false);


        //return null;

        return new InsideHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideHolder insideHolder, int i) {

        String ss = adminDetails.get(i).getImageViewPath();

        final AdminDetail adminDetail = adminDetails.get(i);

       j =i;


        insideHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    adminDetail.setCheckBox(true);


                    returnAdminDetails = adminDetails;

                    if(passResult_checkBox_interface!=null){

                        passResult_checkBox_interface.passingArray(returnAdminDetails);
                    }

                }else {

                    adminDetail.setCheckBox(false);

                    returnAdminDetails = adminDetails;

                    if(passResult_checkBox_interface!=null) {

                        passResult_checkBox_interface.passingArray(returnAdminDetails);
                    }

                 }

            }
        });


//       //problem is i always 1 for the problem.



        int id = mContext.getApplicationContext().getResources().getIdentifier(ss,null, mContext.getPackageName());

        if(i==2) {

            Log.i("checkkLocation", "9 location " +adminDetails.get(i).getTextShow());
        }
        insideHolder.imageViewList.setImageResource(id);
        insideHolder.textViewList.setText(adminDetails.get(i).getTextShow());

    }

    @Override
    public int getItemCount() {
        return adminDetails.size();
    }


    public class InsideHolder extends RecyclerView.ViewHolder {

        public TextView textViewList;
        public ImageView imageViewList;

        public CheckBox checkBox;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            textViewList = itemView.findViewById(R.id.admin_Profile_CardView_textViewiD);
            imageViewList = itemView.findViewById(R.id.admin_Profile_CardView_imageViewid);

            checkBox = itemView.findViewById(R.id.admin_Profile_CardView_checkBox);


        }


    }
}
