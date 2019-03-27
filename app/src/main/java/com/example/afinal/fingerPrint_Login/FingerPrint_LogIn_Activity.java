package com.example.afinal.fingerPrint_Login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;
import com.example.afinal.fingerPrint_Login.register.register_with_activity.RegAdmin_Activity;

import java.util.Observable;
import java.util.Observer;

public class FingerPrint_LogIn_Activity extends AppCompatActivity implements Observer {
    //https://blog.mindorks.com/essential-guide-for-designing-your-android-app-architecture-mvp-part-2-b2ac6f3f9637

    //https://github.com/googlesamples/android-architecture

    // https://riptutorial.com/android/example/29784/simple-login-example-in-mvp

    private TextView textViewMessage, textViewRegister;
    private Button buttonLogIn, buttonSelectAdmin;

    private Presenter_FingerPrint presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMessage =  findViewById(R.id.logIn_activity_textView_ID);
        textViewRegister = findViewById(R.id.logIn_activity_textViewRegisteriD);
        buttonLogIn = findViewById(R.id.logIn_activity_button1_ID);
        buttonSelectAdmin = findViewById(R.id.logIn_activity_button2_ID);

        presenter = new Presenter_FingerPrint(FingerPrint_LogIn_Activity.this);


        presenter.addObserver(FingerPrint_LogIn_Activity.this);

        buttonSelectAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FingerPrint_LogIn_Activity.this, FingerPrint_LogIn_Final_Activity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY);


            }
        });

        //when we select log in, should invoke fingerprint reader.

        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //need to rewrite later, need to assume presenter dont know its a context
                //better sent interface like view, lifecycle dependencies issue
                textViewMessage.setText("checking fingerprint");

                presenter.checkSupportedDevice();

                Log.i("checkk flow: ","1");



                Log.i("checkk flow: ","2");


            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(FingerPrint_LogIn_Activity.this, RegAdmin_Activity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();


            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.deleteObserver(FingerPrint_LogIn_Activity.this);
    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof Presenter_FingerPrint){

            String s = ((Presenter_FingerPrint) o).getFinalStringResult();
            textViewMessage.setText(s);

            //then we can time stamp, insert data into firebase.
            //fetch data from firebase, to display in 2nd main activity fragment
            //all of this will be done in the background.
            //here, sqlite database should have been created, our task is to check, do we need to update or not.

        }


    }
}
