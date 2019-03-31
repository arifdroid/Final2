package com.example.afinal.fingerPrint_Login;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.util.Log;

public class Model_fingerPrint extends FingerprintManager.AuthenticationCallback {

    //setup listener
    private PassResult passResult;

    public void setPassResult(PassResult passResult){
        this.passResult = passResult;
    }

    private Context mContext;

    public Model_fingerPrint(Context mContext) {

        Log.i("checkk flow: ","55");
        this.mContext = mContext;
    }

    public void startAuthFingerPrint(FingerprintManager fingerprintManager){

        Log.i("checkk flow: ","66");
        CancellationSignal cancellationSignal = new CancellationSignal();

        fingerprintManager.authenticate(null,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        if(passResult!=null){
            passResult.passingResult("error: "+errString);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        if(passResult!=null){
            passResult.passingResult(" "+helpString);
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Log.i("checkk flow: ","77");
        if(passResult!=null){
            Log.i("checkk flow: ","88");
            passResult.passingResult("success: "+result);
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        if(passResult!=null){
            passResult.passingResult("auth failed");
        }
    }

    public void stopListening() {
    }
}
