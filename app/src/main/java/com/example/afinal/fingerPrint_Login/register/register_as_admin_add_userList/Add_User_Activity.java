package com.example.afinal.fingerPrint_Login.register.register_as_admin_add_userList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin.RegAdmin_AsAdmin_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Add_User_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private ArrayList<UserFromAdmin> userList;

    //private ArrayList<UserFromAdmin> u

    private RecyclerViewAdapter_UserList recyclerViewAdapter_UserList;
    private Timer timer;
    private FloatingActionButton buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user_);

        buttonNext = findViewById(R.id.add_User_FloatButtonFinalizeID);
        floatingActionButton = findViewById(R.id.add_User_FloatButtoniD);
        recyclerView = findViewById(R.id.add_User_RecycleriD);

        initRecycler();

        floatingActionButton.setOnClickListener(this);

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        ObjectAnimator animator = ObjectAnimator.ofFloat(buttonNext,"translationY",-180f);
                        buttonNext.animate()
                                .alpha(1f)
                                .setDuration(200)
                                .setListener(null);
                        animator.setDuration(200);
                        animator.start();


                        Animation fadeIn = AnimationUtils.loadAnimation(Add_User_Activity.this,R.anim.fadein);
                        buttonNext.startAnimation(fadeIn);


                        timer.cancel();


                    }

                });



            }
        },1500,10);

    }

    private void initRecycler() {

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    userList = new ArrayList<>();


    Log.i("checkAddingUser, ","1 ");

    recyclerViewAdapter_UserList = new RecyclerViewAdapter_UserList(this, userList);

        Log.i("checkAddingUser, ","3 ");
    recyclerView.setAdapter(recyclerViewAdapter_UserList);

    }

    @Override
    public void onClick(View v) {

        Log.i("checkAddingUser, ","4 button click ");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==1){

                Log.i("checkAddingUser, ","5 request code");

                if(resultCode==RESULT_OK){

                    Log.i("checkAddingUser, ","5 result code");

                    Uri contactData = data.getData();

                    Cursor cursor = managedQuery(contactData,null,null,null,null);

                    cursor.moveToFirst();

                    Toast.makeText(this,"pick contact you wish to add to your user list",Toast.LENGTH_LONG).show();

                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));

                    Log.i("checkAddingUser, ","6 request code, name:" + name+" , phone:"+number);

                    if(!number.equals("")||number!=null) {

                        Log.i("checkAddingUser, ","7 request code");

                        userList.add(new UserFromAdmin(name,number));

                        recyclerViewAdapter_UserList.notifyDataSetChanged();
                        Log.i("checkAddingUser, ","8 before setadpater again");


                       // recyclerView.setAdapter(recyclerViewAdapter_UserList);

                        Log.i("checkAddingUser, ","9 before setadpater again");

                    }

                }

            }

    }
}
