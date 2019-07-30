package com.jose.freedelivery2.Funcionarios;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.SettingPending.SettingOpCAixa;
import com.jose.freedelivery2.Model.User;
import com.jose.freedelivery2.Dados.ExibirAddressActivity;
import com.jose.freedelivery2.Login.LoginActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.AdapterUsuario;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class OpCActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseRef;
    private MaterialSearchView searchView;
    private RecyclerView recyclerViewUsuario;
    private List<User> users = new ArrayList<>();
    private AdapterUsuario adapterUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_c);

        inicializaComponetes();
        autenticacao = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Busca - Clientes");
        setSupportActionBar(toolbar);

        //Configura recyclerview
        recyclerViewUsuario.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsuario.setHasFixedSize(true);
        adapterUsuario = new AdapterUsuario(users);
        recyclerViewUsuario.setAdapter(adapterUsuario);

        //Recupera Usuario
        recuperarUsuario();

        //Configuração do search view
        searchView.setHint("Pesquisar usuario");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarUsuario( newText );
                return true;
            }
        });


        //Configurar evento de clique
        recyclerViewUsuario.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerViewUsuario,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                                User usuarioSelecionado = users.get(position);
                                Intent i = new Intent(OpCActivity.this, ExibirAddressActivity.class);
                                i.putExtra("UsuarioComu", usuarioSelecionado);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    private void pesquisarUsuario(String pesquisa){

        DatabaseReference usuarioRef = firebaseRef
                .child("UsuarioComu");
        Query query = usuarioRef.orderByChild("cpf")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff" );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    users.add( ds.getValue(User.class) );
                }

                adapterUsuario.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void recuperarUsuario() {

        DatabaseReference usuarioRef = firebaseRef.child("UsuarioComu");
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    users.add( ds.getValue(User.class) );
                }

                adapterUsuario.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opcaixa, menu);

        //Configurar botao pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_sair_caixa:
                desconetarUsuario();
                break;
            case R.id.menuPesquisa:

                break;
            case R.id.action_Confinguracao:
                abriConfiguaração();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abriConfiguaração() {
        startActivity(new Intent(OpCActivity.this, SettingOpCAixa.class));
    }
    private void inicializaComponetes(){
        searchView = findViewById(R.id.materialSearchView);
        recyclerViewUsuario = findViewById(R.id.recyclerUsuario);
    }

    private void desconetarUsuario() {
        try {
            autenticacao.signOut();
            Intent i = new Intent(OpCActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



