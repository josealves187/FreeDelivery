package com.jose.freedelivery2.Registration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.TelaAdm.TelaAdmActivity;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.Preferencias;

public class RegisterActivity extends AppCompatActivity {

    private BootstrapEditText edtName;
    private BootstrapEditText edtEmail;
    private BootstrapEditText edtPassword;
    private BootstrapEditText edtConfirmPassword;
    private BootstrapEditText edtCpf;
    private BootstrapButton btnRegister;

    private RadioGroup radioGroup;
    private RadioButton rbEntregador;
    private RadioButton rbCaixa;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth autenticacao;


    private Usuarios usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ADD Toobar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Add Fucionario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializando os componetes
        inicializaComponentes();
        database = FirebaseDatabase.getInstance();

        esconderteclado();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esconderteclado();
                if (edtName.getText().toString().equals("") || edtEmail.getText().toString().equals("")
                        || edtCpf.getText().toString().equals("")) {
                    edtName.setError("Por favor, preencha o nome!");
                    edtEmail.setError("Por favor, preencha o Email!");
                    edtCpf.setError("Por favor, preencha o CPF!");
                    edtName.requestFocus();
                    edtEmail.requestFocus();
                    edtCpf.requestFocus();
                } else if (edtPassword.getText().toString().equals("") || edtConfirmPassword.getText().toString().equals("")) {
                    edtPassword.setError("Por favor, preencha o Senha!");
                    edtConfirmPassword.setError("Por favor, Confirma senha!");
                    edtPassword.requestFocus();
                    edtConfirmPassword.requestFocus();

                } else if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setName(edtName.getText().toString());
                    usuarios.setPassword(edtPassword.getText().toString());
                    usuarios.setCpf(edtCpf.getText().toString());

                    if (rbCaixa.isChecked()) {
                        usuarios.setTipoUsuario("Caixa");
                    } else if (rbEntregador.isChecked()) {
                        usuarios.setTipoUsuario("Entregado");
                    }
                    cadastrarUsuario(usuarios);

                } else {
                    Toast.makeText(RegisterActivity.this, "As senhas não correspondem!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    private void cadastrarUsuario(final Usuarios usuarios) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuarios.getEmail(), usuarios.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Colocando um metodo dentro do outro
                            inserirFuncionarioDatabase(usuarios);

                            finish();

                            //abri tela pricipal
                            telaAdmin();

                            Toast.makeText(RegisterActivity.this, "Cadastro Criado com Sucesso!",
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


                            Toast.makeText(RegisterActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    private boolean inserirFuncionarioDatabase(Usuarios usuarios) {

        try {
            reference = ConfiguracaoFirebase.getFirebase().child("Usuario");
            reference.push().setValue(usuarios);
            Toast.makeText(this, "Usuarios cadastrado sucesso!", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            Toast.makeText(this, "Erro ao salva usuarios!", Toast.LENGTH_LONG).show();
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

    private void telaAdmin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        Preferencias preferencias = new Preferencias(RegisterActivity.this);

        Intent i = new Intent(RegisterActivity.this, TelaAdmActivity.class);
        startActivity(i);
        finish();


    }


    private void inicializaComponentes() {

        radioGroup = (RadioGroup)findViewById(R.id.rdGrupo);
        rbCaixa = (RadioButton) findViewById(R.id.Caixa);
        rbEntregador = (RadioButton) findViewById(R.id.Entregador);
        btnRegister = (BootstrapButton) findViewById(R.id.btnRegister);
        edtName = (BootstrapEditText) findViewById(R.id.edtName);
        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtCpf = (BootstrapEditText) findViewById(R.id.edtCpf);
        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (BootstrapEditText) findViewById(R.id.edtConfirmPassword);

    }
}
