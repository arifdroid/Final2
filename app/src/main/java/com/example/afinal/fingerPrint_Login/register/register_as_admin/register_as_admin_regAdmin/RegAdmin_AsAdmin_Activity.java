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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RegAdmin_AsAdmin_Activity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextCode;
    private Button buttonLogin, buttonGetCode;
    private TextView textViewMessage;
    private Timer timer;
    private int count;

    private String userName;
    private String userPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    private boolean allowCreateAdmin;

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


        //testing animation view

        timer = new Timer();

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

            }
        };


    }

    private void checkPhoneCredential(PhoneAuthCredential phoneAuthCredential) {


        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //check user withing admin document.

                    CollectionReference collectionReference = FirebaseFirestore.getInstance()
                            .collection("all_admins_collections");

                    if(userPhone!=null) {
                        Query query1 = collectionReference.whereEqualTo("phone", userPhone);

                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    if(task.getResult()!=null) {

                                        if (task.getResult().isEmpty()) {

                                            //here we can add document

                                            allowCreateAdmin=true;

                                            Map<String,Object> kk = new HashMap<>();

                                            kk.put("name",userName);
                                            kk.put("phone",userPhone);


                                            DocumentReference reference = FirebaseFirestore.getInstance()
                                                    .collection("all_admins_collections")
                                                    .document(userName+userPhone+"collection");


                                            reference.set(kk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {



                                                }
                                            }).addOnCanceledListener(new OnCanceledListener() {
                                                @Override
                                                public void onCanceled() {


                                                }
                                            });

                                        } else {
                                            //toast not forward

                                        }

                                     }
                                }else {

                                    //toast not forward
                                }

                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {


                            }
                        });




                    }


                }else { //task unsuccessful


                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });


    }


    private void getCallBack(String userPhone) {

      PhoneAuthProvider.getInstance().verifyPhoneNumber(userPhone,
                45,
                TimeUnit.SECONDS,
                RegAdmin_AsAdmin_Activity.this,
                mCallBack);



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


}
