package com.jose.freedelivery2.TelaAdm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.jose.freedelivery2.SettingPending.PendingAdmin;
import com.jose.freedelivery2.Login.LoginActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Registration.RegisterActivity;
import com.jose.freedelivery2.Telas_Oferta.Add_Oferta_Activity;
import com.jose.freedelivery2.Telas_Oferta.Exibi_Oferta_Activity;

public class TelaAdmActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference referenceFirebase;
    private LinearLayout Add_fucionario;
    private LinearLayout pedidos;
    private LinearLayout Add_oferta;
    private LinearLayout excluir_prdutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adm);

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        setSupportActionBar(toolbar);

        autenticacao = FirebaseAuth.getInstance();
        referenceFirebase = FirebaseDatabase.getInstance().getReference();

    //recuperando View
        RecuperandoDado();

        Add_fucionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaAdmActivity.this, RegisterActivity.class));
            }
        });

        pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaAdmActivity.this, PendingAdmin.class));
            }
        });

        Add_oferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaAdmActivity.this, Add_Oferta_Activity.class));
            }
        });

        excluir_prdutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TelaAdmActivity.this, Exibi_Oferta_Activity.class));

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case  R.id.action_sair_admin:
                desconetarUsuario();
                break;
            case  R.id.action_add_usuario:
                addFucionario();
                break;
            case R.id.action_Confinguracao:
                abriPedidoFinalizados();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFucionario() {
        startActivity(new Intent(TelaAdmActivity.this,RegisterActivity.class));

    }
    private void abriPedidoFinalizados() {
        startActivity(new Intent(TelaAdmActivity.this, PendingAdmin.class));
    }

    private void desconetarUsuario() {
        try {
            autenticacao.signOut();
            autenticacao.signOut();
            Intent i = new Intent(TelaAdmActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RecuperandoDado() {
        Add_fucionario = (LinearLayout) findViewById(R.id.Add_fucionario);
        Add_oferta = (LinearLayout)findViewById(R.id.Add_oferta);
        pedidos = (LinearLayout) findViewById(R.id.pedidos);
        excluir_prdutor = (LinearLayout) findViewById(R.id.excluir_prdutor);
    }
}