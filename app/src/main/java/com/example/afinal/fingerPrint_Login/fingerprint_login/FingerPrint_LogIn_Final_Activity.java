package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.afinal.fingerPrint_Login.oop.OnServerTime_Interface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.main_activity_fragment.Main_BottomNav_Activity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

//introduce fingerprint id here,
//need observer to watch, if string pull from fragment(sharedpreferences) exist.
//first instantiate to null
//update text view as well.

public class FingerPrint_LogIn_Final_Activity extends AppCompatActivity implements FingerPrintFinal_View_Interface, View.OnClickListener, Observer, OnServerTime_Interface {

    private FloatingActionButton floatButtonGetAction;
    private TextView textView;
    private ImageView imageView;

    private Login_Select_Action_Fragment fragment;

    private FingerPrintFinal_Presenter presenter;

    private boolean dataPulled;

    String nameUser;
    String phoneUser;

    ConstraintLayout backColor;

    //after log in register user data timestamp directly.

    private String globalAdminNameHere = "ariff";
    private String globalAdminPhoneHere= "+60190";

    private FirebaseFirestore instance = FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceRating
            = instance.collection("all_admin_doc_collections")
            .document(globalAdminNameHere+globalAdminPhoneHere+"_doc").collection("all_employee_thisAdmin_collection");


    //cloud function with time value

    private String dayNow;

    private long timeStampthis;

    private OnServerTime_Interface onServerTime_interface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print__log_in__final_);

        onServerTime_interface = new FingerPrint_LogIn_Final_Activity();

        dayNow = null;

        dataPulled = false;

        Log.i("checkFinalFlow : ", " 1 oncreate() fingerprint_main_activity");


        timeStampthis= 0;

        nameUser = "";
        phoneUser = "";

        floatButtonGetAction = findViewById(R.id.logn_Final_floatingActionButtonID);

        floatButtonGetAction.setOnClickListener(this);

        textView = findViewById(R.id.login_final_textViewHereID);
        imageView = findViewById(R.id.login_final_imageViewID);
        textView.setText("click button below to log in");
        backColor = findViewById(R.id.backLayoutColourID);



        presenter = new FingerPrintFinal_Presenter(this);

        presenter.addObserver(this);

        fragment = new Login_Select_Action_Fragment();



        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.i("checkFinalFlow : ", " 2 backstackFragment() fingerprint_main_activity");

                if(nameUser!=null) {

                    if(nameUser!="") {
                        textView.setText("admin detected, fingerprint checkin now with server..");

                        presenter.checkSupportedDevice();

                        textView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                Log.i("checkFinalFlow : ", " 3 backfragment() aftertextchange");

                                if(s.toString().equals("success verified")) {

                                    Log.i("checkFinalFlow : ", " 4 backFragment(), success verified, before server time ");

                                    //check in with time stamp.
                                    //getServerTimeNow(this);
                                    getServerTimeNow(onServerTime_interface);

                                    Log.i("checkFinalFlow : ", " 5 backFragment(), success verified, AFTER server time ");

//
//                                    Intent intent = new Intent(FingerPrint_LogIn_Final_Activity.this, Main_BottomNav_Activity.class);
//                                    startActivity(intent);

                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    finish();

                                }else if(s.toString().equals("waiting")){

                                    Log.i("checkFinalFlow : ", " 6 backFragment(), do nothing waiting for fingerprint ");

                                }else if(s.toString().equals("try again")){

                                    Log.i("checkFinalFlow : ", " 7 backFragment(), try again fingerprint ");

                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"please select admin, try finger again" ,Toast.LENGTH_SHORT).show();


                                }else {

                                    Log.i("checkFinalFlow : ", " 8 backFragment(), fingerprint need try again");

                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"try fingerprint" ,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }else {

                    Log.i("checkFinalFlow : ", " 9 backFragment(), waiting for fingerprint ");

                    textView.setText("waiting");
                }

                Log.i("checkFinalFlow : ", " 10 backFragment(), prolem in flow ");

            }
        });




    }

    public void getServerTimeNow(final OnServerTime_Interface onServerTime_interface){

        FirebaseFunctions.getInstance().getHttpsCallable("getTimeNow")
                .call()
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {

                        Log.i("checkFinalFlow : ", " 11 getServerTimeNow(), before task");

                        if(task.isSuccessful()){

                        //long timm = (long) (task.getResult().getData())/1000;
                        long timm = (long) (task.getResult().getData());

                        if(onServerTime_interface!=null) {

                            onServerTime_interface.onSuccess(timm);

                            Date date = new Date(timm);

                            dayNow  = (date.toString()).substring(0,3);

                            Log.i("checkFinalFlow : ", " 11v1 getServerTimeNow(), getting the time: "+ date);

                            Log.i("checkFinalFlow : ", " 12 getServerTimeNow(), getting the time");

                             Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time now is: "+ date +" day:"+dayNow, Toast.LENGTH_LONG).show();
                        }else {

                            //must handle this. in case server dont return time, how do we insert timestamp?
                            Log.i("checkFinalFlow : ", " 13 getServerTimeNow(), no time recorded from server");

                            //maybe we can get user timestamp,

                           // getBackupTimeFromUser();

                            onServerTime_interface.onFailed();
                        }



                        }else {

                            Log.i("checkFinalFlow : ", " 14 getServerTimeNow(), task failed");


                            if(dayNow==null){

                                getBackupTimeFromUser();
                            }

                    }

                    }
                });

        return;

    }



    @Override
    public void onClick(View v) {

        //reset back status
       // nameUser="";
        nameUser=null;

        backColor.setAlpha(0.05f);

        getSupportFragmentManager().beginTransaction()
        .replace(R.id.frameID,fragment)
        .addToBackStack("")
        .commit();


    }

    @Override
    public void update(Observable o, Object arg) {

    if(o instanceof FingerPrintFinal_Presenter){

        String s = ((FingerPrintFinal_Presenter) o).getFinalStringResult();
        textView.setText(s);
    }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.deleteObserver(this);
    }

    //probably unnecessary
    @Override
    public void onSuccess(long timeStampHere) {

        Log.i("checkkTime : ", " 2 "+ timeStampHere);
        this.timeStampthis =timeStampHere;
        return;
    }

    @Override
    public void onFailed() {

        if(dayNow==null) {
            getBackupTimeFromUser();

        return;
        }
    }

    private void getBackupTimeFromUser() {

        //getting date time from user

        Log.i("checkFinalFlow : ", " 15 getBackupTimeFromUser(), setup our day without server ");

        Date date2 = new Date();

        Log.i("checkFinalFlow : ", " 15v` getBackupTimeFromUser(), setup our day without server :" + date2);

        Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time without server is: "+ date2 +" day:"+dayNow, Toast.LENGTH_LONG).show();

        dayNow  = (date2.toString()).substring(0,3);

        return;



    }
}
