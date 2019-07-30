package com.jose.freedelivery2.Telas_Oferta;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Adapter.AdapterOfertas;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;
import com.jose.freedelivery2.Model.Anuncio;
import com.jose.freedelivery2.R;


import java.util.ArrayList;
import java.util.List;

public class Exibi_Oferta_Activity extends AppCompatActivity {



    private AdapterOfertas adapterOfertas;
    private List<Anuncio> ofertas = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private RecyclerView recyclerOfertas;

    private FirebaseAuth autenticacao;
    private String idUsuarioLogado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);


        //Configurações iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Ofertas da semana");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configura recyclerview
        recyclerOfertas.setLayoutManager(new LinearLayoutManager(this));
        recyclerOfertas.setHasFixedSize(true);
        adapterOfertas = new AdapterOfertas(ofertas,this);
        recyclerOfertas.setAdapter(adapterOfertas);


        //Recupera produtos para empresa
        recuperarProdutos();

        //Adiciona evento de clique no recyclerview
        recyclerOfertas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerOfertas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                final Anuncio produtoSelecionado = ofertas.get(position);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(Exibi_Oferta_Activity.this);
                                dialog.setTitle("Comfirmar exclusão");
                                dialog.setMessage("Deseja excluir a produto : "+ produtoSelecionado.getNome() + " ?");
                                dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (ofertas.remove(produtoSelecionado)){
                                            produtoSelecionado.remover();
                                            Toast.makeText(Exibi_Oferta_Activity.this,
                                                    "Produto excluído com sucesso!",
                                                    Toast.LENGTH_SHORT)
                                                    .show();

                                        }

                                    }
                                });

                                dialog.setNegativeButton("não", null);
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    private void recuperarProdutos() {
        DatabaseReference ofertasRef = firebaseRef
                .child("anuncios")
                ;

        ofertasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ofertas.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Anuncio anuncio = ds.getValue(Anuncio.class);
                    ofertas.add(anuncio);



                }
                adapterOfertas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
        recyclerOfertas = (RecyclerView)findViewById(R.id.recycleViewOferta);

    }





}
