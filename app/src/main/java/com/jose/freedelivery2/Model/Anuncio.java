package com.jose.freedelivery2.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;

import java.util.List;

import static com.jose.freedelivery2.Helper.UsuarioFirebase.getIdUsuario;

public class Anuncio {

    private String IdUsuario;
    private String IdProduto;
    private String descricao;
    private String nome;
    private String urlImage;
    private String preco;
    private String date;
    private List<String> fotos;


    public Anuncio() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference oferta = firebaseRef
                .child("anuncios");
        setIdProduto(oferta.push().getKey());

    }




    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference ofertaRef = firebaseRef
                .child("anuncios")
               .child(getIdProduto());
        ofertaRef.setValue(this);

    }

    public void remover(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("anuncios")

                .child( getIdProduto() );
        produtoRef.removeValue();
    }

    public String getIdProduto() {
        return IdProduto;
    }
    public void setIdProduto(String idProduto) {
        IdProduto = idProduto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}
