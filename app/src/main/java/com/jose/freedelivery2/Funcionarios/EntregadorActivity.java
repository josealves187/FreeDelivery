package com.jose.freedelivery2.Funcionarios;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.SettingPending.ConfigurationEntregadorActivity;
import com.jose.freedelivery2.Model.Pedido;
import com.jose.freedelivery2.Login.LoginActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.AdapterPedido;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;
import com.jose.freedelivery2.Update.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class EntregadorActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_entregador);

        //Configurações iniciais
        inicializarComponentes();
        autenticacao = FirebaseAuth.getInstance();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configuração Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Free-Delivery");
        setSupportActionBar(toolbar);


        //Configura recyclerview
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(pedidos, this);
        recyclerPedidos.setAdapter(adapterPedido);

        recuperarPedidos();


        //Adiciona evento de clique no recyclerview
        recyclerPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Pedido pedidoSelecionado = pedidos.get(position);
                                Intent i = new Intent(EntregadorActivity.this, SignUpActivity.class);
                                i.putExtra("enderecoselecionador", pedidoSelecionado);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                final Pedido pedido = pedidos.get(position);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(EntregadorActivity.this);
                                dialog.setTitle("Comfirmar exclusão");
                                dialog.setMessage("Finaliza entregar: " + pedido.getNome() + " ?");
                                dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (pedidos.remove(pedido)) {
                                            pedido.setStatus("finalizado");
                                            pedido.atualizarStatus();
                                            removePedidos();
                                            Toast.makeText(EntregadorActivity.this,
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
                .equalTo("confirmado");

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
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entregado, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_sair_entre:
                desconetarUsuario();
                break;
            case R.id.action_Confinguracaoe:
                abriConfiguaração();

        }
        return super.onOptionsItemSelected(item);
    }


    private void desconetarUsuario() {
        try {

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(EntregadorActivity.this,
                    "Usuário Desconectado!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(EntregadorActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abriConfiguaração() {
        startActivity(new Intent(EntregadorActivity.this, ConfigurationEntregadorActivity.class));
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

