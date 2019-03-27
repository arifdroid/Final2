package com.example.afinal.fingerPrint_Login.fingerprint_login;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.afinal.fingerPrint_Login.PassResult;

import java.util.Observable;

class FingerPrintFinal_Presenter extends Observable {

    private String resultFinal;

    private FingerprintManager fingerprintManager;

    private FingerPrintFinal_Model model_fingerPrint;

    private FingerPrintFinal_View_Interface view_interface;

    private Context mContext;



    public FingerPrintFinal_Presenter(FingerPrintFinal_View_Interface view_interface) {

        this.view_interface = view_interface;
        mContext = ((AppCompatActivity)view_interface).getApplicationContext();
        resultFinal = "";
    }

    public void checkSupportedDevice() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //run
            Log.i("checkFinal : ", " flow 4 ,presenter, checkSupporedDevice(), before ");
            fingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
            Log.i("checkFinal : ", " flow 5 ,presenter, checkSupporedDevice(), after ");
            model_fingerPrint =new FingerPrintFinal_Model(mContext);

            if(model_fingerPrint!=null){

               Log.i("checkFinal : ", " flow 6,presenter, checkSupporedDevice(), if not null ");
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

        Log.i("checkFinal : ", " flow 8 ,presenter, startFingerPrintAuth(), before ");
        model_fingerPrint.startAuthFingerPrint(fingerprintManager);
        Log.i("checkFinal : ", " flow 9 ,presenter, startFingerPrintAuth(), after");


        model_fingerPrint.setPassResult(new PassResult() {
            @Override
            public void passingResult(String result) {

                if(result!=null){


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
