package com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile.RegAdmin_asAdmin_Profile_Activity;

public class RegAdmin_AsAdmin_Activity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextCode;

    private Button buttonLogin, buttonGetCode;

    private TextView textViewMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin__as_admin_);

        editTextName = findViewById(R.id.regAdmin_asAdmin_editText_NameiD);
        editTextPhone = findViewById(R.id.regAdmin_asAdmin_editTextPhone);
        editTextCode = findViewById(R.id.regAdmin_asAdmin_editText_CodeiD);

        buttonGetCode = findViewById(R.id.regAdmin_asAdmin_button_getCodeiD);
        buttonLogin = findViewById(R.id.regAdmin_asAdmin_buttonLoginiD);

        textViewMessage = findViewById(R.id.regAdmin_asAdmin_textViewMessageiD);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegAdmin_AsAdmin_Activity.this,RegAdmin_asAdmin_Profile_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}
