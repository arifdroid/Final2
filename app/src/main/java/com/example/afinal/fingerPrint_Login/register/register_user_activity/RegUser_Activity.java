package com.example.afinal.fingerPrint_Login.register.register_user_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;
import com.example.afinal.fingerPrint_Login.fingerprint_login.Login_Select_Action_Fragment;
import com.example.afinal.fingerPrint_Login.register.register_with_activity.RegAdmin_Activity;
import com.example.afinal.fingerPrint_Login.register.setup_pin_code.Setup_Pin_Activity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RegUser_Activity extends AppCompatActivity implements View.OnClickListener, RegUserView_Interface, Observer {

    private static final int READ_REQUEST_CODE = 42;
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

    private String statusnow="";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    //Timer to start while checking doc process
    //if doc method not updated, we stop observable, and clear data, ask user to try again.

    Timer timer;

    private CircleImageView circleImageView;

    ////// storage

    private StorageReference storageReference;
    private Uri mImageuri;
    private Timer timer2;
    private int count_adminGlobal;
    private boolean imageSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_user);

        imageSetup =false;

        buttonGetCode = findViewById(R.id.regUser_Button_GetCodeID);
        buttonLogin = findViewById(R.id.regUser_Button_LogInID);

        editTextName = findViewById(R.id.regUser_editText_NameID);
        editTextPhone = findViewById(R.id.regUser_editText_PhoneID);
        editTextCode = findViewById(R.id.regUser_editText_CodeID);

        textViewMessage = findViewById(R.id.regUser_textViewID);
        textViewMessage.setText("enter your name and phone");



        circleImageView = findViewById(R.id.regUser_circlerImageView);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent,READ_REQUEST_CODE);

            }
        });

        Log.i("checkUserReg Flow: ", "[Activity] , 1 ");

        final Intent intent =getIntent();

        adminName = intent.getStringExtra("admin_name"); //pulling data
        adminPhone = intent.getStringExtra("admin_phone");

        Toast.makeText(this, "admin number: "+adminPhone + ", admin name: "+adminName,Toast.LENGTH_SHORT).show();

        inputValid=false;

        timer = new Timer();

        presenter = new RegUser_Presenter(this);
        presenter.addObserver(this);


        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                textViewMessage.setText("got credential");

                Log.i("checkUserReg Flow: ", "[Activity] , 2 , verification completed");

                verifyCredential(phoneAuthCredential);


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                textViewMessage.setText("verification failed");
                Log.i("checkUserReg Flow: ", "[Activity] , 3 , verification failed ");
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeFromFirebase=s;
                Log.i("checkUserReg Flow: ", "[Activity] , 4 , codesent, codeReceived: " +codeFromFirebase);

                //checkDocResult("received code, enter code now");
            }
        };

//        timer2 = new Timer();

//        timer2.schedule(new TimerTask() { .. no need timer if use snapshot listener
//            @Override
//            public void run() {

//                if(adminName!=null && adminPhone!=null && userName!=null && userPhone!=null) {
//                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
//                            .document(adminName + adminPhone + "doc").collection("all_employee_thisAdmin_collection")
//                            .document(userName + userPhone + "doc");

//                    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections").
//                            document(adminName + adminPhone + "doc").collection("all_employee_thisAdmin_collection");
//
//                    Query query1 = collectionReference.whereEqualTo("name",userName);   //check if it is written.
//
//                    query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
//
//                        @Override
//                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
//
//                            Log.i("checkSnapShotListener", "1 start");
//                            if(e!=null){
//
//                                return;
//                            }
//
//                            if(queryDocumentSnapshots!=null ){
//                               int size =  queryDocumentSnapshots.size();
//                                  Log.i("checkSnapShotListener", "2 not null");
////                                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
////
////                                }
////
//                                if (size>=1){
//
//                                    //move to next activity?
//
//                                    Log.i("checkSnapShotListener", " 3 exist");
//
//                                    Intent intent1 = new Intent(RegUser_Activity.this,Setup_Pin_Activity.class);
//                                    startActivity(intent1);
//                                    finish();
//                                }
//
//                            }
//                        }
//                    });
// //               }

