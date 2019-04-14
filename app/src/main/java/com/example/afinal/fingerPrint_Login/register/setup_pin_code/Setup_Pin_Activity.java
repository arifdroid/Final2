package com.example.afinal.fingerPrint_Login.register.setup_pin_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
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
import com.example.afinal.fingerPrint_Login.main_activity_fragment.Main_BottomNav_Activity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Setup_Pin_Activity extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4;
    private TextView textView, textViewName,textViewPhone;

    private Button button;

    private CircleImageView circleImageView;

    private DocumentReference documentReference;
    private StorageReference storageReference;

    Timer timer;
    private String nameHere;
    private String phoneHere;
    private String adminName;
    private String adminPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__pin_);

        editText1 = findViewById(R.id.regUser_editNumber_1_iD);
        editText2 = findViewById(R.id.regUser_editNumber_2_iD);
        editText3 = findViewById(R.id.regUser_editNumber_3_iD);
        editText4 = findViewById(R.id.regUser_editNumber_4_iD);

        //storageReference =



        textView = findViewById(R.id.regUser_pinCode_textViewiD);
        textViewName = findViewById(R.id.regUser_pinCode_textView_NameID);
        textViewPhone = findViewById(R.id.regUser_pinCode_textView_PhoneID);

        circleImageView = findViewById(R.id.regUser_pinCode_circleImageViewiD);

        textView.setText("enter 4 pin password you prefer");


//        SharedPreferences prefs = getSharedPreferences("com.example.finalV8_punchCard", Context.MODE_PRIVATE);
//
//        final String nameHere = prefs.getString("final_User_Name","");
//        final String phoneHere = prefs.getString("final_User_Phone","");
//        String adminName = prefs.getString("final_Admin_Name","");
//        String adminPhone = prefs.getString("final_Admin_Phone","");
      //  String ref = prefs.getString("final_pth_user","");

            Intent intent = getIntent();
            nameHere = intent.getStringExtra("sentUserName");
            phoneHere= intent.getStringExtra("sentUserPhone");
            adminName = intent.getStringExtra("sentAdminName");
            adminPhone = intent.getStringExtra("sentAdminPhone");

        Log.i("finalSharePreDataCheck","Setup_Pin_Activity 2,name: "+ nameHere+ ", phone: "+phoneHere+ ", adminName:"
                +adminName+" , adminPhone: "+adminPhone);

        documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document(adminName+adminPhone+"doc").collection("all_employee_thisAdmin_collection")
                .document(nameHere+phoneHere+"doc");

        Log.i("checkSharedPreferences ", "1");

        // storageReference
        storageReference = FirebaseStorage.getInstance().getReference().child(""+adminName+adminPhone+"doc").child(""+nameHere+phoneHere+"image");
        //FirebaseStorage.getInstance().getReference("" + adminName + adminPhone+"doc").child("" + userPhone + userName +"image")

        textViewName.setText(nameHere);
        textViewPhone.setText(phoneHere);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(storageReference!=null) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.i("checkSharedPreferences ", ", name: "+ nameHere + ", phone: "+ phoneHere );

                            Picasso.with(Setup_Pin_Activity.this).load(uri.toString()).into(circleImageView);
                            if (nameHere != null) {
                                textViewName.setText(nameHere +"updated");

                            }
                            if (phoneHere != null) {


                                textViewName.setText(nameHere +"updated");
                            }

                            timer.cancel();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {

                            Log.i("checkSharedPreferences ", "canceled "+", name: "+ nameHere + ", phone: "+ phoneHere );

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.i("checkSharedPreferences ", "onfailure "+ e.getMessage());

                        }
                    });


                }

            }
        },0,3000);


        button = findViewById(R.id.regUser_pinCode_buttoniD);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText1!=null && editText2!=null && editText3!=null && editText4!=null){

                                //then can start intent here,
                                //if(timer.)
                                timer.cancel();

                                String number1 = editText1.getText().toString();
                                String number2 = editText2.getText().toString();
                                String number3 = editText3.getText().toString();
                                String number4 = editText4.getText().toString();

                                String number = number1+number2+number3+number4+"";

                                //need to store to firestore as well.



                                SharedPreferences prefs = getSharedPreferences(
                                        "com.example.finalV8_punchCard", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("final_User_Pin",number);

                                if(documentReference!=null){

                                    Map<String,Object> map = new HashMap<>();

                                    map.put("custom_pin",number);

                                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {

                                        }
                                    });
                                }


                                Toast.makeText(Setup_Pin_Activity.this,"pin : "+number+ " , is saved", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Setup_Pin_Activity.this, FingerPrint_LogIn_Final_Activity.class);
                                startActivity(intent);
                                finish();



                            }
                else {
                    Toast.makeText(Setup_Pin_Activity.this, "please enter 4 pin prefered password number", Toast.LENGTH_SHORT).show();

                }
            }
        });

        Log.i("checkSharedPreferences ", "1");

        Log.i("checkSharedPreferences ", ", name: "+ nameHere + ", phone: "+ phoneHere + ", name admin: "+ adminName+ ", phone admin: "+ adminPhone );

    }
}
