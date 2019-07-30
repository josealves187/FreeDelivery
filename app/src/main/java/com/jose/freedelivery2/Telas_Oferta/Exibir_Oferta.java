package com.jose.freedelivery2.Telas_Oferta;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Adapter.AdapterOfertas;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Model.Anuncio;
import com.jose.freedelivery2.R;

import java.util.ArrayList;
import java.util.List;

public class Exibir_Oferta extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private RecyclerView recyclerViewAncuio;
    private AdapterOfertas adapterAnucio;
    private List<Anuncio> anucios = new ArrayList<>();


    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_oferta);


        //Configurações iniciais
        inicializarComponentes();
        autenticacao = FirebaseAuth.getInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Configura recyclerview
        adapterAnucio = new AdapterOfertas(anucios,this);
        recyclerViewAncuio.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAncuio.setHasFixedSize(true);
        recyclerViewAncuio.setAdapter(adapterAnucio);

        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Ofetas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recupera produtos para empresa
//        recuperarPedidos();
        recuperarProdutos();

    }

//    private void recuperarPedidos() {
//        dialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage("Carregando dados")
//                .setCancelable(false)
//                .build();
//        dialog.show();
//
//
//        Runnable progressRunnable = new Runnable() {
//            @Override
//            public void run() {
//                dialog.cancel();
//            }
//        };
//
//        Handler pdCanceller = new Handler();
//        pdCanceller.postDelayed(progressRunnable, 4000);
//
//
//            DatabaseReference ofertasRef = firebaseRef
//                    .child("anuncios")
//                    .child(idUsuarioLogado)
//
//                    ;
//
//        Query pedidoPesquisa = ofertasRef.orderByChild("nome");
//        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    anucios.clear();
//
//                    for (DataSnapshot ds : dataSnapshot.getChildren()){
//                        anucios.add(ds.getValue(Anuncio.class));
//                    }
//                    adapterOfertas.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//        }

    private void recuperarProdutos() {
        DatabaseReference ofertasRef = firebaseRef
                .child("anuncios");

        ofertasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anucios.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Anuncio anuncio = ds.getValue(Anuncio.class);
                    anucios.add(anuncio);

                }

                adapterAnucio.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
          recyclerViewAncuio = (RecyclerView)findViewById(R.id.recyclerOfertas);
    }
}
