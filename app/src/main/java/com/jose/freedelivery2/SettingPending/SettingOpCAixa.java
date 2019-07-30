package com.jose.freedelivery2.SettingPending;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Model.Pedido;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.AdapterPendente;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SettingOpCAixa extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private RecyclerView recyclerUsuario;
    private AdapterPendente adapterPedido;
    private List<Pedido> pedidos = new ArrayList<>();
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_op_caixa);



        //Configurações iniciais
        inicializarComponentes();
        autenticacao = FirebaseAuth.getInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configuração Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Envio pendente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configura recyclerview
        recyclerUsuario.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsuario.setHasFixedSize(true);
        adapterPedido = new AdapterPendente(pedidos, this);
        recyclerUsuario.setAdapter(adapterPedido);

        recuperarPedidos();


        //Adiciona evento de clique no recyclerview
        recyclerUsuario.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerUsuario,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                            }


                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    private void recuperarPedidos() {
//        dialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage("Carregando dados")
//                .setCancelable(false)
//                .build();
//        dialog.show();
//

        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario");

        Query pedidoPesquisa = pedidoRef.orderByChild("status")
                .equalTo("pendente");

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
//                    dialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void inicializarComponentes() {
        recyclerUsuario = findViewById(R.id.recyclerUsuarioe);
    }

}
