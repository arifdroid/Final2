package com.example.afinal.fingerPrint_Login.register;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.afinal.fingerPrint_Login.PassResult;
import com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile.RecyclerView_Admin_Profile_Adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

//
//    private Context mContext;
//    public TimePickerFragment(Context mContext) {
//        this.mContext =mContext;

//
//    public void getContext(){
//
//
//    }
//    }

    //setup listener
//    private PassResult passResult;
//
//    public void setPassResult(PassResult passResult){
//        this.passResult = passResult;
//    }

    //setup map listener
//
//    private PassResultMap passResultMap;
//
//    public void setPassResultMap(PassResultMap passResultMap){
//        this.passResultMap = passResultMap;
//    }

//
////
//    private String hour;
//    private String minute;

    private static int label;

    @SuppressLint("ValidFragment")
    public TimePickerFragment(int k) {
        label=k;
    }
//
//    public void setLabel(int label) {
//        this.label = label;
//    }

    public static int getLabel() {
        return label;
    }





    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        //this.label = setLabel(label);

        //getLabel();

        Log.i("checkTime", "createDialog, label now: "+ label ); //i think label need to be set here, problem is, on cre
        Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);

        int minute = c.get(Calendar.MINUTE);
//
//        this.hour = String.valueOf(hour);
//        this.minute = String.valueOf(minute);
//

        return new TimePickerDialog(getContext(), (TimePickerDialog.OnTimeSetListener) getContext(),hour,minute, DateFormat.is24HourFormat(getContext()));


    }


//    public void setHourMinute(int hour, int minute) {
//        this.hour = String.valueOf(hour);
//        this.minute = String.valueOf(minute);
//
//        if(this.hour!=null) {
//
//            if(this.minute==null){
//                this.minute="00";
//            }
//
//
//            Map<String, String> kk = new HashMap<>();
//            kk.put("hour", this.hour);
//            kk.put("minute",this.minute);
//
//            if(passResultMap!=null){
//
//                passResultMap.setPassResultMap(kk);
//            }
////
////            if (this.hour != null) {
////
////                if (passResultMap != null) {
////                    passResultMap.setPassResultMap(this.hour);
////                }
////            }
//        }
//
//        return;
//    }







}
