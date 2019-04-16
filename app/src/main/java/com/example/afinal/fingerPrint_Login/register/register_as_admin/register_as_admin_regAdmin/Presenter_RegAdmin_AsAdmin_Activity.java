package com.example.afinal.fingerPrint_Login.register.register_as_admin.register_as_admin_regAdmin;

import android.content.Context;
import android.widget.Toast;

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

    public Presenter_RegAdmin_AsAdmin_Activity(Context context){
        this.mContext= context;

    }


    public void checkCredentialWithUpdates(PhoneAuthCredential phoneAuthCredential, String name, String phone) {
        allowCreateAdmin=0;
        this.nameUser_admin=name;
        this.phoneUser_admin=phone;

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //check user withing admin document.

                    CollectionReference collectionReference = FirebaseFirestore.getInstance()
                            .collection("all_admins_collections");


                    if(phoneUser_admin!=null) {
                        Query query1 = collectionReference.whereEqualTo("phone", phoneUser_admin);

                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if(task.isSuccessful()){

                                    if(task.getResult()!=null) {

                                        if (task.getResult().isEmpty()) {

                                            //here we can add document



                                            Map<String,Object> kk = new HashMap<>();

                                            kk.put("name",nameUser_admin);
                                            kk.put("phone",phoneUser_admin);


                                            DocumentReference reference = FirebaseFirestore.getInstance()
                                                    .collection("all_admins_collections")
                                                    .document(nameUser_admin+phoneUser_admin+"collection");


                                            reference.set(kk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(mContext, "successfully registered", Toast.LENGTH_SHORT).show();

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
                                                    Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();


                                                }
                                            });

                                        } else {
                                            //toast not forward
                                            allowCreateAdmin=3;
                                            setChanged();
                                            notifyObservers();
                                            //Toast.makeText(mContext,"please try again",Toast.LENGTH_SHORT).show();

                                            //already exist.
                                        }

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


    }

    public PhoneAuthCredential getCredential() {
        return credential;
    }
}
