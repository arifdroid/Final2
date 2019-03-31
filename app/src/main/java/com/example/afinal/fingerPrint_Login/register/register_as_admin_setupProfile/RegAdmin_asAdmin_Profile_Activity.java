package com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.afinal.fingerPrint_Login.register.register_as_admin_add_userList.Add_User_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RegAdmin_asAdmin_Profile_Activity extends AppCompatActivity {

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_admin_as_admin__profile_);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        returnAdminDetailList = new ArrayList<>();

        floatingActionButton = findViewById(R.id.admin_Profile_fButtoniD);
        circleImageView = findViewById(R.id.admin_Profile_circleImageViewID);
        textViewName = findViewById(R.id.admin_Profile_textViewNameiD);
        textViewPhone = findViewById(R.id.admin_Profile_textViewPhoneiD);

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


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        adminDetailsList = new ArrayList<>();

        //process data getWifi
        String wifiName = checkWifiStep1();
        String wifiBssid = checkBSSIDStep();

        //get Location

        requestLocationPermission();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
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

                        Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this, "only " + count + " boxes checked , size list "+ adminDetailsList.size() , Toast.LENGTH_SHORT).show();
                        ;
                    }

                }

            }
        });


        //populate data
        if (wifiName != null) {
            populateData(wifiName, wifiBssid, streetName);
        }
        //recyclerView
        //initRecycler();
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

            }
        }

    }

    private void showImage(Uri uri) {

        circleImageView.setImageURI(uri);

        Toast.makeText(RegAdmin_asAdmin_Profile_Activity.this,"image setup", Toast.LENGTH_SHORT).show();

    }

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

                    Double lat = location.getLatitude();
                    Double longitude = location.getLongitude();

                    Log.i("checkkLocation", "3");

                    Geocoder geocoder = new Geocoder(RegAdmin_asAdmin_Profile_Activity.this, Locale.getDefault());

                    try {

                        Log.i("checkkLocation", "4");

                        streetName = geocoder.getFromLocation(lat, longitude, 1).get(0).getThoroughfare();

                        Log.i("checkkLocation", "5 " + streetName);

                        adminDetailsList.add(new AdminDetail(streetName, "drawable/ic_location_on_black_24dp"));
                        recyclerView_Admin_Profile_Adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(recyclerView_Admin_Profile_Adapter);
                        recyclerView_Admin_Profile_Adapter.setPassResult_checkBox_interface(new PassResult_CheckBox_Interface() {
                            @Override
                            public void passingArray(ArrayList<AdminDetail> adminDetails) {
                                //returned list.

                                returnAdminDetailList = adminDetails;
                            }
                        });


                        if(streetName!=null|| streetName!=""){

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

    }

    private String checkBSSIDStep() {


        String bssidName = wifiInfo.getBSSID();

        return bssidName;
    }

    private String checkWifiStep1() {


        String name = wifiInfo.getSSID();



        return name;
    }

}
