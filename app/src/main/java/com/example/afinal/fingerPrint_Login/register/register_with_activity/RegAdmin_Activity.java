package com.example.afinal.fingerPrint_Login.register.register_with_activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;
import com.example.afinal.fingerPrint_Login.register.register_user_activity.RegUser_Activity;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class RegAdmin_Activity extends AppCompatActivity implements View.OnClickListener, RegAdminViewInterface, Observer {

    private TextView textViewMessage;
    private EditText editTextName, editTextPhone;
    private Button logInButton;

    private RegAdmin_Presenter presenter;

    private String globalAdminName;
    private String globalAdminPhone;

    private boolean checkValid;
    private Timer timer;
    private String statusnow;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin_);

        editTextName = findViewById(R.id.regAdmin_EditText_AdminNameID);
        editTextPhone = findViewById(R.id.regAdmin_EditText_AdminPhoneID);
        textViewMessage = findViewById(R.id.regAdmin_TextView_ID);
        logInButton = findViewById(R.id.regAdmin_Button_ID);

        checkValid=false;



        textViewMessage.setText("please enter admin name, phone");

        presenter = new RegAdmin_Presenter(this);
        presenter.addObserver(this);



        logInButton.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deleteObserver(this);
    }

    @Override
    public void onClick(View v) {

        statusnow = "wait..";
        textViewMessage.setText(statusnow);
        String adminName = editTextName.getText().toString();
        String adminPhone = editTextPhone.getText().toString();

        checkValid = presenter.checkInputValid(adminName,adminPhone);

        if(checkValid){ //here we call

            globalAdminName = adminName;
            globalAdminPhone =adminPhone;
          boolean finalStatus = presenter.checkFromFirebaseSimulation(adminName,adminPhone);

            if(finalStatus){
                //success

               result(true);

            }else {

                result(false);
            }


        }else {

           return;
        }


    }

//    private void onReturn() {
//        Toast.makeText(this, "please try again", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, FingerPrint_LogIn_Final_Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//    }
//

    @Override
    public void update(Observable o, Object arg) {

        //here we set valid. back to true

        if(o instanceof RegAdmin_Presenter){

            boolean checkHere = ((RegAdmin_Presenter) o).checkFinalFromFirebase(); //maybe because we never return false.

            if(checkHere==true){



                textViewMessage.setText("success log in");
                result(checkHere);


            }else {
                //if not return, never update again, since,
                //return; //or we just handle here.
                //update will be called if theres an update, should always have update

                textViewMessage.setText("not success, try again");

              //  return;
                //onReturn();



            }
        }

    }

    @Override
    public void result(boolean check) {

        if(check){

            Toast.makeText(this,"success log in",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegAdmin_Activity.this, RegUser_Activity.class);

            intent.putExtra("admin_name", globalAdminName); //this just pass intent.
            intent.putExtra("admin_phone", globalAdminPhone);

            startActivity(intent);

        }
        else {

            //check if cancel after few secs

            Toast.makeText(this,"please wait",Toast.LENGTH_SHORT).show();

            //then ask try again.

        }



    }
}
