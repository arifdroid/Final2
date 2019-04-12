package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.afinal.fingerPrint_Login.oop.OnServerTime_Interface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

//introduce fingerprint id here,
//need observer to watch, if string pull from fragment(sharedpreferences) exist.
//first instantiate to null
//update text view as well.

public class FingerPrint_LogIn_Final_Activity extends AppCompatActivity implements FingerPrintFinal_View_Interface, View.OnClickListener, Observer, OnServerTime_Interface {

    public static int userCount;

    private FloatingActionButton floatButtonGetAction;
    private TextView textView;
    private ImageView imageView;

    private Login_Select_Action_Fragment fragment;

    private FingerPrintFinal_Presenter presenter;

    private boolean dataPulled;

    public String nameUser;
    public String phoneUser;

    ConstraintLayout backColor;

    //after log in register user data timestamp directly.

    public String globalAdminNameHere ; //"ariff";
    public String globalAdminPhoneHere;//"+60190";

    private FirebaseFirestore instance = FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceRating
            = instance.collection("all_admin_doc_collections")
            .document(globalAdminNameHere+globalAdminPhoneHere+"_doc").collection("all_employee_thisAdmin_collection");


    //cloud function with time value

    private String dayNow;
    //private String dateNow;

    private long timeStampthis;

    private OnServerTime_Interface onServerTime_interface;
    private String dateAndTimeNow;

    /////////// constraint by admin

    private String bssidConstraint;
    private String locationConstraint;
    private String morningConstraint;
    private String eveningConstraint;
    private String ssidConstraint;
    private String latitudeConstraint;
    private String longitudeConstraint;
    private String streetConstraint;

    //this is data from current user.
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    private String userSSID;
    private String userBSSID;

    private String userLatitude;
    private String userLongitude;
    private LocationManager mLocationManager;

    private String user_StreetName;

    // location user.
    private final int REQUEST_LOCATION_PERMISSION = 1;

