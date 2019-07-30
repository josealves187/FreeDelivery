package com.jose.freedelivery2.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Model.Usuarios;
import com.jose.freedelivery2.Funcionarios.EntregadorActivity;
import com.jose.freedelivery2.Funcionarios.OpCActivity;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Registration.RegisteUsuarioActivity;
import com.jose.freedelivery2.TelaAdm.TelaAdmActivity;
import com.jose.freedelivery2.UsuarioComu.MainActivity;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private TextView editEmailLog;
    private TextView editrPassowodLog;
    private Button btnLoginLog;
    private Button btnCancelaLog;
    private TextView btnCadastraLog;
    private TextView EqueciMinhaSe;

    //Recuperando Botoes do layout Recupera senha
    private BootstrapButton btnSendEmail;
    private BootstrapButton btnCancelResgSen;
    private TextView editEmailRecovery;

    private ProgressDialog progressDialog;


    private Dialog dialog;
    private FirebaseUser currentUser;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;
    private DatabaseReference referenceFirebase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_geral);

        inicializaComponentes();
        referenceFirebase = FirebaseDatabase.getInstance().getReference();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();



        //verificarUsuarioLogado();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              usuarioLogado();
            }
        }, 3000);







        if (usuarioLogado()){


            init();
            showDialog();

            String email = autenticacao.getCurrentUser().getEmail();
            abriTelasPricipais(email);




        }else {
            btnLoginLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editEmailLog.getText().toString().equals("")) {
                        editEmailLog.setError("Preencha o E-mail!");
                        editEmailLog.requestFocus();
                    } else if (editrPassowodLog.getText().toString().equals("")) {
                        editrPassowodLog.setError("Preencha a Senha!");
                        editrPassowodLog.requestFocus();



                        usuarios = new Usuarios();
                        usuarios.setEmail(editEmailLog.getText().toString());
                        usuarios.setPassword(editrPassowodLog.getText().toString());


                    } else {

                        EfetuarLogin(editEmailLog.getText().toString(),
                                editrPassowodLog.getText().toString());
                    }

                }
            });

        }

        btnCadastraLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisteUsuarioActivity.class);
                startActivity(i);

            }
        });

//        btnCancelaLog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editEmailLog.setText("");
//                editrPassowodLog.setText("");
//                finish();
//
//            }
//        });

        EqueciMinhaSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Estou chamado o layout  alert na textview da tela de login em recupera senha
                abrirDialog();
            }
        });


    }

    private void init() {
       this.progressDialog = new ProgressDialog(LoginActivity.this);
    }

    private void showDialog(){
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.abri_dialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.abri_dialog);

    }

    private void abriTelasPricipais( String emailUsuario) {

        referenceFirebase.child("Usuario").orderByChild("email").equalTo(emailUsuario.toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String tipoUsuario = postSnapshot.child("tipoUsuario").getValue().toString();
                            if (tipoUsuario.equals("Admin")){
                                Intent i = new Intent(LoginActivity.this, TelaAdmActivity.class);
                                startActivity(i);
                                finish();
                            }else if (tipoUsuario.equals("Caixa")){
                                Intent i = new Intent(LoginActivity.this, OpCActivity.class);
                                startActivity(i);
                                finish();
                            }else if (tipoUsuario.equals("Entregado")){
                                Intent i = new Intent(LoginActivity.this, EntregadorActivity.class);
                                startActivity(i);
                                finish();
                            }else if (tipoUsuario.equals("comum")){
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void abrirDialog() {

        dialog = new Dialog(LoginActivity.this);

        dialog.setContentView(R.layout.alert_recovery_password);

        btnCancelResgSen = (BootstrapButton) dialog.findViewById(R.id.btnCancelResgSen);
        btnSendEmail = (BootstrapButton) dialog.findViewById(R.id.btnSendEmail);
        editEmailRecovery = (BootstrapEditText) dialog.findViewById(R.id.editEmailRecovery);


        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editEmailRecovery.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Preencha E-mail!", Toast.LENGTH_SHORT).show();
                    editEmailRecovery.requestFocus();
                }else
                autenticacao.sendPasswordResetEmail(editEmailRecovery.getText().toString())
                        .addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                    Toast.makeText(LoginActivity.this, "Verifique sua caixa de e-mail!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                dialog.dismiss();
            }
        });

        btnCancelResgSen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    @Override
    protected void onStart() {
        super.onStart();

//        verificarPermissoes();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = autenticacao.getCurrentUser();

        if (currentUser != null) {
            abriTelasPricipais(editEmailLog.toString());

        }

    }


    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            return true;
        }else {
            return false;

        }
    }
    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void inicializaComponentes() {

        btnCadastraLog = (TextView)findViewById(R.id.btnCadastraLog);
        editEmailLog = (EditText)findViewById(R.id.editEmailLog);
        editrPassowodLog = (EditText)findViewById(R.id.editrPassowodLog);
        btnCancelaLog = (Button)findViewById(R.id.btnCancelaLog);
        btnLoginLog = (Button)findViewById(R.id.btnLoginLog);
        EqueciMinhaSe = (TextView) findViewById(R.id.EqueciMinhaSe);
    }

    private void EfetuarLogin(final String email, String password) {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            abriTelasPricipais(email);


                            Toast.makeText(LoginActivity.this, "Login Efetuado com Sucesso!",
                                    Toast.LENGTH_LONG).show();




                        } else {

                            String excecao = "";
                            try {
                                throw task.getException();
                            }catch(FirebaseAuthInvalidUserException e ) {
                                excecao = "Usuário não cadastrada";
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = " Email e senha não correspondem ao usuário!";
                            }catch (Exception e){
                                excecao = "Erro ao cadastrar usuário!"  + e.getMessage();
                                e.printStackTrace();
                            }
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
