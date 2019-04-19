package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.app.TimePickerDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.TimePickerFragment;

import java.util.ArrayList;

class RecyclerView_Admin_Profile_Adapter extends RecyclerView.Adapter<RecyclerView_Admin_Profile_Adapter.InsideHolder>{

//    FragMangageCompat fragMangageCompat;

    private Context mContext;

    private ArrayList<AdminDetail> adminDetails;


    //setup return list, and interface to pass back data.
    private ArrayList<AdminDetail> returnAdminDetails;

    int j=0;

    private PassResult_CheckBox_Interface passResult_checkBox_interface;
    private String hour;
    private String minute;

    public void setPassResult_checkBox_interface(PassResult_CheckBox_Interface passResult_checkBox_interface){
            this.passResult_checkBox_interface=passResult_checkBox_interface;
    }

    //public static boolean sentCheck;




    public RecyclerView_Admin_Profile_Adapter(Context context, ArrayList<AdminDetail> adminDetails) {
       // fragMangageCompat = new FragMangageCompat();
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


        //set onclicklistener for cardview.
        if(i==2) {
            insideHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DialogFragment timepickerFragment = new TimePickerFragment();
//                    timepickerFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(),"Time Picker");

//                    fragMangageCompat.getTime(mContext);
//
//                    //if(fragMangageCompat.on)
//                   hour = fragMangageCompat.getHour();
//                   minute = fragMangageCompat.getMinute();

                    DialogFragment dialogFragment = new TimePickerFragment();
                    dialogFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(),"Time Picker");

                }
            });

            hour = RegAdmin_asAdmin_Profile_Activity.hour;
            minute = RegAdmin_asAdmin_Profile_Activity.minute;

            if(i==2) {



                if (hour != null) {

                    insideHolder.textViewList.setText("morning time is: "+hour+":"+minute);

                }

            }
            if(i==3){

                if (hour != null) {

                    insideHolder.textViewList.setText("evening time is: "+hour+":"+minute);

                }


            }

        }

        if(i==3) {
            insideHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DialogFragment timepickerFragment = new TimePickerFragment();
//                    timepickerFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(),"Time Picker");

//                    fragMangageCompat.getTime(mContext);
//
//                    //if(fragMangageCompat.on)
//                   hour = fragMangageCompat.getHour();
//                   minute = fragMangageCompat.getMinute();

                    DialogFragment dialogFragment = new TimePickerFragment();
                    dialogFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(),"Time Picker");

                }
            });

            hour = RegAdmin_asAdmin_Profile_Activity.hour;
            minute = RegAdmin_asAdmin_Profile_Activity.minute;


            if(i==3){

                if (hour != null) {

                    insideHolder.textViewList.setText("evening time is: "+hour+":"+minute);

                }


            }

        }



    }


//    public static class FragMangageCompat extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
//
//        String hour;
//        String minute;
//
//        public FragMangageCompat(){
//
//        }
//
//        public static void getTime(Context context){
//
//            DialogFragment timepicker = new TimePickerFragment();
//            timepicker.show(((AppCompatActivity)context).getSupportFragmentManager(),"time picker");
//        }
//
//
//        @Override
//        public void onTimeSet(TimePicker timePicker, int i, int i1) {
//            this.hour = String.valueOf(i);
//
//            this.minute=String.valueOf(i1);
//
//        }
//
//        public String getHour() {
//
//            if (hour!=null) {
//                return hour;
//            }
//            return null;
//        }
//
//        public String getMinute() {
//            if(minute!=null) {
//                return minute;
//            }
//            return null;
//        }
//    }



    @Override
    public int getItemCount() {
        return adminDetails.size();
    }


    public class InsideHolder extends RecyclerView.ViewHolder {

        public TextView textViewList;
        public ImageView imageViewList;

        public CheckBox checkBox;

        public CardView cardView;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            textViewList = itemView.findViewById(R.id.admin_Profile_CardView_textViewiD);
            imageViewList = itemView.findViewById(R.id.admin_Profile_CardView_imageViewid);

            checkBox = itemView.findViewById(R.id.admin_Profile_CardView_checkBox);

            cardView = itemView.findViewById(R.id.admin_Profile_cardViewiD);

//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

        }


    }
}
