package com.example.afinal.fingerPrint_Login.register;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);

        Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);

        int minute = c.get(Calendar.MINUTE);


        return new TimePickerDialog(getContext(), (TimePickerDialog.OnTimeSetListener) getContext(),hour,minute, DateFormat.is24HourFormat(getContext()));

       // return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)getActivity(),hour,minute, DateFormat.is24HourFormat(getActivity()));
    }
}
