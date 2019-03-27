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

        final CollectionReference cR_uid_employee_this = FirebaseFirestore.getInstance().collection("employees_to_offices")
                .document((adminName+adminPhone)).collection("uid_employee_this");


        Query query_CheckDoc = cR_uid_employee_this.whereEqualTo("phone",phone);

        Log.i("checkk UserReg: ", "1");

        query_CheckDoc.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    Log.i("checkk UserReg: ", "2");

                    //should return 1 document only

                    int size = task.getResult().getDocuments().size();

                    if(size==1){

                        checkUserDocStatus=false;
                        Log.i("checkk UserReg: ", "3");
                    }else if(size==0){
                        Log.i("checkk UserReg: ", "4");

                        setNewReturn(true);

                        checkUserDocStatus=true;
                    }else {
                        Log.i("checkk UserReg: ", "2");
                        checkUserDocStatus=false;

                     }

                }
            }
        });

        Log.i("checkk UserReg: ", " 99");


        //does it always return this first? >> yes it does return first, we need to update and tell observer.
        return checkUserDocStatus;
    }

    //this is where we tell observer
    private void setNewReturn(boolean b) {

        if(b==true) {
            checkUserDocStatus = b;

            setChanged();
            notifyObservers();

            //returnCheckDoc_Updated(b);

        }else {
            return;
        }

    }



    @Override
    public boolean getReturnDoc_Updated() {

        return  checkUserDocStatus;
    }
}
