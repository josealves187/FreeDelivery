package com.jose.freedelivery2.Registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.jose.freedelivery2.Model.Address.Address;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.UsuarioComu.MainActivity;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.MaskEditText;
import com.jose.freedelivery2.Helper.UsuarioFirebase;

public class UserAddress extends AppCompatActivity {

    private EditText edtCep;
    private EditText edtRua;
    private EditText edtComplemento;
    private EditText edtCidade;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtEstado;

    private Button salva;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;
    private Address pedidoRecuperado;


    private StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_usuario);

        //Configurações iniciais
        iniciaComponetes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Configuração");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDadosUsuario(view);

                Intent i = new Intent(UserAddress.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
        recueperarDadosUsuario();


    }

    //recueperarDadosUsuario
    private void recueperarDadosUsuario(){

        final DatabaseReference addressref = firebaseRef
                .child("Address")
                .child(idUsuarioLogado);
        addressref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){


                   Address e = dataSnapshot.getValue(Address.class);


                    edtCep.setText(e.getCep());
                    edtRua.setText(e.getRua());
                    edtComplemento.setText(e.getComplemento());
                    edtCidade.setText(e.getCidade());
                    edtBairro.setText(e.getBairro());
                    edtNumero.setText(e.getNumero());
                    edtEstado.setText(e.getEstado());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void validarDadosUsuario(View view) {

        //Valida se os campos foram preenchidos
        String cep = edtCep.getText().toString();
        String rua = edtRua.getText().toString();
        String complemento = edtComplemento.getText().toString();
        String cidade = edtCidade.getText().toString();
        String bairro = edtBairro.getText().toString();
        String numero = edtNumero.getText().toString();
        String estado = edtEstado.getText().toString();

        if (!cep.isEmpty()) {
            if (!rua.isEmpty()) {
                if (!complemento.isEmpty()) {
                    if (!cidade.isEmpty()) {
                        if (!bairro.isEmpty()) {
                            if (!numero.isEmpty()) {
                                if (!estado.isEmpty()) {

                                    Address End = new Address( );
                                    End.setIdUsuario(idUsuarioLogado);
                                    End.setCep(cep);
                                    End.setRua(rua);
                                    End.setComplemento(complemento);
                                    End.setCidade(cidade);
                                    End.setNumero(numero);
                                    End.setBairro(bairro);
                                    End.setEstado(estado);
                                    End.salvar();
                                    finish();
                                    exibirMensagem("Endereço salvo com sucesso");

                                } else {
                                    exibirMensagem("Digite um cep ");
                                }
                            } else {
                                exibirMensagem("Digite a rua ");
                            }
                        } else {
                            exibirMensagem("Digite o complemento");
                        }
                    } else {
                        exibirMensagem("Digite a cidade ");
                    }
                }else{
                    exibirMensagem("Digite o numero ");
                }

                } else {
                exibirMensagem("Digite o bairro ");
            }

            } else {
            exibirMensagem("Digite o estado ");
        }


    }



    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }






    private void iniciaComponetes() {

        edtCep = (EditText) findViewById(R.id.edtCep);
        edtBairro = (EditText) findViewById(R.id.edtBairro);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtComplemento = (EditText) findViewById(R.id.edtComplemento);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtRua = (EditText) findViewById(R.id.edtRua);
        edtEstado = (EditText) findViewById(R.id.edtEstado);
        salva = (Button)findViewById(R.id.salva);
        edtCep.addTextChangedListener(MaskEditText.mask(edtCep, MaskEditText.FORMAT_CEP));

    }

}
