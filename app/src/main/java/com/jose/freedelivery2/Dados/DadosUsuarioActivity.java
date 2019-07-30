package com.jose.freedelivery2.Dados;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jose.freedelivery2.Model.User;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.MaskEditText;
import com.jose.freedelivery2.Helper.Permissoes;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Registration.UserAddress;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class DadosUsuarioActivity extends AppCompatActivity {

    private BootstrapEditText edtName;
    private BootstrapEditText edtName2;
    private BootstrapEditText edtCpf;
    private BootstrapEditText edttelefone;
    private BootstrapEditText edttelefone2;

    private BootstrapButton btnRegisterd;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private ImageView imagePerfil;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Permissoes.validarPermissoes(permissoes,this, 1);
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        iniciaComponetes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();




        btnRegisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDadosUsuarios(v);
                startActivity(new Intent(DadosUsuarioActivity.this, UserAddress.class));
                finish();
            }
        });

        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        recueperarDadosUsuario();

    }

    private void recueperarDadosUsuario() {
        DatabaseReference user = firebaseRef
                .child("UsuarioComu")
                .child(idUsuarioLogado);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    User  e = dataSnapshot.getValue(User.class);
                    edtName2.setText(e.getNome2());
                    edtCpf.setText(e.getCpf());
                    edttelefone.setText(e.getTelefone());
                    edttelefone2.setText(e.getTelefone2());
                    edtName.setText(e.getNome());

                    urlImagemSelecionada = e.getUrlImagem();
                    if (urlImagemSelecionada != ""){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imagePerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void validarDadosUsuarios(View v) {


        //Valida se os campos foram preenchidos
        String nome = edtName.getText().toString();
        String nome2 = edtName2.getText().toString();
        String cpf = edtCpf.getText().toString();
        String telefone = edttelefone.getText().toString();
        String telefone2 = edttelefone2.getText().toString();
        ImageView imagePerfil = null;


        if (!nome.isEmpty()) {
            if (!nome2.isEmpty()) {
                if (!cpf.isEmpty()) {
                    if (!telefone.isEmpty()) {
                        if (!telefone2.isEmpty()) {


                            User user = new User();

                            user.setNome(nome);
                            user.setIdUsuario(idUsuarioLogado);
                            user.setNome2(nome2);
                            user.setCpf(cpf);
                            user.setTelefone(telefone);
                            user.setTelefone2(telefone2);
                            user.setUrlImagem(urlImagemSelecionada);
                            user.salvar();
                            finish();


                        } else {
                            exibirMensagem("Digite o nome ");
                        }
                    } else {
                        exibirMensagem("Campo vazio como deseja ser chamador ");
                    }
                } else {
                    exibirMensagem("Digite o cpf");
                }
            } else {
                exibirMensagem("Digite o telefone ");
            }
        }else  {
            exibirMensagem("Digite o telefone fixo ");
        }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }

                if( imagem != null){

                    imagePerfil.setImageBitmap( imagem );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("Usuario")
                            .child(idUsuarioLogado + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }}).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlImagemSelecionada = downloadUrl.toString();




                                Toast.makeText(DadosUsuarioActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DadosUsuarioActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }



    private void iniciaComponetes() {
        btnRegisterd   = (BootstrapButton)  findViewById(R.id.btnRegisterd);
        edtName = (BootstrapEditText) findViewById(R.id.edtName1);
        edtName2 = (BootstrapEditText) findViewById(R.id.edtName2);
        edtCpf = (BootstrapEditText) findViewById(R.id.edtCpf1);
        edttelefone = (BootstrapEditText) findViewById(R.id.edttelefone);
        edttelefone2 = (BootstrapEditText) findViewById(R.id.edttelefone1);
        imagePerfil = (ImageView)findViewById(R.id.imagePerfilUser);
        edtCpf.addTextChangedListener(MaskEditText.mask(edtCpf, MaskEditText.FORMAT_CPF));
        edttelefone.addTextChangedListener(MaskEditText.mask(edttelefone, MaskEditText.FORMAT_CELULAR));
        edttelefone2.addTextChangedListener(MaskEditText.mask(edttelefone2, MaskEditText.FORMAT_FONE));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionsResultado : grantResults){
            if (permissionsResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
