package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afinal.R;
import com.example.afinal.fingerPrint_Login.oop.UserCheckIn;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentUserScore extends Fragment {

    private String globalAdminNameHere = "ariff";
    private String globalAdminPhoneHere= "+60190";

    private FireStoreAdapter fireStoreAdapter;

    private FirebaseFirestore instance = FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceRating
            = instance.collection("all_admin_doc_collections")
            .document(globalAdminNameHere+globalAdminPhoneHere+"_doc").collection("all_employee_thisAdmin_collection");


    public FragmentUserScore() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.bottom_nav_user_score_fragment, container, false);
        //textView = rootView.findViewById(R.id.bottom_nav_fragment_userScore_textViewiD);

        setupRecyclerView(rootView);






        return rootView;
    }

    private void setupRecyclerView(View rootView) {

        Query query1 = collectionReferenceRating.orderBy("priority",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UserCheckIn> options = new FirestoreRecyclerOptions.Builder<UserCheckIn>()
                .setQuery(query1, UserCheckIn.class)
                .build();


        fireStoreAdapter = new FireStoreAdapter(options);

        RecyclerView recyclerView = rootView.findViewById(R.id.user_score_frag_recycleriD);

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(fireStoreAdapter);



    }

    @Override
    public void onStart() {
        super.onStart();

        //fireStoreAdapter

        fireStoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fireStoreAdapter.stopListening();
    }
}
