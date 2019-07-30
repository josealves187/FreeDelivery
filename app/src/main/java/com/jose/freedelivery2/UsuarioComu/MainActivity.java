package com.jose.freedelivery2.UsuarioComu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.jose.freedelivery2.Registration.UserAddress;
import com.jose.freedelivery2.Login.LoginActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.FragmentAdapter;
import com.jose.freedelivery2.Dados.CommitmentActivity;
import com.jose.freedelivery2.Dados.DadosUsuarioActivity;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.SobreActivity;
import com.jose.freedelivery2.Telas_Oferta.Exibir_Oferta;
import com.jose.freedelivery2.Update.SignUpActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;

    private FirebaseAuth autenticacao;
    private DatabaseReference refence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de compras");
        setSupportActionBar(toolbar);


        RecuperandoDados();

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_done));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_delete));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new FragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              InseriCompromisso();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void RecuperandoDados() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_sair_User:
                desconetarUsuario();
                break;
            case R.id.action_add_Endereco:
                abriEndereco();
                break;
            case R.id.action_Confinguracao:
                abriConfiguaração();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Desconectar) {
            desconetarUsuario();

        } else if (id == R.id.nav_Endereco) {
            abriEndereco();

        } else if (id == R.id.nav_slideshow) {
            abriatualicacao();


        } else if (id == R.id.nav_tools) {
//            exlcluiUsuarioLogador();
//            chamalogin();
            ChamaOfetas();



        } else if (id == R.id.nav_share) {
            startActivity( new Intent(this, SobreActivity.class ) );

        } else if (id == R.id.nav_send) {
            EnviarEmail();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ChamaOfetas() {
        startActivity(new Intent(MainActivity.this, Exibir_Oferta.class));
    }

    private void abriConfiguaração() {
        startActivity(new Intent(MainActivity.this, UserAddress.class));
    }

    private void abriEndereco(){
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
    private void abriatualicacao(){
        startActivity(new Intent(MainActivity.this, DadosUsuarioActivity.class));
    }
    private void InseriCompromisso (){
        startActivity(new Intent(MainActivity.this, CommitmentActivity.class));
    }



    private void desconetarUsuario() {
        try {

            FirebaseAuth.getInstance().signOut();
            chamalogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chamalogin() {
        Toast.makeText(MainActivity.this,
                "Usuário Desconectado!",Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void EnviarEmail(){

       Intent email = new Intent( Intent.ACTION_SEND );
       email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jralves187@gmail.com"," " } );
       email.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo App" );
       email.putExtra(Intent.EXTRA_TEXT, "Mensagem automática" );

       //configurar apps para e-mail
       email.setType("message/rfc822");
       //email.setType("application/pdf");
       //email.setType("image/png");

       startActivity( Intent.createChooser(email, "Escolha o App de E-mail:" ) );

   }
//   private void exlcluiUsuarioLogador(){
//        String emailUsuarioLogado = autenticacao.getCurrentUser().getEmail();
//
//        refence.child("Usuario").orderByChild("email").equalTo(emailUsuarioLogado).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//
//
//                    final Usuarios usuarios = dataSnapshot1.getValue(Usuarios.class);
//
//                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                    user.delete()
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Log.d("USUARIO_EXCLUIDO", "User Deleted");
//
//                                        Toast.makeText(MainActivity.this, "Usuário excluido", Toast.LENGTH_SHORT).show();
//
//                                        refence = ConfiguracaoFirebase.getFirebase();
//                                        refence.child("usuario").child(usuarios.getKeyFucionario()).removeValue();
//
//
//                                        autenticacao.signOut();
//
//                                    }
//
//                                }
//                            });
//
//                }
//            }
//
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//
//            }
//        });
//
//
//   }

}
