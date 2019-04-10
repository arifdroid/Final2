package com.example.afinal.fingerPrint_Login.register.setup_pin_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.main_activity_fragment.Main_BottomNav_Activity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Setup_Pin_Activity extends AppCompatActivity {

    private EditText editText1, editText2, editText3, editText4;
    private TextView textView, textViewName,textViewPhone;

    private Button button;

    private CircleImageView circleImageView;

    private DocumentReference documentReference;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__pin_);

        editText1 = findViewById(R.id.regUser_editNumber_1_iD);
        editText2 = findViewById(R.id.regUser_editNumber_2_iD);
        editText3 = findViewById(R.id.regUser_editNumber_3_iD);
        editText4 = findViewById(R.id.regUser_editNumber_4_iD);



        textView = findViewById(R.id.regUser_pinCode_textViewiD);
        textViewName = findViewById(R.id.regUser_pinCode_textView_NameID);
        textViewPhone = findViewById(R.id.regUser_pinCode_textView_PhoneID);

        circleImageView = findViewById(R.id.regUser_pinCode_circleImageViewiD);

        textView.setText("enter 4 pin password you prefer");


        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.finalV8_punchCard", Context.MODE_PRIVATE);

        final String nameHere = prefs.getString("final_User_Name","");
        final String phoneHere = prefs.getString("final_User_Phone","");


        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.with(Setup_Pin_Activity.this).load(uri.toString()).into(circleImageView);
                if(nameHere!=null){
                    textViewName.setText(nameHere);

                }if(phoneHere!=null){

                    textViewPhone.setText(phoneHere);
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        button = findViewById(R.id.regUser_pinCode_buttoniD);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText1!=null ){

                    if(editText2!=null){

                        if(editText3!=null){

                            if(editText4!=null){

                                //then can start intent here,

                                String number1 = editText1.getText().toString();
                                String number2 = editText2.getText().toString();
                                String number3 = editText3.getText().toString();
                                String number4 = editText4.getText().toString();

                                String number = number1+number2+number3+number4+"";

                                SharedPreferences prefs = getSharedPreferences(
                                        "com.example.finalV8_punchCard", Context.MODE_PRIVATE);

                                SharedPreferences.Editor editor = prefs.edit();

                                editor.putString("final_User_Pin",number);

                                Intent intent = new Intent(Setup_Pin_Activity.this, Main_BottomNav_Activity.class);
                                startActivity(intent);
                                finish();



                            }else {

                                Toast.makeText(Setup_Pin_Activity.this,"please enter 4 pin prefered password number", Toast.LENGTH_SHORT).show();
                            }


                        }


                    }
                }
            }
        });
    }
}
