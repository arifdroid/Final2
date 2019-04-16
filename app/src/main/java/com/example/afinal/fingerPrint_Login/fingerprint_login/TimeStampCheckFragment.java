package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.afinal.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//https://guides.codepath.com/android/using-dialogfragment

public class TimeStampCheckFragment extends DialogFragment {

    private TextView textViewAsk;
    private Button buttonCheckin, buttonCheckOut;

    public TimeStampCheckFragment() {
    }


    public static TimeStampCheckFragment newInstance(String day){
        TimeStampCheckFragment timeStampCheckFragment = new TimeStampCheckFragment();
        Bundle args = new Bundle();
        args.putString("question",day);
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

        textViewAsk = view.findViewById(R.id.textView2);
        buttonCheckin = view.findViewById(R.id.cardview_PunchIntiD);
        buttonCheckOut = view.findViewById(R.id.cardview_PunchOutiD);

        String question = getArguments().getString("question", "Enter Name?");
        getDialog().setTitle(question);
//a
        textViewAsk.setText(question);
        textViewAsk.requestFocus(); //what is this?

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);



    }
}
