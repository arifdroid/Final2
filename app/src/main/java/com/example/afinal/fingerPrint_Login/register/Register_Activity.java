package com.example.afinal.fingerPrint_Login.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.afinal.R;

public class Register_Activity extends AppCompatActivity {


    //https://www.youtube.com/watch?v=Asc4hU1iSTU&list=PLOzDKCBkR50Set8l8vzp4sWSumCy6Z6Nf&index=35&t=652s
    //private TextView mTextMessage;

    public static String globalAdminName;
    public static String globaladminPhone;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment =null;

            switch (item.getItemId()) {
                case R.id.navigation_checkadmin:
                    selectedFragment =new RegisterAdmin_Fragment();
                    break;
                case R.id.navigation_registeruser:

                    selectedFragment =new RegisterUser_Fragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.registration_frameiD,selectedFragment,null);
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.registration_navigationiD);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.registration_frameiD,
                    new RegisterAdmin_Fragment()).commit();
        }

    }

}
