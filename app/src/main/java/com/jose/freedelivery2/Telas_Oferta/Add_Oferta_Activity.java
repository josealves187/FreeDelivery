package com.jose.freedelivery2.Telas_Oferta;

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
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Helper.Permissoes;
import com.jose.freedelivery2.Helper.UsuarioFirebase;
import com.jose.freedelivery2.Model.Anuncio;
import com.jose.freedelivery2.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class Add_Oferta_Activity extends AppCompatActivity{


    private BootstrapEditText edtName1;
    private BootstrapEditText descricaoProduto;
    private BootstrapEditText precoProduto;
    private BootstrapEditText DataOferta;

    private BootstrapButton btnRegisterd;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private ImageView imagePerfil,imagePerfil2,imagePerfil3;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;

    private String urlImagemSelecionada = "";
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__oferta_);

        Permissoes.validarPermissoes(permissoes, this, 1);

        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        iniciaComponetes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();





        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        btnRegisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDadosUsuarios(v);

            }
        });
        recueperarDadosUsuario();

    }

    private void recueperarDadosUsuario() {
        DatabaseReference user = firebaseRef
                .child("anuncio")

                ;
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Anuncio e = dataSnapshot.getValue(Anuncio.class);


                    edtName1.setText(e.getNome());
                    descricaoProduto.setText(e.getDescricao());
                    precoProduto.setText(e.getPreco());
                    DataOferta.setText(e.getDate());


                    urlImagemSelecionada = e.getUrlImage();
                    if (urlImagemSelecionada != "") {
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
        String nome = edtName1.getText().toString();
        edtName1.setError("Digite o nome do produto");
        String descricao = descricaoProduto.getText().toString();
        descricaoProduto.setError("Descriçao produto");
        String preco = precoProduto.getText().toString();
        precoProduto.setError("Digite Preço");
        String data = DataOferta.getText().toString();
        DataOferta.setError("Digite a data da Oferta");
        ImageView imagePerfil = null;


        if (!nome.isEmpty()) {
            if (!descricao.isEmpty()) {
                if (!preco.isEmpty()) {
                    if (!data.isEmpty()) {


                        Anuncio anuncio = new Anuncio();

                        anuncio.setNome(nome);

                        anuncio.setDescricao(descricao);
                        anuncio.setPreco(preco);
                        anuncio.setDate(data);
                        anuncio.setUrlImage(urlImagemSelecionada);
                        anuncio.salvar();
                        finish();


                    } else {

                        exibirMensagem("Digite o nome do produto ");
                    }
                } else {
                    exibirMensagem("Descriçao produto ");
                }
            } else {
                exibirMensagem("Digite Preço");
            }
        } else {
            exibirMensagem("Digite a data da Oferta ");
        }

        finish();
    }

    private void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
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

                if (imagem != null) {

                    imagePerfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("anuncio")
                            .child("jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlImagemSelecionada = downloadUrl.toString();


                                Toast.makeText(Add_Oferta_Activity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Add_Oferta_Activity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    private void iniciaComponetes() {
        btnRegisterd = (BootstrapButton) findViewById(R.id.btnRegisterOfe);
        edtName1 = (BootstrapEditText) findViewById(R.id.edtNameProduto);
        descricaoProduto = (BootstrapEditText) findViewById(R.id.descricaoProduto);
        precoProduto = (BootstrapEditText) findViewById(R.id.precoProduto);
        DataOferta = (BootstrapEditText) findViewById(R.id.DataOferta);
        imagePerfil = (ImageView) findViewById(R.id.fotoProduto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionsResultado : grantResults) {
            if (permissionsResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }

        }
    }

    private void alertaValidacaoPermissao() {

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
