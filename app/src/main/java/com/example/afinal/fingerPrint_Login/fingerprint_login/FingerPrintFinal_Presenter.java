package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.afinal.fingerPrint_Login.PassResult;
import com.example.afinal.fingerPrint_Login.oop.OnServerTime_Interface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

class FingerPrintFinal_Presenter extends Observable {

    private String resultFinal;

    private FingerprintManager fingerprintManager;

    private FingerPrintFinal_Model model_fingerPrint;

    private FingerPrintFinal_View_Interface view_interface;

    private Context mContext;

    private Activity mActivity;

    private String dateAndTimeNow;

    private Map<String, Object> returnMap;

//    private String nameUser;
//    private String globalAdminPhoneHere;
//    private String phoneUser;
//    private String globalAdminNameHere;

    // location user.
    private final int REQUEST_LOCATION_PERMISSION = 1;

    private static final int READ_REQUEST_CODE = 42;

    private String user_StreetName;
    private Map<String, Object> remapLocation;



    // private LocationManager mLocationManager;


    public FingerPrintFinal_Presenter(FingerPrintFinal_View_Interface view_interface, Activity activity) {

        mActivity =activity;
        returnMap = new HashMap<>();
        remapLocation = new HashMap<>();
        dateAndTimeNow="";
        Log.i("checkFinalFlow : ", " 16 fingerprint presenter constructor() ");

        this.view_interface = view_interface;
        mContext = ((AppCompatActivity)view_interface).getApplicationContext();
        resultFinal = "";
    }

