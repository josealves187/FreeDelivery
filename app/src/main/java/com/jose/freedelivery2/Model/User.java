package com.jose.freedelivery2.Model;

import com.google.firebase.database.DatabaseReference;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;

import java.io.Serializable;

public class User implements Serializable {
    private String telefone;
    private String telefone2;
    private String nome2;
    private String nome;
    private String UrlImagem;
    private String idUsuario;
    private String cpf;





    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("UsuarioComu")
                .child( getIdUsuario() );
        usuarioRef.setValue(this);

    }




    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getNome2() {
        return nome2;
    }

    public void setNome2(String nome2) {
        this.nome2 = nome2;
    }

    public String getUrlImagem() {
        return UrlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        UrlImagem = urlImagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }


    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}