    private static final int READ_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print__log_in__final_);

    //    userCount =0//;
        onServerTime_interface = new FingerPrint_LogIn_Final_Activity();

        dayNow = null;
        dateAndTimeNow = null;
        dataPulled = false;
        user_StreetName =null;

        globalAdminPhoneHere =null;
        globalAdminPhoneHere=null;

        //pull our data from phone. get bssid, ssid, location also.

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //request location permission

        requestLocationPermission();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        userSSID = wifiInfo.getSSID();
        userBSSID = wifiInfo.getBSSID();





        //pull constraint by admin, like, time constraint, location or bssid,, we do this below.

        Log.i("checkFinalFlow : ", " 1 oncreate() fingerprint_main_activity");


        timeStampthis= 0;

        nameUser = null;
        phoneUser = null;

        floatButtonGetAction = findViewById(R.id.logn_Final_floatingActionButtonID);

        floatButtonGetAction.setOnClickListener(this);

        textView = findViewById(R.id.login_final_textViewHereID);
        imageView = findViewById(R.id.login_final_imageViewID);
        textView.setText("click button below to log in");
        backColor = findViewById(R.id.backLayoutColourID);

        // here we set next day to zero.

        getServerTimeNow(onServerTime_interface);

        presenter = new FingerPrintFinal_Presenter(this);

        presenter.addObserver(this);

        fragment = new Login_Select_Action_Fragment();



        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.i("checkFinalFlow : ", " 2 backstackFragment() fingerprint_main_activity");

                //this probably still null

                if(nameUser!=null && phoneUser!=null && globalAdminPhoneHere!=null && globalAdminNameHere!=null ){

                    //this still can fail. go to admin document.
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                        .document(globalAdminNameHere+globalAdminPhoneHere+"doc");

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            Map<String, Object> remap;

                            remap = Objects.requireNonNull(task.getResult()).getData();
                            //assume same,
                            if(remap!=null) {
                                for (Map.Entry<String, Object> kk : remap.entrySet()) {

                                    //here we gonna set constraint, so before map, we check all these condition

                                    if(kk.getKey().equals("location")){

                                        locationConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("latitude")){

                                        latitudeConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("longitude")){

                                        longitudeConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("bssid")){

                                        bssidConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("ssid")){

                                        ssidConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("morning_constraint")){

                                        morningConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("evening_constraint")){
                                        eveningConstraint = kk.getValue().toString();
                                    }

                                    if(kk.getKey().equals("admin_street_name")){
                                        streetConstraint = kk.getValue().toString();
                                    }



                                }

                            }

                        }else {



                        }

                    }
                });

                }
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

                                Log.i("checkFinalFlow : ", " 3 backfragment() aftertextchange");

                                if(s.toString().equals("success verified")) {

                                    Log.i("checkFinalFlow : ", " 4 backFragment(), success verified, before server time ");

                                    //check in with time stamp.
                                    //getServerTimeNow(this);
                                   // getServerTimeNow(onServerTime_interface);

                                    Log.i("checkFinalFlow : ", " 5 backFragment(), success verified, AFTER server time ");

                                    //sometime getServerTime return later

                                    if(dayNow==null){
                                        getServerTimeNow(onServerTime_interface); //problem if still empty.
                                    }

                                    if(dayNow!=null){

                                        // https://firebase.google.com/docs/firestore/manage-data/delete-data#fields
                                        // https://stackoverflow.com/questions/53836195/firebase-functions-update-all-documents-inside-a-collection
                                        //  https://github.com/firebase/snippets-node/blob/e709ef93b8d7c6f538d1b4143ffe8ec2e2741d2e/firestore/main/index.js#L916-L956


                                        //we could do all this in separate task, for faster?

                                        String timeCurrent = dateAndTimeNow.substring(11,13);
                                        String timeCurrent2 = dateAndTimeNow.substring(14,16);
                                        timeCurrent = timeCurrent+"."+timeCurrent2;
                                        String dateCurrent = dateAndTimeNow.substring(4,10);

                                       // getServerTimeNow(onServerTime_interface);
                                        //check time constraint set by admin.
                                        //push data into database
                                        //for today, is this first time writing to database?
                                        //if first time, check as morning frame.

                                        //one way to know, go to today time stamp in database,

                                        // https://github.com/firebase/functions-samples/blob/master/delete-old-child-nodes/functions/.eslintrc.json
                                        // https://stackoverflow.com/questions/32004582/delete-firebase-data-older-than-2-hours
                                        // https://firebase.google.com/docs/firestore/extend-with-functionsx

                                        //if we delete from firebase function automatically, we can just check with null.

                                        //go to time stamp? if not, just viewing.

                                        //check with bssid first.




                                        Float timeCurrent_Float = Float.valueOf(timeCurrent);

                                        if(timeCurrent_Float<14f){ //assume morning constraint, before 14pm

                                            Float morning_constraint = Float.valueOf(morningConstraint);
//
//


//
//                                             if(timeCurrent_Float<morning_constraint){
//
//
//                                            }
//
                                        }






                                       // if(timeAdmin)
//                                        Intent intent = new Intent(FingerPrint_LogIn_Final_Activity.this, Main_BottomNav_Activity.class);
//                                        startActivity(intent);

                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       // finish();



                                    }




                                }else if(s.toString().equals("waiting")){

                                    Log.i("checkFinalFlow : ", " 6 backFragment(), do nothing waiting for fingerprint ");

                                }else if(s.toString().equals("try again")){

                                    Log.i("checkFinalFlow : ", " 7 backFragment(), try again fingerprint ");

                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"please select admin, try finger again" ,Toast.LENGTH_SHORT).show();


                                }else {

                                    Log.i("checkFinalFlow : ", " 8 backFragment(), fingerprint need try again");

                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"try fingerprint" ,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }else {

                    Log.i("checkFinalFlow : ", " 9 backFragment(), waiting for fingerprint ");

                    textView.setText("waiting");
                }

                Log.i("checkFinalFlow : ", " 10 backFragment(), prolem in flow ");

            }
        });




    }

    public void getServerTimeNow(final OnServerTime_Interface onServerTime_interface){

        FirebaseFunctions.getInstance().getHttpsCallable("getTimeNow")
                .call()
                .addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
                    @Override
                    public void onComplete(@NonNull Task<HttpsCallableResult> task) {

                        Log.i("checkFinalFlow : ", " 11 getServerTimeNow(), before task");

                        if(task.isSuccessful()){

                        //long timm = (long) (task.getResult().getData())/1000;
                        long timm = (long) (task.getResult().getData());

                        if(onServerTime_interface!=null) {

                            onServerTime_interface.onSuccess(timm);

                            Date date = new Date(timm); //if in wrong timezone, need to setup

                            dayNow  = (date.toString()).substring(0,3);

                            dateAndTimeNow = date.toString();

                            Log.i("checkFinalFlow : ", " 11v1 getServerTimeNow(), getting the time: "+ date);

                            Log.i("checkFinalFlow : ", " 12 getServerTimeNow(), getting the time");

                             Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time now is: "+ date +" day:"+dayNow, Toast.LENGTH_LONG).show();
                        }else {

                            //must handle this. in case server dont return time, how do we insert timestamp?
                            Log.i("checkFinalFlow : ", " 13 getServerTimeNow(), no time recorded from server");

                            //maybe we can get user timestamp,

                           // getBackupTimeFromUser();

                            onServerTime_interface.onFailed();
                        }



                        }else {

                            Log.i("checkFinalFlow : ", " 14 getServerTimeNow(), task failed");


                            if(dayNow==null){

                                //getBackupTimeFromUser(); ,, need to request again.
                            }

                    }

                    }
                });

        return;

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

    //probably unnecessary
    @Override
    public void onSuccess(long timeStampHere) {

        Log.i("checkkTime : ", " 2 "+ timeStampHere);
        this.timeStampthis =timeStampHere;
        return;
    }

    @Override
    public void onFailed() {

        if(dayNow==null) {
            //getBackupTimeFromUser(); , still ask user to make sure, have internet connection

        return;
        }
    }

    // we cant afford to get data from user, since user can offline,
    //and reset next day data.

    private void getBackupTimeFromUser() {

        //getting date time from user

        Log.i("checkFinalFlow : ", " 15 getBackupTimeFromUser(), setup our day without server ");

        Date date2 = new Date();

        Log.i("checkFinalFlow : ", " 15v` getBackupTimeFromUser(), setup our day without server :" + date2);

        Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time without server is: "+ date2 +" day:"+dayNow, Toast.LENGTH_LONG).show();

        dayNow  = (date2.toString()).substring(0,3);

        return;



    }

    // getting location section

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //       Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    5, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            userLatitude = String.valueOf(location.getLatitude());
                            userLongitude = String.valueOf(location.getLongitude());

                            Double lat = location.getLatitude();
                            Double longitude = location.getLongitude();

                            Log.i("checkkLocation", "3");

                            Geocoder geocoder = new Geocoder(FingerPrint_LogIn_Final_Activity.this, Locale.getDefault());

                            try {

                                Log.i("checkkLocation", "4");

                                user_StreetName = geocoder.getFromLocation(lat, longitude, 1).get(0).getThoroughfare();

                                Log.i("checkkLocation", "5 " + user_StreetName);



                                if(user_StreetName !=null|| user_StreetName !=""){

                                    mLocationManager.removeUpdates(this);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }



                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });



        } else {

            Log.i("checkkLocation", "5");

            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }

        return;
    }
}
