package com.example.afinal.fingerPrint_Login.register.register_user_activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.afinal.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class RegUser_Activity extends AppCompatActivity implements View.OnClickListener, RegUserView_Interface, Observer {

    private Button buttonLogin, buttonGetCode;

    private EditText editTextName, editTextPhone, editTextCode;

    private TextView textViewMessage;

    private RegUser_Presenter presenter;

    private boolean inputValid;
    private boolean inputValid_2;

    //if register as admin,
    //just declare, admin name, and admin phone,
    //no need to check employee, instead, check if admin is existed.
    //then jump another activity to set the ssid , bssid etc.

    private String codeFromFirebase;

    private String adminName;
    private String adminPhone;
    private String userName,userPhone;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_user);

        buttonGetCode = findViewById(R.id.regUser_Button_GetCodeID);
        buttonLogin = findViewById(R.id.regUser_Button_LogInID);

        editTextName = findViewById(R.id.regUser_editText_NameID);
        editTextPhone = findViewById(R.id.regUser_editText_PhoneID);
        editTextCode = findViewById(R.id.regUser_editText_CodeID);

        textViewMessage = findViewById(R.id.regUser_textViewID);
        textViewMessage.setText("enter your name and phone");

        Intent intent =getIntent();

        adminName = intent.getStringExtra("admin_name");
        adminPhone = intent.getStringExtra("admin_phone");

        inputValid=false;

        Log.i("checkk UserReg: ", "tt starting");


        presenter = new RegUser_Presenter(this);
        presenter.addObserver(this);




        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                textViewMessage.setText("got credential");
                verifyCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeFromFirebase=s;
                Log.i("checkk UserReg: ", "tt 5 code Receiver: " +codeFromFirebase);

                checkDocResult("received code, enter code now");
            }
        };




    }

    private void verifyCredential(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //then check with firebase.

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //get the reference to admin collection,
                    final CollectionReference cR_ifRegistered = FirebaseFirestore.getInstance().collection("admins_offices");

                    //check array of field "employee_this_admin", if contain UserCheckIn phone number
                    Query query_ifRegistered = cR_ifRegistered.whereArrayContains("employee_this_admin",user.getPhoneNumber());

                    query_ifRegistered.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            //here we dont have to retrieve the number or, get from which document,
                            //if it is exist, means number of document >=1, //though as this code written, or
                            //the way the data is structured, one phone number can belong to only one admin at a time.

                            //we just check. size of document.

                            if(queryDocumentSnapshots.size()>=1){

                                //then here we can log in

                                registrationProcessFireStore();

                            }else {

                                //return as UserCheckIn is not verified by any admin,
                                //sign him out from firebase authentication page.

                                logOutNow();
                            }





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //failure to retrieve data, ask UserCheckIn to try again, log in.

                                logOutNow();

                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                            //canceled to retrieve data, ask UserCheckIn to enter again if consent.

                                logOutNow();

                        }
                    });

                }


                }


        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                    onStartOur();
            }
        });

    }

    private void onStartOur() {
        textViewMessage.setText("please try again");
        onStart();
    }

    private void logOutNow() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){

            FirebaseAuth.getInstance().signOut();
        }

        onStartOur();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deleteObserver(this);
    }

    private void registrationProcessFireStore() {

    //check first if alreadt exist.

        Log.i("checkk UserReg: ", "registrationprocess");
        presenter.checkUserDoc(userName,userPhone,adminName,adminPhone);
        Log.i("checkk UserReg: ", "registrationprocess AFTER");



    }

    @Override
    public void onClick(View v) {

        Log.i("checkk UserReg: ", "tt 1");

        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String code =editTextCode.getText().toString();



        inputValid = presenter.checkInputValid(name,phone);
        inputValid_2 = presenter.checkInputValid(name,phone,code);

        Log.i("checkk UserReg: ", "tt 2 " + name);
        if(inputValid|| inputValid_2) {
            Log.i("checkk UserReg: ", "tt 3");
            userName = name;
            userPhone =phone;
            switch (v.getId()) {

                case R.id.regUser_Button_GetCodeID:

                    Log.i("checkk UserReg: ", "tt 4");

                    //presenter.phonecallBack();
                    getCallBack(phone);

                    break;

                case R.id.regUser_Button_LogInID:

                    Log.i("checkk UserReg: ", "tt 5");

                    checkCredential(code,codeFromFirebase);

                    Log.i("checkk UserReg: ", "tt 6, after check credential compare ourCode: "+ code + " , codeFirebase: "+ codeFromFirebase);


                    break;

            }
        }

        if(!inputValid){
            //handle input false
            textViewMessage.setText("please enter name and phone");
        }else if(!inputValid_2){

            textViewMessage.setText("please enter code");
        }


    }

    private void checkCredential(String code, String codeFromFirebase) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code,codeFromFirebase);

        verifyCredential(credential);

    }

    private void getCallBack(String phone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,
                45,
                TimeUnit.SECONDS,
                RegUser_Activity.this,
                mCallBack);


    }

    @Override
    public void checkDocResult(String status) {
        textViewMessage.setText(status);
    }




    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof RegUser_Presenter){

            checkDocResult("success doc created");
        }

    }
}
