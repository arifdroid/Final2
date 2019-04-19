package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afinal.fingerPrint_Login.register.WifiReceiver;
import com.example.afinal.fingerPrint_Login.register.register_as_admin_add_userList.Add_User_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class RegAdmin_asAdmin_Profile_Activity extends AppCompatActivity implements Observer {

    private CircleImageView circleImageView;

    private TextView textViewName, textViewPhone;

    private RecyclerView recyclerView;

    private RecyclerView_Admin_Profile_Adapter recyclerView_Admin_Profile_Adapter;

    private LocationManager mLocationManager;

    private LocationListener mLocationListener;

    private ArrayList<AdminDetail> adminDetailsList;

    private ArrayList<AdminDetail> returnAdminDetailList;

    WifiManager wifiManager;

    WifiInfo wifiInfo;

    String streetName;

    private FloatingActionButton floatingActionButton;

//    private final int REQUEST_LOCATION_PERMISSION = 1;

    //static final for image

    private static final int READ_REQUEST_CODE = 42;

    //setup firebase storage reference to test

    private StorageReference storageReference;

    private FirebaseFirestore firebaseFirestore;

    private Presenter_RegAdmin_asAdmin_Profile_Activity presenter;
    private boolean imageSetupTrue;
    private Map<String,String> locationMapFinal;
    private String user_name_asAdmin;
    private String user_phone_asAdmin;
    public static String wifiSSIDHere;
    public static String wifiBSSIDHere;
    private boolean gotWifi;
    private Timer timer;
    private int count;
    private WifiReceiver wifiReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin_as_admin__profile_);

        wifiReceiver = new WifiReceiver();

        Intent intent = getIntent();
        gotWifi=false;
        wifiSSIDHere="";
        wifiBSSIDHere="";

        user_name_asAdmin = intent.getStringExtra("adminName_asAdmin");
        user_phone_asAdmin = intent.getStringExtra("adminPhone_asAdmin");
        adminDetailsList = new ArrayList<>();

        count++;

        locationMapFinal=new HashMap<>();
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        wifiInfo = wifiManager.getConnectionInfo();

        imageSetupTrue=false;

        presenter = new Presenter_RegAdmin_asAdmin_Profile_Activity(this);


        returnAdminDetailList = new ArrayList<>();

        floatingActionButton = findViewById(R.id.admin_Profile_fButtoniD);
        circleImageView = findViewById(R.id.admin_Profile_circleImageViewID);
        textViewName = findViewById(R.id.admin_Profile_textViewNameiD);
        textViewPhone = findViewById(R.id.admin_Profile_textViewPhoneiD);


        if(user_name_asAdmin!=null && user_phone_asAdmin!=null){

            textViewName.setText(user_name_asAdmin);
            textViewPhone.setText(user_phone_asAdmin);
        }

        presenter.addObserver(this);

        //firebase reference

        storageReference = FirebaseStorage.getInstance().getReference();

        //setting up image
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(intent,READ_REQUEST_CODE);

            }
        });

//        //setting up wifi if not initially setup.
//        presenter.getWifiNow(wifiManager);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //process data getWifi
        String wifiName = checkWifiStep1();
        String wifiBssid = checkBSSIDStep();

        //get Location

        //requestLocationPermission();

        presenter.requestLocationPermission(mLocationManager);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;

                if (imageSetupTrue) {

                    for (AdminDetail adminDetail : returnAdminDetailList) {

                        Log.i("checkkLocation", "13 checkBox = " + adminDetail.isCheckBox());

                        boolean checkBoxHere = adminDetail.isCheckBox();

                        Log.i("checkkLocation", "14 checkBox = " + adminDetail.isCheckBox());

                        if (checkBoxHere) {
                            count++;
                        }

                    }
                    //problem is count always 1, inevitable, keep


                    //if (count == 1 && RecyclerView_Admin_Profile_Adapter.sentCheck == false) {
                    if (count == 0) {

                    } else {

                        if ((count) == adminDetailsList.size()) {

                            Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this, "all " + count + " boxes checked", Toast.LENGTH_SHORT).show();

                            //here start a service save all this , create document for user.
                            //and sharedpreferences as well.

                            //here we saved all in sharedpreferences //create 4 pin id.


                            //problem is when this admin want to register as user for other admin.
                            //so we need to put label, then pull according to corresponding lable.

                            //two tag,
                            // tag ONE , mean user was admin, and added user under its tree
                            // tag TWO , mean user was user , and want to become admin.
                            //must be done at first phase.

                            //create new shared pref pool

                            SharedPreferences prefs = getSharedPreferences(
                                    "com.example.finalV8_punchCard."+user_phone_asAdmin, Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = prefs.edit(); // we need to know, which preferences belong to which admin,
                            //if user registered to another admin.

                            editor.putString("final_User_Name",user_name_asAdmin);
                            editor.putString("final_User_Phone",user_phone_asAdmin);
                            editor.putString("final_Admin_Name", user_name_asAdmin);
                            editor.putString("final_Admin_Phone",user_phone_asAdmin);
                            //editor.putString("final_User_Picture", storageReference.toString());

                            editor.commit();



                            Intent intent = new Intent(RegAdmin_asAdmin_Profile_Activity.this, Add_User_Activity.class);
                            startActivity(intent);




                        } else {

                            Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this, "only " + count + " boxes checked , size list " + adminDetailsList.size(), Toast.LENGTH_SHORT).show();
                            ;
                        }

                    }

                }else { //please setup image

                    Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this,"please set image",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //populate data
        if (wifiName != null) {
            populateData(wifiName, wifiBssid, streetName);
        }
        //recyclerView
        //initRecycler();


        //wifi listener


        Intent intentWifi = new Intent();

        intentWifi.setAction("com.example.afinal.fingerPrint_Login.register.WifiReceiver");
        sendBroadcast(intentWifi);

        // https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial

        // https://stackoverflow.com/questions/5888502/how-to-detect-when-wifi-connection-has-been-established-in-android

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.deleteObserver(this);
    }

    //for image loader.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK){

            Uri uri = null;

            if(data!=null){

                uri = data.getData();

                showImage(uri);

                imageSetupTrue=true;

            }
        }

    }

    private void showImage(Uri uri) {

        circleImageView.setImageURI(uri);

        Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this,"image setup", Toast.LENGTH_SHORT).show();

    }
