package com.example.afinal.fingerPrint_Login.test_mvp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.afinal.R;

public class Test_Activity extends AppCompatActivity implements View.OnClickListener, ThisView{

    // https://riptutorial.com/android/example/29784/simple-login-example-in-mvp

    private Button buttonLogin, buttonClear;
    private EditText editTextName, editTextPassword;
    private TextView textView;

    private Test_Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        buttonLogin = findViewById(R.id.test_buttonLogInId);
        buttonClear = findViewById(R.id.test_buttonClearId);

        editTextName = findViewById(R.id.test_editTextNameid);
        editTextPassword = findViewById(R.id.test_editTextPasswordid);
        textView = findViewById(R.id.test_buttonTextViewId);

        presenter = new Test_Presenter(this);


        buttonClear.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.test_buttonClearId:

                break;

            case R.id.test_buttonLogInId:

                break;


        }

    }

    @Override
    public void onClearText() {

    }

    @Override
    public void onLogInResult(Boolean result, int code) {

    }
}
