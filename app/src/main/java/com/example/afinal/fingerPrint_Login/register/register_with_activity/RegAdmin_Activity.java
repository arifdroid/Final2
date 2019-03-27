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
import com.example.afinal.fingerPrint_Login.register.register_user_activity.RegUser_Activity;

import java.util.Observable;
import java.util.Observer;

public class RegAdmin_Activity extends AppCompatActivity implements View.OnClickListener, RegAdminViewInterface, Observer {

    private TextView textViewMessage;
    private EditText editTextName, editTextPhone;
    private Button logInButton;

    private RegAdmin_Presenter presenter;

    private String globalAdminName;
    private String globalAdminPhone;

    private boolean checkValid;

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
        textViewMessage.setText("please enter admin name, phone");
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

    @Override
    public void update(Observable o, Object arg) {

        //here we set valid. back to true

        if(o instanceof RegAdmin_Presenter){

            boolean checkHere = ((RegAdmin_Presenter) o).checkFinalFromFirebase();

            if(checkHere==true){



                textViewMessage.setText("success log in");
                result(checkHere);


            }else {
                return;
            }
        }

    }

    @Override
    public void result(boolean check) {

        if(check){

            Toast.makeText(this,"success log in",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(RegAdmin_Activity.this, RegUser_Activity.class);

            intent.putExtra("admin_name", globalAdminName);
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
