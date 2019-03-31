package com.example.afinal.fingerPrint_Login.register.register_as_admin_add_userList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.afinal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Add_User_Activity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private ArrayList<UserFromAdmin> userList;

    //private ArrayList<UserFromAdmin> u

    private RecyclerViewAdapter_UserList recyclerViewAdapter_UserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user_);

        floatingActionButton = findViewById(R.id.add_User_FloatButtoniD);
        recyclerView = findViewById(R.id.add_User_RecycleriD);

        initRecycler();

        floatingActionButton.setOnClickListener(this);

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

                    String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));

                    Log.i("checkAddingUser, ","6 request code, name:" + name+" , phone:"+number);

                    if(!number.equals("")||number!=null) {

                        Log.i("checkAddingUser, ","7 request code");

                        userList.add(new UserFromAdmin(name,number));

                        recyclerViewAdapter_UserList.notifyDataSetChanged();
                        Log.i("checkAddingUser, ","8 before setadpater again");


                        recyclerView.setAdapter(recyclerViewAdapter_UserList);

                        Log.i("checkAddingUser, ","9 before setadpater again");

                    }

                }

            }

    }
}
