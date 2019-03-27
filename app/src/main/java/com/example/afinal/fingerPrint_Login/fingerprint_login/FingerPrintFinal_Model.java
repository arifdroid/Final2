package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.util.Log;

import com.example.afinal.fingerPrint_Login.PassResult;

public class FingerPrintFinal_Model extends FingerprintManager.AuthenticationCallback {

    private Context mContext;

    private PassResult passResult; // just to pass data to check status

    public void setPassResult(PassResult passResult){
        this.passResult = passResult;
    }



    public FingerPrintFinal_Model(Context mContext) {

        Log.i("checkFinal : ", " flow 10 ,model, constructtor(), ");
        this.mContext = mContext;
    }


    public void startAuthFingerPrint(FingerprintManager fingerprintManager){

        Log.i("checkFinal : ", " flow 11 ,model, startAuthFingerPrint(), ");
        CancellationSignal cancellationSignal = new CancellationSignal();

        fingerprintManager.authenticate(null,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        if(passResult!=null){
            passResult.passingResult("error: "+errString);

        Log.i("checkFinal : ", " flow 12 ,model, onAuthenticationError(), ");

        }

        return;
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        if(passResult!=null){
            passResult.passingResult("please try again");
        }

        return;
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Log.i("checkFinal : ", " flow 13 ,model, onAuthenticationSucceeded(), ");
        if(passResult!=null){
            Log.i("checkFinal : ", " flow 14 ,model, onAuthenticationSucceeded(), passResult ");
            passResult.passingResult("success verified");
        }

        return;
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        if(passResult!=null){
            passResult.passingResult("fingerprint failed");
        }
        return;
    }



}
