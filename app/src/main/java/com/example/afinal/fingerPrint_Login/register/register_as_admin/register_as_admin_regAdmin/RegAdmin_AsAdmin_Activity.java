package com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile.RegAdmin_asAdmin_Profile_Activity;
import com.example.afinal.fingerPrint_Login.register.register_user_activity.RegUser_Activity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RegAdmin_AsAdmin_Activity extends AppCompatActivity implements Observer {

    private EditText editTextName, editTextPhone, editTextCode;
    private Button buttonLogin, buttonGetCode;

    private ConstraintLayout constraintLayout;

    private TextView textViewMessage;
    private Timer timer;
    private int count;

    private String userName;
    private String userPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private boolean allowCreateAdmin;
    private Presenter_RegAdmin_AsAdmin_Activity presenter;
    private String codeFromFirebase;
    private String codeUserAdminEnter;
    private PhoneAuthCredential credenttial;
    private int countForAnimateButton;
    private int copyadminCreated;

    //testing phase

    private Button buttonTestHere;

    // we try pull if there is any data in shared preferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin__as_admin_);

        copyadminCreated=0;
        countForAnimateButton=0;
        credenttial=null;
        editTextName = findViewById(R.id.regAdmin_asAdmin_editText_NameiD);
        editTextPhone = findViewById(R.id.regAdmin_asAdmin_editTextPhone);
        editTextCode = findViewById(R.id.regAdmin_asAdmin_editText_CodeiD);
        constraintLayout = findViewById(R.id.regAdmin_constraint);

        buttonTestHere = findViewById(R.id.buttonaksdkasjklda);

        allowCreateAdmin=false;

        buttonGetCode = findViewById(R.id.regAdmin_asAdmin_button_getCodeiD);
        buttonLogin = findViewById(R.id.regAdmin_asAdmin_buttonLoginiD);

        textViewMessage = findViewById(R.id.regAdmin_asAdmin_textViewMessageiD);

        presenter = new Presenter_RegAdmin_AsAdmin_Activity(this);

        presenter.addObserver(this);


        //here we saved all in sharedpreferences //create 4 pin id.

        SharedPreferences prefs = getSharedPreferences(
                "com.example.finalV8_punchCard", Context.MODE_PRIVATE);

        // https://stackoverflow.com/questions/23635644/how-can-i-view-the-shared-preferences-file-using-android-studio

        File f = new File("/data/data/com.example.afinal/shared_prefs/com.example.finalV8_punchCard.xml");
        //File f2 = new File(Context.);


        if(f.exists()){

            SharedPreferences prefse = getSharedPreferences(
                    "com.example.finalV8_punchCard", Context.MODE_PRIVATE);

           // SharedPreferences.Editor editor = prefse.edit();

            String nameCheck = prefs.getString("final_User_Name","");

            Toast.makeText(this,"shared prefs exist, name: "+ nameCheck,Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"shared prefs NOT exist",Toast.LENGTH_LONG).show();
        }

        //if exist, put label on.



//        SharedPreferences.Editor editor = prefs.edit(); // we need to know, which preferences belong to which admin,
//        //if user registered to another admin.
//
//
//        editor.putString("final_User_Name",userName);
//        editor.putString("final_User_Phone",userPhone);
//        editor.putString("final_Admin_Phone",adminPhone);
//        editor.putString("final_Admin_Name", adminName);
//
//        editor.putString("final_User_Picture", storageReference.toString());


        buttonTestHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = editTextName.getText().toString();
                String userPhone = editTextPhone.getText().toString();

                Intent intent = new Intent(RegAdmin_AsAdmin_Activity.this,RegAdmin_asAdmin_Profile_Activity.class);
                intent.putExtra("adminName_asAdmin",userName);
                intent.putExtra("adminPhone_asAdmin",userPhone);

                startActivity(intent);
            }
        });


        //testing animation view

        timer = new Timer();

        count=0;


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                codeUserAdminEnter = editTextCode.getText().toString();
                if(checkInput(codeUserAdminEnter)){

                    //send method verify

                    checkCredential(codeUserAdminEnter,codeFromFirebase);

                }else {



                }

            }
        });

        buttonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewMessage.setText("check input..");
                userName =editTextName.getText().toString();
                userPhone =editTextPhone.getText().toString();

                if(checkInput(userName,userPhone)){
                    //if input true
                    textViewMessage.setText("getting phone verification..");

                    getCallBack(userPhone);

                }else {

                    //if input faulty


                }



            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codeEntered = editTextCode.getText().toString();

            if(checkInput(codeEntered)){

                checkCredential(codeEntered,codeFromFirebase);

            }else {

                Toast.makeText(RegAdmin_AsAdmin_Activity.this,"please enter code received",Toast.LENGTH_SHORT).show();
            }
            }
        });

        //phone auth call back

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(final PhoneAuthCredential phoneAuthCredential) {

                Toast.makeText(RegAdmin_AsAdmin_Activity.this,"verified, please wait, attempt automatically...",Toast.LENGTH_LONG).show();

                //checkPhoneCredential(phoneAuthCredential);

                textViewMessage.setText("phone verified, try automatically...");


                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        countForAnimateButton++;
                        //here after got code, we animate button, to visible and move
                        //after 1 second, after 3 secound

                        if(countForAnimateButton==10 && copyadminCreated!=2){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editTextCode.setHint("please enter code here");
                                }
                            });


                        }

                        if(countForAnimateButton==1){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    // https://www.youtube.com/watch?v=IECLEh98HnE
                                    // https://stackoverflow.com/questions/30535304/android-pop-in-animation
                                    // https://stackoverflow.com/questions/9448732/shaking-wobble-view-animation-in-android



                                    ObjectAnimator animator = ObjectAnimator.ofFloat(buttonGetCode,"translationY",-70f);
                                    buttonGetCode.animate()
                                            .alpha(0f)
                                            .setDuration(200)
                                            .setListener(null);
                                    animator.setDuration(200);
                                    animator.start();


                                    Animation fadeIn = AnimationUtils.loadAnimation(RegAdmin_AsAdmin_Activity.this,R.anim.fadein);
                                    buttonLogin.startAnimation(fadeIn);


                                    ObjectAnimator moveUpGroup = ObjectAnimator.ofFloat(constraintLayout,"translationY",-180f);
                                    moveUpGroup.setDuration(200);
                                    moveUpGroup.start();
                                    editTextCode.startAnimation(fadeIn);
                                    //editTextCode.setText("try to log in automatically..");
                                    editTextCode.setHint("auto registering attempt..");

                                    checkPhoneCredential(phoneAuthCredential);
//                                buttonLogin.animate()
//                                        .alpha(1f)
//                                        .setDuration(400)
//                                        .setListener(null);



                                }
                            });

                        }

                    }
                },500,1000);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(RegAdmin_AsAdmin_Activity.this,"verification fail: ",Toast.LENGTH_LONG).show();
                Log.i("fail to verify","1 : "+e.getMessage());

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codeFromFirebase=s;

            }
        };

        //problem is when this admin want to register as user for other admin.
        //so we need to put label, then pull according to corresponding lable.

        //two tag,
        // tag ONE , mean user was admin, and added user under its tree
        // tag TWO , mean user was user , and want to become admin.
        //must be done at first phase.







    }

    private void checkCredential(String codeUserAdminEnter, String codeFromFirebase) {

        presenter.getCredentialWithUpdates(codeUserAdminEnter,codeFromFirebase);


    }

    private void checkPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        if(userName!=null&&userPhone!=null) {
            presenter.checkCredentialWithUpdates(phoneAuthCredential, userName, userPhone);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deleteObserver(this);
    }

    private void getCallBack(String userPhone) {

      PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhone,
                60,
                TimeUnit.SECONDS,
                RegAdmin_AsAdmin_Activity.this,
                mCallBack);



    }

    private boolean checkInput(String code){
        if(code.equals("")|| code==null){

            return false;
        }
        else {


            return true;
        }


    }

    private boolean checkInput(String userName,String userPhone) {

        if(userName.equals("")||userPhone.equals("")||userName==null||userPhone==null){


            return false;
        }
        else {

            int countInput = userName.length();

            if(countInput<=3){

                Toast.makeText(this,"please enter name more than 3 characters",Toast.LENGTH_SHORT).show();

                return false;
            }

            //normalize phone number and name
            String uncheckName = userName;
            String uncheckPhone = userPhone;

            userName = userName.trim();
            userPhone = userPhone.replace(" ","");
            userPhone = userPhone.replace("-","");
            if(userPhone.charAt(0)==0){
                userPhone="+6"+userPhone; //test if input is 017
            }
            if(userPhone.charAt(0)==6){
                userPhone="+"+userPhone;
            }

            this.userName =userName;
            this.userPhone=userPhone;
            return true;
        }
    }


    @Override
    public void update(Observable observable, Object o) {
        Log.i("checkFlowAsAdmin", "5");

        if(observable instanceof Presenter_RegAdmin_AsAdmin_Activity){

        Log.i("checkFlowAsAdmin", "4");
        // 0 = initial , 1 = success , 2 = fail
        int adminDocumentCreated = ((Presenter_RegAdmin_AsAdmin_Activity) observable).getIfDocumentCreated();
        copyadminCreated=adminDocumentCreated;
        if(adminDocumentCreated==1){

            Log.i("checkFlowAsAdmin", "3");

            timer.cancel();
            Toast.makeText(this, "successfully registered", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegAdmin_AsAdmin_Activity.this,RegAdmin_asAdmin_Profile_Activity.class);
            intent.putExtra("adminName_asAdmin",userName);
            intent.putExtra("adminPhone_asAdmin",userPhone);
            //intent.addFlags(Intent.)

            startActivity(intent);




        }
        if(adminDocumentCreated==0){

            timer.cancel();
            Log.i("checkFlowAsAdmin", "2");
            textViewMessage.setText("please try again");
            Toast.makeText(this,"please try again",Toast.LENGTH_SHORT).show();


        }
        if(adminDocumentCreated==2){

            timer.cancel();
            textViewMessage.setText("please try again");
            //here can intent to next.

            Log.i("checkFlowAsAdmin", "1");
            Toast.makeText(this,"please try again",Toast.LENGTH_SHORT).show();




        }
        if(adminDocumentCreated==3){ //already exist.

            textViewMessage.setText("please try again");
            Toast.makeText(this,"phone already registered",Toast.LENGTH_SHORT).show();

        }

        credenttial = ((Presenter_RegAdmin_AsAdmin_Activity) observable).getCredential();

        if(credenttial!=null){

            checkPhoneCredential(credenttial);


        }

    }



    }
}