    public void checkSupportedDevice() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //run
            Log.i("checkFinalFlow : ", " 17 presenter, checkSupportedDevice() ");
            fingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);

            model_fingerPrint =new FingerPrintFinal_Model(mContext);

            if(model_fingerPrint!=null){


               startFingerPrintAuth();
            }


        }else {
            //abort operation return


            Log.i("checkFinal : ", " flow 7 ,presenter, checkSupporedDevice(), if null ");
            // update UI here
            //  getResult();

        }
    }


    private void startFingerPrintAuth() {

        //how about we start listening, but we dont need live data,
        //we just need one time result.
        //but what if failed attempt, mistake fingerprint, need to try again.
        //loop 3 times to test, then force to try again with main button

        //we can setup interface result here.
        Log.i("checkFinalFlow : ", " 18 presenter,startFingerPrintAuth(), before");
        model_fingerPrint.startAuthFingerPrint(fingerprintManager);
        Log.i("checkFinalFlow : ", " 19 presenter,startFingerPrintAuth(), after");


        model_fingerPrint.setPassResult(new PassResult() {
            @Override
            public void passingResult(String result) {

                if(!result.equals("success verified")){

                    model_fingerPrint.stopListening();

                    returnToRequest("try again");



                }else {

                    returnToRequest(result);
                }

            }
        });


//        model_fingerPrint.setPassResult(new PassResult() {
//            @Override
//            public void passingResult(String result) {
//                //this will run after result passed.
//
//                Log.i("checkk flow: ","11");
//                //returnToRequest(result);
//
//            }
//        });

        //if success will return here.
        //




    }

    public void stopListetingFingerprint(){

        model_fingerPrint.stopListening();
    }

    private void returnToRequest(String result) {

        //we could use same design, but we suppose to use same node presenter to call for result.

        Log.i("checkFinalFlow : ", " 19 presenter, returnToRequest()");
        resultFinal=result;

        setChanged();
        notifyObservers();
    }

    //getter method for observer


    public String getFinalStringResult() {

        Log.i("checkFinalFlow : ", " 20 presenter, return result");

        return resultFinal;
    }

    //
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

                                //onServerTime_interface.onSuccess(timm);

                                Date date = new Date(timm); //if in wrong timezone, need to setup

                                //dayNow  = (date.toString()).substring(0,3);

                                dateAndTimeNow = date.toString();

                                setChanged();
                                notifyObservers();

                                Log.i("checkFinalFlow : ", " 11v1 getServerTimeNow(), getting the time: "+ date);

                                Log.i("checkFinalFlow : ", " 12 getServerTimeNow(), getting the time");

                                //Toast.makeText(FingerPrint_LogIn_Final_Activity.this,"time now is: "+ date +" day:"+dayNow, Toast.LENGTH_LONG).show();
                            }else {

                                //must handle this. in case server dont return time, how do we insert timestamp?
                                Log.i("checkFinalFlow : ", " 13 getServerTimeNow(), no time recorded from server");

                                //maybe we can get user timestamp,

                                // getBackupTimeFromUser();

                                dateAndTimeNow="";

                                //onServerTime_interface.onFailed();
                            }



                        }else {

                            Log.i("checkFinalFlow : ", " 14 getServerTimeNow(), task failed");

                            dateAndTimeNow="";


//
//                            if(dayNow==null){
//
//                                //getBackupTimeFromUser(); ,, need to request again.
//                            }

                        }

                    }
                });

        return;

    }


    public String getDateAndTimeNow() {
        if(dateAndTimeNow!=null|| dateAndTimeNow!=""|| !dateAndTimeNow.equals("")) {
            return dateAndTimeNow;
        }else {
            return "";
        }
    }

   public void getCurrent_User_Admin_Server_Value(String nameUser, String phoneUser, String globalAdminPhoneHere, String globalAdminNameHere) { // this probably finsih later

        //assume shared preferences got value. pass from fragment.
        if(nameUser!=null && phoneUser!=null && globalAdminPhoneHere!=null && globalAdminNameHere!=null ){

            //this still can fail. go to admin document.
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                    .document(globalAdminNameHere+globalAdminPhoneHere+"doc");

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        Map<String, Object> remap;

                        remap = task.getResult().getData(); //problem with this, is this always run at the if we do other back stack change.

                        //remap.size();
                        //assume same,
                        int j =0;

                        String locationConstraint ="";
                        String latitudeConstraint = "";
                        String longitudeConstraint = "";
                        String bssidConstraint ="";
                        String ssidConstraint ="";
                        String morningConstraint="";
                        String eveningConstraint="";
                        String streetConstraint="";


                        if(remap!=null) {
                            for (Map.Entry<String, Object> kk : remap.entrySet()) {

                                j++;
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

                                Log.i("checkFirestoreDid","ssid:"+ssidConstraint);

                            }

                            int sizeConstraintFromServer = j; //how to know this is finish looping. or we just check all data.

                            returnMap.put("location", locationConstraint);
                            returnMap.put("latitude", latitudeConstraint);
                            returnMap.put("longitude", longitudeConstraint);
                            returnMap.put("morning_constraint",morningConstraint);
                            returnMap.put("evening_constraint", eveningConstraint);
                            returnMap.put("admin_street_name",streetConstraint);
                            returnMap.put("bssid",bssidConstraint);
                            returnMap.put("ssid",ssidConstraint);

                            setChanged();
                            notifyObservers();
                        }

                    }else {



                    }

                }
            });

        }




        return;

    }

    // getting location section
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }


    public Map<String, Object> getReturnMap() {
        return returnMap;
    }

    public Map<String, Object> getRemapLocation() {
        return remapLocation;
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission(final LocationManager mLocationManager) {

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            //       Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                            if(location!=null) {
                                String userLatitude = String.valueOf(location.getLatitude());
                                String userLongitude = String.valueOf(location.getLongitude());

                                remapLocation.put("userLatitude", userLatitude);
                                remapLocation.put("userLongitude", userLongitude);

                                Log.i("locationListener","11");

                                setChanged();
                                notifyObservers();
                            }
//
//                            Double lat = location.getLatitude();
//                            Double longitude = location.getLongitude();
//
//                            Log.i("checkkLocation", "3");

//                            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//
//                            try {
//
//                                Log.i("checkkLocation", "4");
//
//                                user_StreetName = geocoder.getFromLocation(lat, longitude, 1).get(0).getThoroughfare();
//
//                                Log.i("checkkLocation", "5 " + user_StreetName);
//
//
//
//                                if(user_StreetName !=null|| user_StreetName !=""){
//
//                                    mLocationManager.removeUpdates(this);
//                                }
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

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

            //EasyPermissions.requestPermissions((Activity) mContext, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
            EasyPermissions.requestPermissions(mActivity , "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }

        return;
    }
}
