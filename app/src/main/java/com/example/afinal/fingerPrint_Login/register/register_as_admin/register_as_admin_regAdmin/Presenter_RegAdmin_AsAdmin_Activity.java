package com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin;

import android.content.Context;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import androidx.annotation.NonNull;

public class Presenter_RegAdmin_AsAdmin_Activity extends Observable {

    private String nameUser_admin;
    private String phoneUser_admin;

    private Context mContext;
    private int allowCreateAdmin;
    private PhoneAuthCredential credential;
    private String sharedPrefs_label;

    public Presenter_RegAdmin_AsAdmin_Activity(Context context){
        this.mContext= context;
        allowCreateAdmin=0;
        return;

    }


    public void checkCredentialWithUpdates(PhoneAuthCredential phoneAuthCredential, String name, String phone) {

        this.nameUser_admin=name;
        this.phoneUser_admin=phone;

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //check user withing admin document.

                    final CollectionReference collectionReference = FirebaseFirestore.getInstance()
                            .collection("all_admins_collections");


                    if(phoneUser_admin!=null) {
                        Query query1 = collectionReference.whereEqualTo("phone", phoneUser_admin);

                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                        if(task.getResult().size()==0){

                                            //meaning no admin being registered yet with this number. thus
                                            //here we can add document, but we need to add tag, so that we know which admin
                                            //to pull this

                                            Query queryForLable = collectionReference.whereArrayContains("employee_this_admin",phoneUser_admin);

                                            queryForLable.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if(task.isSuccessful()){

                                                        if(task.getResult().size()==1){ //means already a user for another admin,
                                                            //just confirmation.

                                                            //labeled as TWO
                                                            Map<String,Object> kk = new HashMap<>();

                                                            sharedPrefs_label = "com.example.finalV8_punchCard." + phoneUser_admin;
                                                            kk.put("name",nameUser_admin);
                                                            kk.put("phone",phoneUser_admin);
                                                            kk.put("sharedPrefs_label",sharedPrefs_label);

                                                            //sharedPrefs_label = "TWO";

                                                            DocumentReference reference = FirebaseFirestore.getInstance()
                                                                    .collection("all_admins_collections")
                                                                    .document(nameUser_admin+phoneUser_admin+"collection");




                                                            reference.set(kk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if(task.isSuccessful()) {

                                                                        allowCreateAdmin=1;
                                                                        setChanged();
                                                                        notifyObservers();



                                                                    }else {

                                                                        allowCreateAdmin=2;
                                                                        setChanged();
                                                                        notifyObservers();
                                                                        //
                                                                        //Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }).addOnCanceledListener(new OnCanceledListener() {
                                                                @Override
                                                                public void onCanceled() {

                                                                    allowCreateAdmin=2;


                                                                }
                                                            });


                                                        }
                                                        if(task.getResult().size()==0){ // first time admin, user, not user for another admin

                                                            //labeled as ONE

                                                            Map<String,Object> kk = new HashMap<>();

                                                            sharedPrefs_label = "com.example.finalV8_punchCard." + phoneUser_admin;

                                                            kk.put("name",nameUser_admin);
                                                            kk.put("phone",phoneUser_admin);
                                                            kk.put("sharedPrefs_label",sharedPrefs_label); // or dont write it?


                                                            //change label to share preferences

                                                            DocumentReference reference = FirebaseFirestore.getInstance()
                                                                    .collection("all_admins_collections")
                                                                    .document(nameUser_admin+phoneUser_admin+"collection");

                                                            reference.set(kk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if(task.isSuccessful()) {

                                                                        allowCreateAdmin=1;
                                                                        setChanged();
                                                                        notifyObservers();



                                                                    }else {

                                                                        allowCreateAdmin=2;
                                                                        setChanged();
                                                                        notifyObservers();
                                                                        //
                                                                        //Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }).addOnCanceledListener(new OnCanceledListener() {
                                                                @Override
                                                                public void onCanceled() {

                                                                    allowCreateAdmin=2;


                                                                }
                                                            });


                                                        }

                                                        else { // other error., somehow more than 1



                                                        }

                                                    }
                                                    else { //task fail, need to check again


                                                    }
                                                }
                                            }).addOnCanceledListener(new OnCanceledListener() {
                                                @Override
                                                public void onCanceled() {



                                                }
                                            });
//
//
//
//                                            Map<String,Object> kk = new HashMap<>();
//
//                                            kk.put("name",nameUser_admin);
//                                            kk.put("phone",phoneUser_admin);
//
//
//                                            DocumentReference reference = FirebaseFirestore.getInstance()
//                                                    .collection("all_admins_collections")
//                                                    .document(nameUser_admin+phoneUser_admin+"collection");
//
//
//
//
//                                            reference.set(kk).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                    if(task.isSuccessful()) {
//
//                                                        allowCreateAdmin=1;
//                                                        setChanged();
//                                                        notifyObservers();
//
//
//
//                                                    }else {
//
//                                                         allowCreateAdmin=2;
//                                                        setChanged();
//                                                        notifyObservers();
//                                                        //
//                                                        //Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            }).addOnCanceledListener(new OnCanceledListener() {
//                                                @Override
//                                                public void onCanceled() {
//
//                                                    allowCreateAdmin=2;
//
//
//                                                }
//                                            });

                                        } else {
                                            //toast not forward
                                            allowCreateAdmin=3;
                                            setChanged();
                                            notifyObservers();
                                            //Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();

                                            //already exist.
                                        }


                                }else {

                                    allowCreateAdmin=2;
                                    setChanged();
                                    notifyObservers();
                                    //
                                    //toast not forward
                                }

                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {

                                allowCreateAdmin=2;
                                setChanged();
                                notifyObservers();
                                //
                            }
                        });




                    }


                }else { //task unsuccessful

                    allowCreateAdmin=2;
                    setChanged();
                    notifyObservers();
                    //
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });

        return;
    }

    public int getIfDocumentCreated(){
        return allowCreateAdmin;
    }

    public void getCredentialWithUpdates(String codeUserAdminEnter, String codeFromFirebase) {


        credential = PhoneAuthProvider.getCredential(codeUserAdminEnter,codeFromFirebase);

        if(credential!=null){
            setChanged();
            notifyObservers();
        }

        return;
    }

    public PhoneAuthCredential getCredential() {
        return credential;
    }
}
