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

import java.util.Observable;
import java.util.Observer;

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



    // >> testing value

    //cloud function with time value

    private long timeStampthis;

    private OnServerTime_Interface onServerTime_interface;

    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print__log_in__final_);

        //initially false, add observer to this.

        FirebaseApp.initializeApp(this);

        dataPulled = false;



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

        Log.i("checkFinal : ", " flow 1");

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.i("checkFinal : ", " flow 2 , backstacklistener, before presenter activity");

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

                                if(s.toString().equals("success verified")) {

                                    //check in with time stamp.
                                    //getServerTimeNow(this);

                                    Log.i("checkkTime : ", " 00 ");


                                    getServerTimeNow(onServerTime_interface);

                                    if(timeStampthis!=0){
                                        Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time now is: "+ timeStampthis, Toast.LENGTH_SHORT).show();
                                    }

                                    Intent intent = new Intent(FingerPrint_LogIn_Final_Activity.this, Main_BottomNav_Activity.class);
                                    startActivity(intent);

                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();

                                }else {
                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"please try fingerprint again" ,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }else {
                    textView.setText("waiting");
                }

                Log.i("checkFinal : ", " flow 3 , backstacklistener, after presenter activity");

            }
        });




    }

    public void getServerTimeNow(final OnServerTime_Interface onServerTime_interface){

        FirebaseFunctions.getInstance().getHttpsCallable("getTime")
                .call()
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {

                        Log.i("checkkTime : ", " 22 before task" );

                        if(task.isSuccessful()){

                        long timm = (long) task.getResult().getData();

                        Log.i("checkkTime : ", " 1 "+ timm);
                        if(onServerTime_interface!=null) {
                            onServerTime_interface.onSuccess(timm);
                        }else {
                            onServerTime_interface.onFailed();
                        }


                    }else {

                        Log.i("checkkTime : ", " 11 task failed?" );
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


    @Override
    public void onSuccess(long timeStampHere) {

        Log.i("checkkTime : ", " 2 "+ timeStampHere);
        this.timeStampthis =timeStampHere;
        return;
    }

    @Override
    public void onFailed() {
 
    }
}
