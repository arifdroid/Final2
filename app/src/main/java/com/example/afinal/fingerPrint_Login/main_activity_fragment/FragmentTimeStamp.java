package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afinal.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentTimeStamp extends Fragment {

    private LineChart chart;

    public FragmentTimeStamp() {
    }


    //setup collection reference

    private CollectionReference collectionReferenceTest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.bottom_nav_timestamp_fragment, container, false);
        //textView = rootView.findViewById(R.id.bottom_nav_fragment_timeStamp_textView);

        chart = rootView.findViewById(R.id.chartiD);

        collectionReferenceTest = FirebaseFirestore.getInstance().collection("all_admin_doc_collections")
                .document("ariff+60190_doc")
                .collection("all_employee_thisAdmin_collection");

        getTimeStampDataNow(collectionReferenceTest);


        return rootView;
    }

    private void getTimeStampDataNow(CollectionReference collectionReferenceTest) {

        collectionReferenceTest.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){

                        //snapshot of each employee data, pull out, every ts available for each document.
                        //process may take more than 10 secs.
                        //need to check from early check, what is today's day. then only pull relevant todays data.
                        //for faster doc, cached it, load daily only. but for testing purpose,
                        // load it all at once, update using observable.





                    }



                }else {

                    //task not successful
                }
            }
        });

    }
}
