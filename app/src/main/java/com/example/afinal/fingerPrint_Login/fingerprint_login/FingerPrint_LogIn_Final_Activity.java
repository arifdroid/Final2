package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.afinal.fingerPrint_Login.oop.OnServerTime_Interface;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

// https://stackoverflow.com/questions/8077728/how-to-prevent-the-activity-from-loading-twice-on-pressing-the-button

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

    private String phoneAdminConstraint;

    //this is data from current user.
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    private String userSSID;
    private String userBSSID;

    private String userLatitude;
    private String userLongitude;
    private LocationManager mLocationManager;

    private String user_StreetName;

    //time here current.
    private String timeCurrent;
    private String timeCurrent2;
    private String dateCurrent;
    private boolean checkLocationProcess;
    private boolean checkAdminConstraintProcess;
    Method showsb;
    private boolean statusBarWeSet;
    private int counterFlowHere;
    private int counterFlowHere2;
    private int counterFlowHere3;

    //test program flow.

    private TextView textViewFinalData;
    private int countFinalFlow;

    private TextView textViewFinalData2;
    private int countFinalFlow2;

    private TextView textViewDataLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print__log_in__final_);

        textViewFinalData = findViewById(R.id.textViewFINALDATAID);
        countFinalFlow=0;
        textViewFinalData2 = findViewById(R.id.textViewFinalDataID2);
        countFinalFlow2=0;

        textViewDataLocation = findViewById(R.id.textViewFinalDataLocationiD);


    //    userCount =0//;
        counterFlowHere =0;
        counterFlowHere2=0;
        counterFlowHere3=0;
        Log.i("checkFinalFlow : ", " 1 oncreate() fingerprint_main_activity");

        statusBarWeSet = false;

        onServerTime_interface = new FingerPrint_LogIn_Final_Activity();

        dayNow = null;
        dateAndTimeNow = null;
        dataPulled = false;
        user_StreetName =null;

        globalAdminPhoneHere =null;
        globalAdminNameHere=null;

        presenter = new FingerPrintFinal_Presenter(this, (Activity)FingerPrint_LogIn_Final_Activity.this);
        Log.i("checkFinalFlow : ", " 2 oncreate() fingerprint_main_activity, after presenter Constructor()");

        presenter.addObserver(this);


        //pull our data from phone. get bssid, ssid, location also.

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //request location permission
        Log.i("checkFinalFlow : ", " 3 oncreate() activity, before request location ");
        presenter.requestLocationPermission(mLocationManager);
        Log.i("checkFinalFlow : ", " 4 oncreate() activity, after request location ");

        //get the time here
        Log.i("checkFinalFlow : ", " 5 oncreate() activity, before request time ");

        presenter.getServerTimeNow(onServerTime_interface); // this will be done, in back task
        Log.i("checkFinalFlow : ", " 6 oncreate() activity, after request time ");

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


        fragment = new Login_Select_Action_Fragment();



        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Log.i("checkFinalFlow : ", " 6 backstackFragment() fingerprint_main_activity");

                //this probably still null

                countFinalFlow2++; //this triggered two times,

                textViewFinalData2.setText("backStackChange: "+countFinalFlow2);

                if(nameUser!=null) {

                    Log.i("checkFinalFlow : ", " 7 backstackFragment() activity, nameUser :"+nameUser +" success");

                    if(nameUser!="") {
                        textView.setText("admin detected, fingerprint checkin now with server..");

                        Log.i("checkFinalFlow : ", " 8 backstackFragment() activity, before fingerprint");
                        presenter.checkSupportedDevice();
                        Log.i("checkFinalFlow : ", " 8 backstackFragment() activity, after fingerprint");

                        textView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) { //being triggered everytime, this is the problem.


                                countFinalFlow++;
                                textViewFinalData.setText("trigerred textchange :"+ countFinalFlow);

                                Log.i("checkFinalFlow : ", " 3 backfragment() aftertextchange");

                                if(s.toString().equals("success verified")) {

                                    // https://firebase.google.com/docs/firestore/manage-data/delete-data#fields
                                    // https://stackoverflow.com/questions/53836195/firebase-functions-update-all-documents-inside-a-collection
                                    //  https://github.com/firebase/snippets-node/blob/e709ef93b8d7c6f538d1b4143ffe8ec2e2741d2e/firestore/main/index.js#L916-L956

                                    // https://github.com/firebase/functions-samples/blob/master/delete-old-child-nodes/functions/.eslintrc.json
                                    // https://stackoverflow.com/questions/32004582/delete-firebase-data-older-than-2-hours
                                    // https://firebase.google.com/docs/firestore/extend-with-functionsx

                                    // problem, this do not return anything.

                                    Log.i("finalSharePreDataCheck","FingerPrintLogin_Final_Activity 6 , before return,name: "
                                            + nameUser+ ", phone: "+phoneUser+ ", adminName:"
                                            +globalAdminNameHere+" , adminPhone: "+globalAdminPhoneHere);

                                    //right data, but we dont retrieve data.
                                    presenter.getCurrent_User_Admin_Server_Value(nameUser,phoneUser,globalAdminNameHere,globalAdminPhoneHere);


                                    Log.i("checkFinalFlow : ", " 4 backFragment(), success verified, before server time ");

                                    Log.i("checkFinalFlow : ", " 5 backFragment(), success verified, AFTER server time ");



                                }else if(s.toString().equals("waiting")){

                                    Log.i("checkFinalFlow : ", " 6 backFragment(), do nothing waiting for fingerprint ");

                                }else if(s.toString().equals("try again")){

                                    Log.i("checkFinalFlow : ", " 7 backFragment(), try again fingerprint ");

                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"please select admin, try finger again" ,Toast.LENGTH_SHORT).show();


                                }
//                                else {
//
////handle here, if return nothing
//                                    //update(this,"");
//                                    presenter.stopListetingFingerprint(); //cannot stop here
//                                    Log.i("checkFinalFlow : ", " 8 backFragment(), fingerprint need try again");
//
//                                    Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"try fingerprint" ,Toast.LENGTH_SHORT).show();
//                                }

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

     counterFlowHere++;
        Log.i("checkUpdateFinal","1");

    if(o instanceof FingerPrintFinal_Presenter){

        String s = ((FingerPrintFinal_Presenter) o).getFinalStringResult();
        textView.setText(s); //textview is null, since fingerprint do not return result, update always running.

        Log.i("checkUpdateFinal","2 fingerprint :"+s);
        //getTime
        String time = ((FingerPrintFinal_Presenter) o).getDateAndTimeNow(); //problem if, always updating,

        dateAndTimeNow=time;

        Log.i("checkUpdateFinal"," time :"+time);
        //getFireStore
        Map<String, Object> remapAdminConstraint = ((FingerPrintFinal_Presenter) o).getReturnMap();
        Log.i("checkUpdateFinal","3 remapAdminConstraint :"+ remapAdminConstraint);
        if(remapAdminConstraint!=null) {

            for (Map.Entry<String, Object> kk : remapAdminConstraint.entrySet()) {

                if (kk.getKey().equals("location")) {

                    locationConstraint = kk.getValue().toString();
                }
                if (kk.getKey().equals("latitude")) {
                    latitudeConstraint = kk.getValue().toString();
                }

                if (kk.getKey().equals("longitude")) {

                    longitudeConstraint = kk.getValue().toString();
                }
                if (kk.getKey().equals("morning_constraint")) {
                    morningConstraint = kk.getValue().toString();
                }

                if (kk.getKey().equals("evening_constraint")) {

                    eveningConstraint = kk.getValue().toString();
                }
                if (kk.getKey().equals("admin_street_name")) {
                    streetConstraint = kk.getValue().toString();
                }

                if (kk.getKey().equals("bssid")) {

                    bssidConstraint = kk.getValue().toString();
                }
                if (kk.getKey().equals("ssid")) {
                    ssidConstraint = kk.getValue().toString();
                }
                if (kk.getKey().equals("phone")) {
                    phoneAdminConstraint = kk.getValue().toString();
                }



            }

            Log.i("checkUpdateFinal","4 remapAdminConstraint :"+ remapAdminConstraint + ", ssid constraint :"+ssidConstraint);
            checkAdminConstraintProcess = true;

        }
        //getLocation

        Map<String,Object> remapLocation = ((FingerPrintFinal_Presenter) o).getRemapLocation();
        Log.i("checkUpdateFinal","5 remapLocation :"+ remapLocation );
        if(remapLocation!=null) {
            for (Map.Entry<String, Object> kk : remapLocation.entrySet()) {

                if (kk.getKey().equals("userLatitude")) {
                    userLatitude = kk.getValue().toString();
                }


                if (kk.getKey().equals("userLongitude")) {
                    userLongitude = kk.getValue().toString();
                }

            }

            Log.i("checkUpdateFinal","6 remapLocation :"+ remapLocation + " latite :"+ userLatitude );

            checkLocationProcess= true;
        }
        //here we process

        if(checkAdminConstraintProcess ==true && checkLocationProcess ==true && dateAndTimeNow!=null && s!=null && !s.equals("")){ //meaning all data being fetch

//            if(morningConstraint!=null &&eveningConstraint!=null && dateAndTimeNow!=null && userLongitude!=null && userLatitude!=null
//        && latitudeConstraint!=null && longitudeConstraint!=null && userBSSID!=null && userSSID!=null
//        && ssidConstraint!=null && bssidConstraint!=null) {

            Log.i("finalCheckFlowHere", "1");

            if(phoneAdminConstraint!=null){ //means the right admin have finish downloaded, but might some case, phone data retrieve, but not others?
                                            //MAYBE
                Log.i("finalCheckFlowHere", "2, phone admin pull:" + phoneAdminConstraint);

                timeCurrent = dateAndTimeNow.substring(11, 13);      //process time current first, by server
                timeCurrent2 = dateAndTimeNow.substring(14, 16);
                timeCurrent = timeCurrent + "." + timeCurrent2;

                //first check if within network.
                if(userSSID==ssidConstraint){

                    Log.i("finalCheckFlowHere", "2, admin ssid:" + ssidConstraint+" , user ssid"+ userSSID);

                    //Toast.makeText(this,"bssid different" ,Toast.LENGTH_LONG).show();

                    presenter.deleteObserver(this);

                    if(userBSSID==bssidConstraint){

                        Toast.makeText(this,"bssid same, ssid :"+ userSSID ,Toast.LENGTH_LONG).show();



                    }else { //if bssid different might need to check other bssid available by admin.

                        Toast.makeText(this,"bssid different, bssid "+userBSSID ,Toast.LENGTH_LONG).show();



                    }

                }else { //if outside wifi network. use location instead


                    Log.i("finalCheckFlowHere", "3, ssid different, location check, latitude admin:"
                            + latitudeConstraint+" , user latitude"+ userLatitude);

                    //presenter.

                    //so process location., //if user dont provide location, ask user to provide.

                    if((userLatitude==null || userLongitude==null) || (latitudeConstraint==null || longitudeConstraint==null)){

                        Log.i("finalCheckFlowHere", "4, ssid different, location NULL, latitude admin:"
                                + latitudeConstraint+" , user latitude"+ userLatitude);
                        textViewDataLocation.setText("basic flow:"+ counterFlowHere+ " , turn on GPS:"+counterFlowHere2+" , GPS ON:"+ counterFlowHere3);
                       // Toast.makeText(this,"please turn on GPS",Toast.LENGTH_LONG).show();
                        //this is always excuted.
                        //ask user to provide location, turn on GPS.
                        //else cannot log in.
                        //

                        counterFlowHere2++;

                        //statusBarWeSet=false;

                        if(statusBarWeSet==false) {



                            presenter.deleteObserver(this);

                            @SuppressLint("WrongConstant") Object sbservice = getSystemService("statusbar");
                            Class<?> statusbarManager = null;
                            try {
                                statusbarManager = Class.forName("android.app.StatusBarManager");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (Build.VERSION.SDK_INT >= 17) {
                                try {
                                    showsb = statusbarManager.getMethod("expandNotificationsPanel");
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    showsb = statusbarManager.getMethod("expand");
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                showsb.invoke(sbservice);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }

                        }
//                        Intent openIntent = new Intent(Intent)

                       // presenter.deleteObserver(this);
                       // onResume(); // restart process?

                    }else {

                        Log.i("finalCheckFlowHere", "5, ssid different, location CHECK, latitude admin:"
                                + latitudeConstraint+" , user latitude"+ userLatitude);

                        presenter.removeLocationNow(); //need check here, if location got zero updated before process

                        //if location provided, process, and check with admin

                        Double userLatitudeDouble = Double.valueOf(userLatitude);
                        Double userLongitudeDouble = Double.valueOf(userLongitude);

                        Double adminLatitude = Double.valueOf(latitudeConstraint);
                        Double adminLongitude = Double.valueOf(longitudeConstraint);

                        Location user = new Location("point User");

                        user.setLatitude(userLatitudeDouble);
                        user.setLongitude(userLongitudeDouble);

                        Location admin = new Location("point Admin");
                        admin.setLongitude(adminLongitude);
                        admin.setLatitude(adminLatitude);

                        Log.i("finalCheckFlowHere", "6, ssid different, location CHECK, latitude admin:"
                                + latitudeConstraint+" , user latitude"+ userLatitude);

                        float distanceOffset = user.distanceTo(admin);

                        if(distanceOffset<=50){  //assume 50 is 50 meter.
                            //here can process ask to stamp.

                            Log.i("finalCheckFlowHere", "7, ssid different, location CHECK, OFFSET RIGHT");

                            Toast.makeText(this,"distance within provided, distance: "+ distanceOffset,Toast.LENGTH_LONG).show();

                            presenter.deleteObserver(this);
                        }else { //means more than 50 meter.

                            Log.i("finalCheckFlowHere", "8, ssid different, location CHECK, OFFSET OUT");

                            counterFlowHere3++;

                            Toast.makeText(this,"distance outside provided "+ distanceOffset,Toast.LENGTH_LONG).show();

                            //recorded location to admin, and send notification to admin.
                            //after that ask to time stamp
                            //can apply machine learning. //detect location manipulation.

                            //can apply machine learning for timestamp process.

                            presenter.deleteObserver(this);
                        }

                    }

                    //if(lo)


                }



            }

 //       }
        }



        Log.i("checkAllValue: ", "[CHECK] 1. ssid Admin: " + ssidConstraint + ", 2.ssid user: " + userSSID
                + ", 3. bssid Admin: " + bssidConstraint + ", 4. bssid User " + userBSSID +
                ", 5.longitude admin: " + longitudeConstraint + ", 6.longitude user: " + userLongitude +
                ". 7.latitude admin" + latitudeConstraint + ", 8.latitude user" + userLatitude +
                ". 9.morning admin" + morningConstraint + ", 10.morning user" + timeCurrent +
                ". 11.evening admin" + eveningConstraint + ", 10.morning user" + timeCurrent);


        //we can process data here ,

        //need to stop listening at some point.

        //check within network admin provided.
        //if not, check location admin provided.
    }

    }

    @Override
    protected void onResume() {
        presenter.addObserver(this);
        statusBarWeSet =true;
        super.onResume();



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


}
