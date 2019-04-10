package com.example.afinal.fingerPrint_Login.register.register_with_activity;

import java.util.Observable;
import java.util.Observer;

class RegAdmin_Presenter extends Observable implements  RegAdminPresenter_Interface, Observer {

    private RegAdminViewInterface view;
    private RegAdmin_Model model;

    private boolean modelReturn;

    public RegAdmin_Presenter(RegAdminViewInterface regAdminViewInterface) {


        view = regAdminViewInterface;

        model = new RegAdmin_Model();

        model.addObserver(this);
    }

    @Override
    public boolean checkInputValid(String name, String phone) {
        modelReturn = false;
        if((name!=null&& phone!=null)||(name!="" && phone!="")){


            return true;
        }
        return false;
    }

    @Override
    public boolean checkFromFirebaseSimulation(String name, String phone) {

        // need observable
        //this is always false, unless exists, then override.

        modelReturn = model.getFromFirebase(name,phone);


        if(modelReturn){    //if this true, means, need updated result.


        }

        return false;
    }

    @Override
    public boolean checkFinalFromFirebase() {

       return modelReturn; //is this always return true
       // return false;
    }

    @Override
    public void resultPresenter(String s) {


    }

    @Override
    public void update(Observable o, Object arg) {

        if(o instanceof  RegAdmin_Model){

            boolean checkHere = ((RegAdmin_Model) o).returnSimulation();

            if(checkHere){ //if now we are getting result back.

                //need to notify change to modelReturn

                modelReturn = checkHere;

                setChanged();
                notifyObservers();


            }else {

                modelReturn = false;
               setChanged();
               notifyObservers();

            }

        }
    }


}
