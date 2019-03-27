package com.example.afinal.fingerPrint_Login.register.register_with_activity;

import android.os.Handler;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Observable;

public class RegAdmin_Model extends Observable implements RegAdminModel_Interface{

    private boolean returnSimulation;

    private String nameAdminCheck;
    private String phoneAdminCheck;

    public RegAdmin_Model(){

    }

    @Override
    public boolean getFromSimulation(String name, String phone) {
        returnSimulation=false;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                setNewSussesfulFecthData(true);

            }
        },4000);


        return returnSimulation;
    }

    @Override
    public boolean returnSimulation() {

        return returnSimulation;

    }

    @Override
    public boolean getFromFirebase(String name1, final String phone) {

        final String name = name1;

        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("admins_offices");

        Query query_admin = collectionReference.whereEqualTo("admin_phone",phone);

        query_admin.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    QuerySnapshot querySnapshot =task.getResult();

                    int size = querySnapshot.size();

                    if(querySnapshot.size()==1){

                        for(QueryDocumentSnapshot documentSnapshot:querySnapshot){

                            Map<String,Object> map;

                            map= documentSnapshot.getData();

                            for(Map.Entry<String,Object> remap: map.entrySet()){

                                if(remap.getKey().equals("admin_name")){

                                    nameAdminCheck= remap.getValue().toString();
                                }if(remap.getKey().equals("admin_phone")){
                                    phoneAdminCheck = remap.getValue().toString();
                                }

                            }

                        }

                        if(nameAdminCheck.equals(name)&&phoneAdminCheck.equals(phone)){

//                    returnSimulation =true;
//                    setChanged();
//                    notifyObservers();

                            setNewSussesfulFecthData(true);


                        }else {


                            setNewSussesfulFecthData(false);

                        }






                    }else {


                        setNewSussesfulFecthData(false);


                    }





                } //try again, connection problem?




            }


        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

                // noticeString="failed";



            }
        });




        return returnSimulation;
    }

    private void setNewSussesfulFecthData(boolean returned) {



        if(returned==true){

            returnSimulation = returned;
            setChanged();
            notifyObservers();

        }else {
            return;
        }
    }


}