//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void populateData(String wifiName, String wifiBssid, String streetName) {

        //adminDetailsList.add(new AdminDetail())

        //circleImageView.setImageResource(R.drawable.ic_location_on_black_24dp);

        Log.i("checkkLocation", "6");

        adminDetailsList.add(new AdminDetail(wifiName, "drawable/ic_wifi_black_24dp"));
        adminDetailsList.add(new AdminDetail(wifiBssid, "drawable/ic_wifi_lock_black_24dp"));
        initRecycler();



    }

    private void initRecycler() {

        recyclerView = findViewById(R.id.admin_Profile_recyclerViewiD);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_Admin_Profile_Adapter = new RecyclerView_Admin_Profile_Adapter(RegAdmin_asAdmin_Profile_Activity.this, adminDetailsList);

        Log.i("checkkLocation", "8");

        recyclerView.setAdapter(recyclerView_Admin_Profile_Adapter);
        recyclerView_Admin_Profile_Adapter.setPassResult_checkBox_interface(new PassResult_CheckBox_Interface() {
            @Override
            public void passingArray(ArrayList<AdminDetail> adminDetails) {
                //returned list.

                returnAdminDetailList = adminDetails;

            }
        });

        //setting up wifi if not initially setup.
        presenter.getWifiNow();

    }

    private String checkBSSIDStep() {


        String bssidName ="";
        return bssidName;
    }

    private String checkWifiStep1() {


        String name = "";



        return name;
    }

    @Override
    public void update(Observable observable, Object o) {

        Log.i("checkFlowData ", "1");

        if(observable instanceof Presenter_RegAdmin_asAdmin_Profile_Activity){
            Log.i("checkFlowData ", "2");
            //keep listening for location, wifi provided, and

            Map<String,String> locationHere = ((Presenter_RegAdmin_asAdmin_Profile_Activity) observable).getRemapReturnLocation();

            if(locationHere!=null){
                Log.i("checkFlowData ", "3 , location not null");
                Geocoder geocoder = new Geocoder(RegAdmin_asAdmin_Profile_Activity.this, Locale.getDefault());

                Double latitudeHere =null;
                Double longitudeHere =null;

                for(Map.Entry<String,String> kk: locationHere.entrySet()){

                    if(kk.getKey().equals("latitudeHere")){

                      //  locationMapFinal.put("latitudeFinal" ,kk.getValue());
                        latitudeHere = Double.valueOf(kk.getValue());
                    }
                    if(kk.getKey().equals("longitudeHere")){

                      //  locationMapFinal.put("longitudeFinal", kk.getValue());
                        longitudeHere = Double.valueOf(kk.getValue());
                    }


                    if(latitudeHere!=null && longitudeHere!=null ) {
                        try {
                            streetName = geocoder.getFromLocation(latitudeHere, longitudeHere, 1).get(0).getSubThoroughfare();

                            Log.i("checkFlowData ", "4 , streetName: "+streetName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        adminDetailsList.add(new AdminDetail(streetName, "drawable/ic_location_on_black_24dp"));
                        recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
                       // recyclerView.setAdapter(recyclerView_Admin_Profile_Adapter);
                        recyclerView_Admin_Profile_Adapter.setPassResult_checkBox_interface(new PassResult_CheckBox_Interface() {
                            @Override
                            public void passingArray(ArrayList<AdminDetail> adminDetails) {
                                //returned list.

                                returnAdminDetailList = adminDetails;
                            }
                        });


                        presenter.stopListening(mLocationManager);
                    }
                }


            }

            Boolean wifiHere = ((Presenter_RegAdmin_asAdmin_Profile_Activity) observable).getReturnMapWifi();

            if(wifiHere){

                Log.i("checkFlowData ", "4 , wifi status: "+wifiHere);


                Map<String,String> wifiresult = ((Presenter_RegAdmin_asAdmin_Profile_Activity) observable).getwifiResult();


                for(Map.Entry<String,String> kk : wifiresult.entrySet()){

                    Log.i("checkFlowData ", "5 , wifi status: "+kk.getValue());

                    if(kk.getKey().equals("SSID")){

                        wifiSSIDHere = kk.getValue();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                adminDetailsList.get(0).setTextShow(wifiSSIDHere);
                                recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    if(kk.getKey().equals("BSSID")){

                        wifiBSSIDHere = kk.getValue();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                adminDetailsList.get(1).setTextShow(wifiBSSIDHere);
                                recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }


            }




        }

    }


}
