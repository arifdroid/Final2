package com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin;

import android.animation.ObjectAnimator;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin__as_admin_);

        editTextName = findViewById(R.id.regAdmin_asAdmin_editText_NameiD);
        editTextPhone = findViewById(R.id.regAdmin_asAdmin_editTextPhone);
        editTextCode = findViewById(R.id.regAdmin_asAdmin_editText_CodeiD);

        allowCreateAdmin=false;

        buttonGetCode = findViewById(R.id.regAdmin_asAdmin_button_getCodeiD);
        buttonLogin = findViewById(R.id.regAdmin_asAdmin_buttonLoginiD);

        textViewMessage = findViewById(R.id.regAdmin_asAdmin_textViewMessageiD);

        presenter = new Presenter_RegAdmin_AsAdmin_Activity(this);

        //testing animation view

//        timer = new Timer();

        count=0;
//
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                //move button here
//
//                count++;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Toast.makeText(RegAdmin_AsAdmin_Activity.this,"test animation"+count,Toast.LENGTH_SHORT).show();
//                        ObjectAnimator animator = ObjectAnimator.ofFloat(buttonGetCode,"translationX",-70f);
//                        animator.setDuration(1500);
//                        animator.start();
//                       // timer.cancel();
//                    }
//                });
//
//
//            }
//        },0,3000);
//

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

                userName =editTextName.getText().toString();
                userPhone =editTextPhone.getText().toString();

                if(checkInput(userName,userPhone)){
                    //if input true

                    getCallBack(userPhone);

                }else {

                    //if input faulty


                }



            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegAdmin_AsAdmin_Activity.this,RegAdmin_asAdmin_Profile_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //phone auth call back

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                checkPhoneCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codeFromFirebase=s;

            }
        };


    }

    private void checkCredential(String codeUserAdminEnter, String codeFromFirebase) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeUserAdminEnter,codeFromFirebase);
        if(credential!=null) {
            checkPhoneCredential(credential);
        }

    }

    private void checkPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        if(userName!=null&&userPhone!=null) {
            presenter.checkCredentialWithUpdates(phoneAuthCredential, userName, userPhone);
        }


    }


    private void getCallBack(String userPhone) {

      PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhone,
                45,
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

    if(o instanceof Presenter_RegAdmin_AsAdmin_Activity){


        // 0 = initial , 1 = success , 2 = fail
        int adminDocumentCreated = ((Presenter_RegAdmin_AsAdmin_Activity) o).getIfDocumentCreated();

        if(adminDocumentCreated==1){


        }
        if(adminDocumentCreated==0){

        }
        if(adminDocumentCreated==2){

            //here can intent to next.

        }
        if(adminDocumentCreated==3){ //already exist.


        }


    }



    }
}
