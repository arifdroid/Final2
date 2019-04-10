package com.example.afinal.fingerPrint_Login.register.register_user_activity;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

class RegUser_Presenter extends Observable implements RegUser_Presenter_Interface, Observer {


    private RegUser_Model_Interface model_interface;

    private RegUserView_Interface view_interface;

    private boolean gotDoc;

    private String returnStatus;


    public RegUser_Presenter(RegUserView_Interface view_interface) {

        this.view_interface = view_interface;
        model_interface = new RegUser_Model();

        ((RegUser_Model) model_interface).addObserver(this);

        Log.i("checkUserReg Flow: ", "[Presenter] , 21 ,constructor");

    }


    @Override
    public void checkUserDoc(String name, String phone, String adminName, String adminPhone) {

        boolean doublecheck = checkInputValid(name,phone);

        if(doublecheck){

            boolean triplecheck = checkInputValid(adminName,adminPhone);

            if(triplecheck){

                //we need to tell this now, now it is set at false.

                Log.i("checkUserReg Flow: ", "[Presenter] , 22 ,before model");

                gotDoc = model_interface.checkUserDoc_Model(name,phone,adminName,adminPhone);

                Log.i("checkUserReg Flow: ", "[Presenter] , 22 ,after model");

                if(gotDoc){

                    Log.i("checkUserReg Flow: ", "[Presenter] , 23 ,unlikely");

                   // view_interface.checkDocResult("doc created");

                }else {

                    Log.i("checkUserReg Flow: ", "[Presenter] , 24 , initially");


                   // view_interface.checkDocResult("please wait");
                    //this maybe

                }



            }
        }

        return;

    }




    @Override
    public boolean checkInputValid(String name, String phone) {

        if((name!=null&& phone!=null)||(name!="" && phone!="")){

            Log.i("checkk UserReg: ", "99");
            return true;
        }

        Log.i("checkk UserReg: ", "00");
        return false;
    }

    @Override
    public boolean checkInputValid(String name, String phone, String code) {
        if((name!=null&& phone!=null&& code!=null)||(name!="" && phone!=""&& code!="")){

            Log.i("checkk UserReg: ", "code 1");
            return true;
        }

        Log.i("checkk UserReg: ", " code 2");
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof  RegUser_Model){

            //((RegUser_Model) o).returnCheckDoc_Updated();

            Log.i("checkUserReg Flow: ", "[Presenter] , 25 , observer update, return: "+ ((RegUser_Model) o).getReturnDoc_Updated());


            boolean updatedCheckDoc = ((RegUser_Model) o).getReturnDoc_Updated();

            if(updatedCheckDoc==true){

                Log.i("checkUserReg Flow: ", "[Presenter] , 26 , observer update, return true: ");


                gotDoc = updatedCheckDoc;

                setChanged();
                notifyObservers();

               // setBoolean(gotDoc);

               // setStatus("doc created");

            }else {

                Log.i("checkUserReg Flow: ", "[Presenter] , 27 , observer update, return: false ");

                gotDoc = false;

                setChanged();
                notifyObservers();

              //  setBoolean(gotDoc);

               // setStatus("please contact admin");
            }
            return;
        }

        return;
    }

//    private void setBoolean(Boolean b){
//
//        if()
//
//
//    }

    private void setStatus(String doc_created) {

        //sending result to ui

        Log.i("checkUserReg Flow: ", "[Presenter] , 28 , observer update, return: "+doc_created);

        if(gotDoc) {
            returnStatus = doc_created;

            Log.i("checkUserReg Flow: ", "[Presenter] , 28.1 , observer update, gotDoc: "+gotDoc + ", status:"+doc_created);

        }

        else {

            Log.i("checkUserReg Flow: ", "[Presenter] , 28.1 , observer update, gotDoc: "+gotDoc+ ", status:"+doc_created);
            returnStatus =doc_created;
        }

        return;

    }

    public String getReturnStatus() {

        Log.i("checkUserReg Flow: ", "[Presenter] , 29 , observer update, return: "+returnStatus);
        return returnStatus;
    }

    public boolean getFinally(){

        return gotDoc;
    }
}