//            }
//        },0,1500);  //every 1.5 seconds check doc created or not.

    }



    //handle image


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==42 && resultCode== Activity.RESULT_OK){

            mImageuri = null;

            if(data!=null){

                mImageuri = data.getData();

                imageSetup =true;

                showImage(mImageuri);
            }

        }
    }

    private void showImage(Uri uri) {

        circleImageView.setImageURI(uri);


    }

    private void verifyCredential(PhoneAuthCredential phoneAuthCredential) {


        statusnow= "wait..";
        textViewMessage.setText(statusnow);

        Log.i("checkUserReg Flow: ", "[Activity] , 5 , getting credential process");

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    textViewMessage.setText("credential verified, wait..");

                    //then check with firebase.
                    Log.i("checkUserReg Flow: ", "[Activity] , 6 , task successfull");

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //get the reference to admin collection,
                    final CollectionReference cR_ifRegistered = FirebaseFirestore.getInstance().collection("admins_offices");

                    //check array of field "employee_this_admin", if contain UserCheckIn phone number
                    Query query_ifRegistered = cR_ifRegistered.whereArrayContains("employee_this_admin",user.getPhoneNumber());

                    query_ifRegistered.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()) {

                                Log.i("checkUserReg Flow: ", "[Activity] , 7 , ");

                                int documentSnapshotSize = task.getResult().getDocuments().size();

                                if (documentSnapshotSize == 1) {

                                    //then here we can log in

                                    registrationProcessFireStore();

                                    Log.i("checkUserReg Flow: ", "[Activity] , 8 , go to creating document now ");


                                } else {

                                    //return as UserCheckIn is not verified by any admin,
                                    //sign him out from firebase authentication page.
                                    //or somehow he already registered to other admin // this is supposed to be admin functions.

                                    Log.i("checkUserReg Flow: ", "[Activity] , 9 ,fail if  ");


                                    logOutNow();
                                }

                            }
                            else {
                                Log.i("checkUserReg Flow: ", "[Activity] , 10 , task fail ");

                                logOutNow();
                            }
                        }
                    });

                }


                }


        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                Log.i("checkUserReg Flow: ", "[Activity] , 11 ,canceled  ");


                logOutNow();
            }
        });

    }

    private void onStartOur() {

        Log.i("checkUserReg Flow: ", "[Activity] , 12 ,stop  ");


        Toast.makeText(this,"please try register again", Toast.LENGTH_SHORT).show();
        textViewMessage.setText("press back, and try again");
        //onStart();

        Intent intent = new Intent(RegUser_Activity.this, RegAdmin_Activity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear other activity on top of old instance of the activity,
        startActivity(intent);
        //finish();
    }

    private void logOutNow() {

        Log.i("checkUserReg Flow: ", "[Activity] , 13 , logoutNow  ");

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

        Log.i("checkUserReg Flow: ", "[Activity] , 14 ,go to presenter  ");

        presenter.checkUserDoc(userName,userPhone,adminName,adminPhone);

        Log.i("checkUserReg Flow: ", "[Activity] , 15 ,back from presenter  ");

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
            userPhone = phone;

            if(imageSetup){

            switch (v.getId()) {

                case R.id.regUser_Button_GetCodeID:

                    //we can create boolean check, if user is maxed here.

                    if (checkNumberOfAdminRegisteredTo()) {


                        textViewMessage.setText("getting code..");

                        Log.i("checkk UserReg: ", "tt 4");

                        //presenter.phonecallBack();
                        getCallBack(phone);

                    } else {

                        Intent intentHereGoBack_MaxAlready = new Intent(RegUser_Activity.this, FingerPrint_LogIn_Final_Activity.class);
                        startActivity(intentHereGoBack_MaxAlready);
                        finish();
                    }

                    break;

                case R.id.regUser_Button_LogInID:

                    Log.i("checkk UserReg: ", "tt 5");

                    checkCredential(code, codeFromFirebase);

                    Log.i("checkk UserReg: ", "tt 6, after check credential compare ourCode: " + code + " , codeFirebase: " + codeFromFirebase);


                    break;

            }

        }else {

                textViewMessage.setText("please click picture, and set profile picture");
            }
        }

        if(!inputValid){
            //handle input false
            textViewMessage.setText("please enter name and phone");
        }else if(!inputValid_2){

            textViewMessage.setText("please enter code");
        }


    }

    //check if max user is registered.
    private boolean checkNumberOfAdminRegisteredTo() {

        File f_MainPool = new File("/data/data/com.example.afinal/shared_prefs/com.example.finalV8_punchCard.MAIN_POOL.xml");

        if(f_MainPool.exists()){ //if exist, should

            //if exist, is already user to other admin, so counter should read 1.
            //read count first if 2 or higher, send error.

            SharedPreferences prefs_Main_Pool = this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

            String count_admin = prefs_Main_Pool.getString("count_admin","");

            if(Integer.valueOf(count_admin)==1){


                count_adminGlobal =1;

                return true; //



            }if((Integer.valueOf(count_admin)>=2)){

                count_adminGlobal =2;
                return false;
                //this should be error. somehow, should not happen to have register to 2 admin.


            }else {

                return false;// somehow something weird data pulled
            }


        }else {

            count_adminGlobal =0;
            return true; //not exist yet, so can add more user. //another user



        }


    }

    private void checkCredential(String code, String codeFromFirebase) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code,codeFromFirebase);

        Log.i("checkUserReg Flow: ", "[Activity] , 16 ,check credential  ");

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
        //textViewMessage.setText(status);

        //here we create, but what if constant fail.
        //after 60 seconds, observable do not return update. we need to delete all operation,

        Log.i("checkUserReg Flow: ", "[Activity] , 17 ,checkDocResutl, result suppose to create ");

        if(status.equals("doc created")){

            //creating document here.
            //then after finish, move to next.

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                    .document(adminName+adminPhone+"doc").collection("all_employee_thisAdmin_collection")
                    .document(userName+userPhone+"doc");

            Map<Object,String> userprofile_data = new HashMap<>();

            userprofile_data.put("name",userName);
            userprofile_data.put("phone",userPhone);
            userprofile_data.put("rating","2.5");
            userprofile_data.put("image",documentReference.toString());
            //userprofile_data.put("");

            textViewMessage.setText("success.. setting up account");

            //documentReference

            documentReference.set(userprofile_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Log.i("documentSet ", "1");


                        if(mImageuri!=null) {

                            storageReference = FirebaseStorage.getInstance().getReference("" + adminName + adminPhone+"doc").child("" + userName + userPhone +"image");
                            storageReference.putFile(mImageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if(task.isSuccessful()){

                                        Log.i("checkImageUploaded", "1");

                                        Log.i("checkSharedPreferences ", "before image upload success");

                                    }else {
                                        //task not successful

                                        Log.i("checkImageUploaded", "2");

                                        Log.i("checkSharedPreferences ", "before image upload task fail");

                                    }

                                }
                            }).addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {

                                    Log.i("checkImageUploaded", "3");
                                }
                            });

                        }

                        //should we check,

                        //here we saved all in sharedpreferences //create 4 pin id.

                        //check sharedpref into the right admin. ..first check pool, where to create.



                        if(count_adminGlobal==0){ //here we can create the whole.

                            //create 2 file2,

                            SharedPreferences prefs_Main_Pool = RegUser_Activity.this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();

                            editor_Main_Pool.putString("count_admin","1");
                            editor_Main_Pool.putString("final_Admin_Phone_MainPool",userPhone);

                            editor_Main_Pool.commit();

                            //here for direct


                            SharedPreferences prefs = getSharedPreferences(
                                    "com.example.finalV8_punchCard."+adminPhone, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = prefs.edit(); // we need to know, which preferences belong to which admin,
                            //if user registered to another admin.


                            editor.putString("final_User_Name",userName);
                            editor.putString("final_User_Phone",userPhone);
                            editor.putString("final_Admin_Phone",adminPhone);
                            editor.putString("final_Admin_Name", adminName);

                            editor.putString("final_User_Picture", storageReference.toString());

                            Log.i("finalSharePreDataCheck","Reg_User_Activity 1,name: "+ userName+ ", phone: "+userPhone + ", adminName:"
                                    +adminName+" , adminPhone: "+adminPhone);


                            //FingerPrint_LogIn_Final_Activity.userCount++;

                            editor.commit();

                        }if(count_adminGlobal==1){

                            //create file

                            SharedPreferences prefs_Main_Pool = RegUser_Activity.this.getSharedPreferences("com.example.finalV8_punchCard.MAIN_POOL", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor_Main_Pool = prefs_Main_Pool.edit();

                            editor_Main_Pool.putString("count_admin","2");
                            editor_Main_Pool.putString("final_Admin_Phone_MainPool_2",userPhone);

                            editor_Main_Pool.commit();


                            SharedPreferences prefs = getSharedPreferences(
                                    "com.example.finalV8_punchCard."+adminPhone, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = prefs.edit(); // we need to know, which preferences belong to which admin,
                            //if user registered to another admin.


                            editor.putString("final_User_Name",userName);
                            editor.putString("final_User_Phone",userPhone);
                            editor.putString("final_Admin_Phone",adminPhone);
                            editor.putString("final_Admin_Name", adminName);

                            editor.putString("final_User_Picture", storageReference.toString());

                            Log.i("finalSharePreDataCheck","Reg_User_Activity 1,name: "+ userName+ ", phone: "+userPhone + ", adminName:"
                                    +adminName+" , adminPhone: "+adminPhone);


                            //FingerPrint_LogIn_Final_Activity.userCount++;

                            editor.commit();



                        }if(count_adminGlobal>=2){

                            //cancel create, should not create here.,, not create anything.

                        }



                        //we skipped this?

                        Toast.makeText(RegUser_Activity.this,"user succesfully created", Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(RegUser_Activity.this, Setup_Pin_Activity.class);
//
//                        startActivity(intent);

                        //intent.addFlags()

                     //   finish();


                    }else {

                        Log.i("documentSet ", "2, task failed");


                    }
                }


            });

           //add listener

            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections").
                    document(adminName + adminPhone + "doc").collection("all_employee_thisAdmin_collection");

            Query query1 = collectionReference.whereEqualTo("name",userName);   //check if it is written.

            query1.addSnapshotListener(new EventListener<QuerySnapshot>() {

                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                    Log.i("checkSnapShotListener", "1 start");
                    if(e!=null){

                        return;
                    }

                    if(queryDocumentSnapshots!=null ){
                        int size =  queryDocumentSnapshots.size();
                        Log.i("checkSnapShotListener", "2 not null");
//                                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
//
//                                }
//
                        if (size>=1){

                            //move to next activity?

                            Log.i("checkSnapShotListener", " 3 exist");

                            Intent intent1 = new Intent(RegUser_Activity.this,Setup_Pin_Activity.class);
                            intent1.putExtra("sentUserName", userName);
                            intent1.putExtra("sentUserPhone", userPhone);
                            intent1.putExtra("sentAdminName", adminName);
                            intent1.putExtra("sentAdminPhone", adminPhone);

                            //intent1.addFlags(Intent.)
                            startActivity(intent1);
                            //finish();

                            //need to disable back button manually.
                        }

                    }
                }
            });
            //               }



        }

        if(status.equals("please contact admin")){

            //this will be called, if false return from check document,
            //document existed
            textViewMessage.setText("registration failed");
            logOutNow();
        }


    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof RegUser_Presenter){

            Log.i("checkUserReg Flow: ", "[Activity] , 18 ,update , o is: "+((RegUser_Presenter) o).getReturnStatus());


            //String resultHere = ((RegUser_Presenter) o).getReturnStatus();

            Boolean resultBoolean = ((RegUser_Presenter) o).getFinally();

            if(resultBoolean){
            //if(resultHere.equals("doc created")){

                Log.i("checkUserReg Flow: ", "[Activity] , 19 ,update doc created ");


                checkDocResult("doc created");
            }

            else {

            //if(resultHere.equals("please contact admin")){

                Log.i("checkUserReg Flow: ", "[Activity] , 20 ,contact admin , fail ");

                checkDocResult("please contact admin");
            }
            //


        }

        return;

    }
}
