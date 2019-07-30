package com.jose.freedelivery2.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Model.Commitment;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.CommitmentAdapter;

import java.util.ArrayList;
import java.util.List;


public class PendentesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CommitmentAdapter adapter;
    private List<Commitment> listacommitments;
    private DatabaseReference databaseReference;
    private Commitment commitment;
    private LinearLayoutManager nlinearLayoutManager;
    private FirebaseUser userAuth;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_pendentes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        PopularListaCompromisso();

        return view;
    }

    private void PopularListaCompromisso() {

        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = userAuth.getUid();

        mRecyclerView.setHasFixedSize(true);


        nlinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(nlinearLayoutManager);

        listacommitments = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("commitment").orderByChild("status").equalTo("Pendente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listacommitments.clear();

                for (DataSnapshot commitmentSnapshot : dataSnapshot.getChildren()) {
                    commitment = commitmentSnapshot.getValue(Commitment.class);
                    if (commitment.getUid().equals(uid)) {
                        listacommitments.add(commitment);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new CommitmentAdapter(listacommitments, getContext());
        mRecyclerView.setAdapter(adapter);
    }


}
