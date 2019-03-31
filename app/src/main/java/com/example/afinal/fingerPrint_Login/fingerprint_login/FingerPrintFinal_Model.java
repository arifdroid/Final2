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

    //telling the same object to stop listening

    private FingerprintManager fingerprintManager;

    private   CancellationSignal cancellationSignal;



    public FingerPrintFinal_Model(Context mContext) {

        Log.i("checkFinalFlow : ", " 20 fingerprint model, constructor()");
        this.mContext = mContext;
    }


    public void startAuthFingerPrint(FingerprintManager fingerprintManager){


        this.fingerprintManager = fingerprintManager;

        //

        Log.i("checkFinalFlow : ", " 21 fingerprint model, startAuthFingerPrint()");
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(null,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        if(passResult!=null){
            passResult.passingResult("error: "+errString);

            Log.i("checkFinalFlow : ", " 22 fingerprint model, error()");

        }

        return;
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        if(passResult!=null){
            passResult.passingResult("please try again");

            Log.i("checkFinalFlow : ", " 22 fingerprint model, help()");
        }

        return;
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        if(passResult!=null){
            Log.i("checkFinalFlow : ", " 23 fingerprint model, success()");
            passResult.passingResult("success verified");
        }

        return;
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        if(passResult!=null){
            passResult.passingResult("fingerprint failed");
            Log.i("checkFinalFlow : ", " 24 fingerprint model, failed()");
        }
        return;
    }

    public void stopListening(){

        Log.i("checkFinalFlow : ", " 25 fingerprint model, stopListening()");
        cancellationSignal.cancel();
    }



}
