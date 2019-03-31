package com.example.afinal.fingerPrint_Login;


import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.example.afinal.fingerPrint_Login.fingerprint_login.FingerPrint_LogIn_Final_Activity;

import java.util.Observable;


public class Presenter_FingerPrint extends Observable {


    private String resultFinal;



    private Context mContext;

    private FingerprintManager fingerprintManager;

    private Model_fingerPrint model_fingerPrint;

    //constructor
    public Presenter_FingerPrint(Context context){

        Log.i("checkk flow: ","5");

        this.mContext = context;
       // checkSupportedDevice();

    }

    public void checkSupportedDevice() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //run
            Log.i("checkk flow: ","6");
           fingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);

           model_fingerPrint =new Model_fingerPrint(mContext);

           if(model_fingerPrint!=null){

               Log.i("checkk flow: ","7");
               startFingerPrintAuth();
           }


        }else {
            //abort operation return

            Log.i("checkk flow: ","8");
          //  getResult();

        }
    }

    private void startFingerPrintAuth() {

        //how about we start listening, but we dont need live data,
        //we just need one time result.
        //but what if failed attempt, mistake fingerprint, need to try again.
        //loop 3 times to test, then force to try again with main button

        //we can setup interface result here.

        Log.i("checkk flow: ","9");



        model_fingerPrint.startAuthFingerPrint(fingerprintManager);
        Log.i("checkk flow: ","10");
        model_fingerPrint.setPassResult(new PassResult() {
            @Override
            public void passingResult(String result) {
                //this will run after result passed.

                Log.i("checkk flow: ","11");

                if(!result.equals("success verified")){

                    //model_fingerPrint failed or cancelled, need to stop listening if failed.

                    model_fingerPrint.stopListening();

                    Toast.makeText(mContext,"stop listening fingerprint", Toast.LENGTH_LONG).show();

                    return;


                }

                returnToRequest(result);

            }
        });






    }

    private void returnToRequest(String result) {

        //we could use same design, but we suppose to use same node presenter to call for result.

        Log.i("checkk flow: ","12");
          resultFinal=result;

          setChanged();
          notifyObservers();
    }

    //getter method for observer


    public String getFinalStringResult() {
        return resultFinal;
    }
}
