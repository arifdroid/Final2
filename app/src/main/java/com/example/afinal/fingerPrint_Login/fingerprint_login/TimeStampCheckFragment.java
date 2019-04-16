package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.main_activity_fragment.Main_BottomNav_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//https://guides.codepath.com/android/using-dialogfragment

public class TimeStampCheckFragment extends DialogFragment {

    private static DocumentReference documentReference;
    private TextView textViewAsk,textViewTime;
    private Button buttonCheckin, buttonCheckOut;
    private String timestampnow;
    private String morOrEveNow;
    private boolean zeroOut;
    private String day;


    public TimeStampCheckFragment() {
    }


    public static TimeStampCheckFragment newInstance(String day, String date,String timeStamp,String userName, String userPhone,String adminName,String adminPhone,String monOrEve, Boolean zeroOut){
        TimeStampCheckFragment timeStampCheckFragment = new TimeStampCheckFragment();
        Bundle args = new Bundle();
        args.putString("day",day);
        args.putString("date",date);
        args.putString("timestamp",timeStamp);
        args.putString("monOrEve",monOrEve);
        args.putBoolean("zeroOut",zeroOut);

        documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document(adminName+adminPhone+"doc").collection("all_employee_thisAdmin_collection")
                .document(userName+userPhone+"doc");


        timeStampCheckFragment.setArguments(args);

        return timeStampCheckFragment;
    }

    public static TimeStampCheckFragment newInstance(String day, String date,String timeStamp,String userName, String userPhone,String adminName,String adminPhone,String morOrEve){
        TimeStampCheckFragment timeStampCheckFragment = new TimeStampCheckFragment();
        Bundle args = new Bundle();
        args.putString("day",day);
        args.putString("date",date);
        args.putString("timestamp",timeStamp);
        args.putString("morOrEve",morOrEve);

        documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document(adminName+adminPhone+"doc").collection("all_employee_thisAdmin_collection")
                .document(userName+userPhone+"doc");


        timeStampCheckFragment.setArguments(args);

        return timeStampCheckFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.cardview_morning_evening_timestamp, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String amOrPM ="";

        textViewAsk = view.findViewById(R.id.textView2);
        buttonCheckin = view.findViewById(R.id.cardview_PunchIntiD);
        buttonCheckOut = view.findViewById(R.id.cardview_PunchOutiD);
        textViewTime = view.findViewById(R.id.textViewCardView_TimeFragmentiD);

        day = getArguments().getString("day", "");
       // getDialog().setTitle(question);
//a
        String date = getArguments().getString("date","");

        timestampnow = getArguments().getString("timestamp","");

        morOrEveNow = getArguments().getString("morOrEve","");

        if(morOrEveNow.equals("morning")){
             amOrPM = "AM";
        }else {
            amOrPM="PM";
        }

        zeroOut = getArguments().getBoolean("zeroOut",false);

        textViewAsk.setText(day+","+date+": Do you want to punch card this "+morOrEveNow+",");
        textViewTime.setText("at "+timestampnow+" "+amOrPM+" ?");


        textViewAsk.requestFocus(); //what is this?
        textViewTime.requestFocus();

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        buttonCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need data like, admin name , admin phone, user name, user phone, undoubtly time stamp. and day.

                Log.i("onclickFinal","1");

                Map<String,Object> kk = new HashMap<>();

                if(morOrEveNow.equals("morning")) {

                    Log.i("onclickFinal","2");

                    if (day != null) {

                        Log.i("onclickFinal","3");
                        if(zeroOut) {
                            timestampnow = "0";
                        }

                            if (day.equals("Mon")) {

                                kk.put("ts_mon_morning", timestampnow);


                            } else if (day.equals("Tue")) {

                                kk.put("ts_tue_morning", timestampnow);


                            } else if (day.equals("Wed")) {

                                kk.put("ts_wed_morning", timestampnow);


                            } else if (day.equals("Thu")) {

                                kk.put("ts_thu_morning", timestampnow);


                            } else if (day.equals("Fri")) {

                                kk.put("ts_fri_morning", timestampnow);


                            }

                    }

                }else if(morOrEveNow.equals("evening")) { //evening time stamp.

                    if(zeroOut) {

                        timestampnow = "0";
                    }

                        if (day != null) {

                            if (day.equals("Mon")) {

                                kk.put("ts_mon_evening", timestampnow);

                            } else if (day.equals("Tue")) {

                                kk.put("ts_tue_evening", timestampnow);

                            } else if (day.equals("Wed")) {

                                kk.put("ts_wed_evening", timestampnow);

                            } else if (day.equals("Thu")) {

                                kk.put("ts_thu_evening", timestampnow);

                            } else if (day.equals("Fri")) {

                                kk.put("ts_fri_evening", timestampnow);

                            }
                        }


                }

                if(kk!=null){

                    documentReference.set(kk, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){


                                Toast.makeText(getActivity(),"punch card recorded succesfully",Toast.LENGTH_SHORT).show();
                                //intent to next activity,

                                Intent intent = new Intent(getActivity(), Main_BottomNav_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }else { //task not succesfull, ask user to try time stamp again.

                                Toast.makeText(getActivity(),"punch card failed attempt, please try again",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getActivity(), FingerPrint_LogIn_Final_Activity.class); //can we call same activity,
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                //intent call back original activity, we need to clear all data.

                            }
                        }
                    });
                }
            }
        });

        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // just intent to view data. //do we create new task or instance of activity?

                Toast.makeText(getActivity(),"No data recorded", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), Main_BottomNav_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        FingerPrint_LogIn_Final_Activity.timeFragmentBoolean=true;

    }


}
