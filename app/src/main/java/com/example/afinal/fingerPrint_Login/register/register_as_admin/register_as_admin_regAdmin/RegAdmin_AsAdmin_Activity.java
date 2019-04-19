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
import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;
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
import java.util.Set;
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
    private String admin_Label;

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
                if(checkNumberOfAdminRegisteredTo()) {
                    userName = editTextName.getText().toString();
                    userPhone = editTextPhone.getText().toString();

                    textViewMessage.setText("check input..");
                    if (checkInput(userName, userPhone)) {
                        //if input true
                        textViewMessage.setText("getting phone verification..");

                        getCallBack(userPhone);

                    } else {

                        //if input faulty


                    }


                }else { //if FALSE,

                    //intent go back, disallow register more than one user//or admin, since registering admin, means registering user.

                    Intent intentBack = new Intent(RegAdmin_AsAdmin_Activity.this, FingerPrint_LogIn_Final_Activity.class);
                    startActivity(intentBack);
                    finish();
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

    //check shared preferences, if already 2, dont allow to register to admin

    private boolean checkNumberOfAdminRegisteredTo(){

        File f_MainPool = new File("/data/data/com.example.afinal/shared_prefs/com.example.finalV8_punchCard.MAIN_POOL.xml");

        if(f_MainPool.exists()){ //if exist, should

            //if exist, is already user to other admin, so counter should read 1.
            //read count first if 2 or higher, send error.

            SharedPreferences prefs_Main_Pool = this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

            String count_admin = prefs_Main_Pool.getString("count_admin","");

            if(Integer.valueOf(count_admin)==1){


                return true; //return can create new admmin, since within 2 user max.

//                SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();
//
//                editor_Main_Pool.putString("count_admin","2"); //here we update the count. //we could just check earlier.
//                editor_Main_Pool.putString("final_Admin_Phone_MainPool_2",userPhone);
//                //editor_Main_Pool.putString("")
//
//                editor_Main_Pool.commit();


            }if((Integer.valueOf(count_admin)>=2)){


                return false;
                //this should be error. somehow, should not happen to have register to 2 admin.


            }else {



                return false;// somehow something weird data pulled
            }






        }else {


            return true; //not exist yet, so can add more user. //another user

            //here we create, since it is not exist yet.
//
//            SharedPreferences prefs_Main_Pool = this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);
//
//            SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();
//
//            editor_Main_Pool.putString("count_admin","1");
//            editor_Main_Pool.putString("final_Admin_Phone_MainPool",userPhone);
//            //editor_Main_Pool.putString("")
//
//            editor_Main_Pool.commit();

        }
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

            //setup shared preferences.


//            SharedPreferences prefs = getSharedPreferences(
//                    "com.example.finalV8_punchCard"+userPhone, Context.MODE_PRIVATE);

            // https://stackoverflow.com/questions/23635644/how-can-i-view-the-shared-preferences-file-using-android-studio

            //first check with pool, need to update or create?

            File f_MainPool = new File("/data/data/com.example.afinal/shared_prefs/com.example.finalV8_punchCard.MAIN_POOL.xml");

            if(f_MainPool.exists()){ //if exist, should

                //if exist, is already user to other admin, so counter should read 1.
                //read count first if 2 or higher, send error.

                SharedPreferences prefs_Main_Pool = this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

                String count_admin = prefs_Main_Pool.getString("count_admin","");

                if(Integer.valueOf(count_admin)==1){

                    SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();

                    editor_Main_Pool.putString("count_admin","2"); //here we update the count. //we could just check earlier.
                    editor_Main_Pool.putString("final_Admin_Phone_MainPool_2",userPhone);
                    //editor_Main_Pool.putString("")

                    editor_Main_Pool.commit();


                }if((Integer.valueOf(count_admin)>=2)){

                    //this should be error. somehow, should not happen to have register to 2 admin.


                }


            }else {

                //here we create, since it is not exist yet.

                SharedPreferences prefs_Main_Pool = this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();

                editor_Main_Pool.putString("count_admin","1");
                editor_Main_Pool.putString("final_Admin_Phone_MainPool",userPhone);
                //editor_Main_Pool.putString("final_Admin_Name_MainPool",userPhone);

                //editor_Main_Pool.putString("")

                editor_Main_Pool.commit();

            }


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
