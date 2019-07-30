package com.jose.freedelivery2.SettingPending;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Model.Pedido;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.AdapterPedido;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;


import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class PendingAdmin extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private RecyclerView recyclerPedidos;
    private AdapterPedido adapterPedido;
    private List<Pedido> pedidos = new ArrayList<>();
    private String idUsuarioLogado;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);


        //Configurações iniciais
        inicializarComponentes();
        autenticacao = FirebaseAuth.getInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configura recyclerview
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(pedidos, this);
        recyclerPedidos.setAdapter(adapterPedido);

        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Pedido Finalizados");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recuperarPedidos();
        //Adiciona evento de clique no recyclerview
        recyclerPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                final Pedido pedido = pedidos.get(position);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(PendingAdmin.this);
                                dialog.setTitle("Comfirmar exclusão");
                                dialog.setMessage("Deseja excluir a Entrega: "+ pedido.getNome() + " ?");
                                dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (pedidos.remove(pedido)){
                                            pedido.setRemovePedidos();
                                            removePedidos();
                                            Toast.makeText(PendingAdmin.this,
                                                    "Entrega excluído com sucesso!",
                                                    Toast.LENGTH_SHORT)
                                                    .show();

                                        }
                                        adapterPedido.notifyDataSetChanged();
                                        adapterPedido.notifyItemRemoved(position);

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

    private void recuperarPedidos() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();


        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 4000);

        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos");

        Query pedidoPesquisa = pedidoRef.orderByChild("status")
                .equalTo("finalizado");

        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pedidos.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);
                    }
                    adapterPedido.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
        recyclerPedidos = findViewById(R.id.recyclerPedidosAdmin);
    }


    private void removePedidos() {


        final DatabaseReference removePedidos = firebaseRef
                .child("pedidos")
                .child(idUsuarioLogado);

        removePedidos.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot removePedido = dataSnapshot.getChildren().iterator().next();
                    removePedido.getRef().removeValue();
                }


                adapterPedido.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
