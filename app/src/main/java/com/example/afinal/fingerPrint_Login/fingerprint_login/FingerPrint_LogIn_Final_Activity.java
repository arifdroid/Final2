package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.Presenter_FingerPrint;
import com.example.afinal.fingerPrint_Login.main_activity_fragment.Main_BottomNav_Activity;

import java.util.Observable;
import java.util.Observer;

//introduce fingerprint id here,
//need observer to watch, if string pull from fragment(sharedpreferences) exist.
//first instantiate to null
//update text view as well.

public class FingerPrint_LogIn_Final_Activity extends AppCompatActivity implements FingerPrintFinal_View_Interface, View.OnClickListener, Observer {

    private FloatingActionButton floatButtonGetAction;
    private TextView textView;
    private ImageView imageView;

    private Login_Select_Action_Fragment fragment;

    private FingerPrintFinal_Presenter presenter;

    private boolean dataPulled;

    String nameUser;
    String phoneUser;

    ConstraintLayout backColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print__log_in__final_);

        //initially false, add observer to this.
        dataPulled = false;


        nameUser = "";
        phoneUser = "";

        floatButtonGetAction = findViewById(R.id.logn_Final_floatingActionButtonID);

        floatButtonGetAction.setOnClickListener(this);

        textView = findViewById(R.id.login_final_textViewHereID);
        imageView = findViewById(R.id.login_final_imageViewID);
        textView.setText("click button below to log in");
        backColor = findViewById(R.id.backLayoutColourID);

        presenter = new FingerPrintFinal_Presenter(this);

        presenter.addObserver(this);

        fragment = new Login_Select_Action_Fragment();

        Log.i("checkFinal : ", " flow 1");

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.i("checkFinal : ", " flow 2 , backstacklistener, before presenter activity");

                if(nameUser!=null) {

                    if(nameUser!="") {
                        textView.setText("admin detected, fingerprint checkin now with server..");

                        presenter.checkSupportedDevice();

                        textView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                if(s.toString().equals("success verified")) {

                                    Intent intent = new Intent(FingerPrint_LogIn_Final_Activity.this, Main_BottomNav_Activity.class);
                                    startActivity(intent);

                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish();

                                }else {
                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"please try fingerprint again" ,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }else {
                    textView.setText("waiting");
                }

                Log.i("checkFinal : ", " flow 3 , backstacklistener, after presenter activity");

            }
        });




    }

    @Override
    public void onClick(View v) {

        //reset back status
       // nameUser="";
        nameUser=null;

        backColor.setAlpha(0.05f);

        getSupportFragmentManager().beginTransaction()
        .replace(R.id.frameID,fragment)
        .addToBackStack("")
        .commit();


    }

    @Override
    public void update(Observable o, Object arg) {

    if(o instanceof FingerPrintFinal_Presenter){

        String s = ((FingerPrintFinal_Presenter) o).getFinalStringResult();
        textView.setText(s);
    }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.deleteObserver(this);
    }
}
