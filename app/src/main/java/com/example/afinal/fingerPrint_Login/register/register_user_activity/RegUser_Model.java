package com.example.afinal.fingerPrint_Login.register.register_user_activity;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Observable;

public class RegUser_Model extends Observable implements  RegUser_Model_Interface {

private boolean checkUserDocStatus;

    @Override
    public boolean checkUserDoc_Model(String name, String phone, String adminName, String adminPhone) {

        checkUserDocStatus=false;

        // check inside, wrong path

        final CollectionReference cR_uid_employee_this = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document((adminName+adminPhone)+"doc").collection("all_employee_thisAdmin_collection");


        //Query query_CheckDoc = cR_uid_employee_this.whereEqualTo("phone",phone);

        Query query1 = cR_uid_employee_this.whereEqualTo("phone",phone);

        Log.i("checkUserReg Flow: ", "[Model] , 29 , checkUserMode");

        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    Log.i("checkUserReg Flow: ", "[Model] , 30 , task successfull");

                    //should return 1 document only

                    int size = task.getResult().getDocuments().size();

                    if(size==1){

                        //checkUserDocStatus=false; //means user document already existed

                        Log.i("checkUserReg Flow: ", "[Model] , 31 , task existed");
                        setNewReturn(false);


                    }else if(size==0){

                        Log.i("checkUserReg Flow: ", "[Model] , 31 , no document, can create");

                        setNewReturn(true);
                       // checkUserDocStatus=true;
                    }else {
                        Log.i("checkUserReg Flow: ", "[Model] , 32 , fault, dont create");
                        //checkUserDocStatus=false;

                        setNewReturn(false);

                     }

                }
            }
        });

        Log.i("checkUserReg Flow: ", "[Model] , 33 , task checkUserDocStatus: "+ checkUserDocStatus);


        //does it always return this first? >> yes it does return first, we need to update and tell observer.
        return checkUserDocStatus;
    }

    //this is where we tell observer
    private void setNewReturn(boolean b) {

        Log.i("checkUserReg Flow: ", "[Model] , 34 , setReturn b: "+ b + " ,checkUserDocStatus : "+ checkUserDocStatus);


        if(b==true) {

            Log.i("checkUserReg Flow: ", "[Model] , 35 , task checkUserDocStatus: "+ checkUserDocStatus);

            checkUserDocStatus = b;

            setChanged();
            notifyObservers();

            //here we set the document, shared preferences.



        }else {

            Log.i("checkUserReg Flow: ", "[Model] , 36 , task checkUserDocStatus: "+ checkUserDocStatus);

            checkUserDocStatus = false;

            setChanged();
            notifyObservers();
        }

    }



    @Override
    public boolean getReturnDoc_Updated() {

        Log.i("checkUserReg Flow: ", "[Model] , 37 , task checkUserDocStatus: "+ checkUserDocStatus);


        return  checkUserDocStatus;
    }
}
