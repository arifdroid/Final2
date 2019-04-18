package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
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

    private final int REQUEST_LOCATION_PERMISSION = 1;

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
//
//        IntentFilter intentFilter = new IntentFilter();
//
//        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//        //registerReceiver(broadcastReceiver,)

        //broadcast receiver

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
//
//    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
//    public void requestLocationPermission() {
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            //       Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
//                    5, new LocationListener() {
//                        @Override
//                        public void onLocationChanged(Location location) {
//
//                    Double lat = location.getLatitude();
//                    Double longitude = location.getLongitude();
//
//                    Log.i("checkkLocation", "3");
//
//                    Geocoder geocoder = new Geocoder(RegAdmin_asAdmin_Profile_Activity.this, Locale.getDefault());
//
//                    try {
//
//                        Log.i("checkkLocation", "4");
//
//                        streetName = geocoder.getFromLocation(lat, longitude, 1).get(0).getThoroughfare();
//
//                        Log.i("checkkLocation", "5 " + streetName);
//
//                        adminDetailsList.add(new AdminDetail(streetName, "drawable/ic_location_on_black_24dp"));
//                        recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
//                        recyclerView.setAdapter(recyclerView_Admin_Profile_Adapter);
//                        recyclerView_Admin_Profile_Adapter.setPassResult_checkBox_interface(new PassResult_CheckBox_Interface() {
//                            @Override
//                            public void passingArray(ArrayList<AdminDetail> adminDetails) {
//                                //returned list.
//
//                                returnAdminDetailList = adminDetails;
//                            }
//                        });
//
//
//                        if(streetName!=null|| streetName!=""){
//
//                            mLocationManager.removeUpdates(this);
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                        }
//
//
//
//                        @Override
//                        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                        }
//
//                        @Override
//                        public void onProviderEnabled(String provider) {
//
//                        }
//
//                        @Override
//                        public void onProviderDisabled(String provider) {
//
//                        }
//                    });
//
//
//
//        } else {
//
//            Log.i("checkkLocation", "5");
//
//            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
//        }
//
//        return;
//    }


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


//                if(!gotWifi){
//
//                    Log.i("checkFlowData ", "6 , wifi not null");
//
//                    for(Map.Entry<String,String> kk : wifiHere.entrySet()){
//
//
//                        if(kk.getKey().equals("SSID")){
//
//                            wifiSSIDHere = kk.getValue();
//
//                            //adminDetailsList.get(0).set(new AdminDetail(wifiSSIDHere, "drawable/ic_wifi_black_24dp"));
//
//
//                                Log.i("checkFlowData ", "7, ssid: " + wifiSSIDHere);
//
//
//                                if (adminDetailsList.size() >= 2) {
//                                    Log.i("checkFlowData ", "8, somehow fail");
//                                    adminDetailsList.get(0).setTextShow(wifiSSIDHere);
//                                    recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
//                                    gotWifi=true;
//
//                            }
//                        }
//
//                        if(kk.getKey().equals("BSSID")){
//
//                            wifiBSSIDHere = kk.getValue(); //this return null
//
//                                if (adminDetailsList.size() >= 2) {
//                                    adminDetailsList.get(1).setTextShow(wifiBSSIDHere);
//                                    recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
//                                }
//
//                        }
//
//                    }
//
//                }



        }

    }

    //innerclassfor wifi change test




}
