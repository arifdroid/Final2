package com.example.afinal.fingerPrint_Login.register;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile.Presenter_RegAdmin_asAdmin_Profile_Activity;
import com.example.afinal.fingerPrint_Login.register.register_as_admin_setupProfile.RegAdmin_asAdmin_Profile_Activity;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if(info!=null && info.isConnected()){

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//


            RegAdmin_asAdmin_Profile_Activity.wifiSSIDHere = wifiInfo.getSSID();
            RegAdmin_asAdmin_Profile_Activity.wifiBSSIDHere = wifiInfo.getBSSID();

            if(wifiInfo.getSSID().length()>=7) {
                Presenter_RegAdmin_asAdmin_Profile_Activity.gotWifi = true;
            }
        }

    }
}
