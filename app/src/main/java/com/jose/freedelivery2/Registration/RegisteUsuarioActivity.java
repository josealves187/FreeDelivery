package com.jose.freedelivery2.Registration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jose.freedelivery2.Model.Usuarios;
import com.jose.freedelivery2.Login.LoginActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Dados.DadosUsuarioActivity;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;

public class RegisteUsuarioActivity extends AppCompatActivity {

    private BootstrapEditText edtEmail;
    private BootstrapEditText edtPassword;
    private BootstrapEditText edtConfirmPassword;
    private BootstrapButton btnRegister;
    private BootstrapButton btnCancelaLog;


    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth autenticacao;

    private Usuarios user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_usuario);

        //Tooba
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inicializaComponentes();
        database = FirebaseDatabase.getInstance();
        esconderteclado();



        btnCancelaLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaTela1();
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderteclado();
                if (edtEmail.getText().toString().equals("")) {
//                    Toast.makeText(getApplicationContext(), "Insira o seu E-mail!", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Insira o seu E-mail!");
                    edtEmail.requestFocus();
                } else if (edtPassword.getText().toString().equals("") && edtConfirmPassword.getText().toString().equals("")) {
//                    Toast.makeText(getApplicationContext(), "Insira sua senha!", Toast.LENGTH_SHORT).show();
                    edtPassword.setError("Insira sua senha!");
                    edtConfirmPassword.setError("Confirma senha");
                    edtConfirmPassword.requestFocus();
                    edtPassword.requestFocus();


                } else if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    user = new Usuarios();
                    user.setEmail(edtEmail.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    user.setTipoUsuario("comum");

                    cadastrarUsuario(user);

                } else {
                    Toast.makeText(RegisteUsuarioActivity.this, "As senhas não correspondem!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void cadastrarUsuario(final Usuarios user) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Colocando um metodo dentro do outro

                            inserirFuncionarioDatabase(user);
                            telaUsuario();
                            Toast.makeText(RegisteUsuarioActivity.this, "",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = " Digite Uma senha Mais Forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = " Por Favor, Digite Um Email Válido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar usuário!" + e.getMessage();
                                e.printStackTrace();
                            }


                            Toast.makeText(RegisteUsuarioActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    private boolean inserirFuncionarioDatabase(Usuarios user){

        try {
            reference = ConfiguracaoFirebase.getFirebase().child("Usuario");
            String key = reference.push().getKey();
            user.setKeyFucionario(key);
            reference.child(key).setValue(user);
            return true;

        }catch (Exception e){
            Toast.makeText(this, "Erro ao salva Usuario!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }




    public void esconderteclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void telaUsuario() {
        startActivity(new Intent(RegisteUsuarioActivity.this, DadosUsuarioActivity.class));
        finish();
    }

    private void chamaTela1() {
        Intent i = new Intent(RegisteUsuarioActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }


    private void inicializaComponentes() {

        btnRegister = (BootstrapButton) findViewById(R.id.btnRegister);
        btnCancelaLog = (BootstrapButton)findViewById(R.id.btnCancelaLog);
        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (BootstrapEditText) findViewById(R.id.edtConfirmPassword);

    }
}
