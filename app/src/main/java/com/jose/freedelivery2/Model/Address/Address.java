package com.jose.freedelivery2.Model.Address;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Address   {


    public static final int REQUEST_ZIP_CODE_CODE = 566;
    public static final String ZIP_CODE_KEY = "zip_code_key";

    private String Cep;
    private String Rua;
    private String Complemento;
    private String Cidade;
    private String Numero;
    private String Bairro;
    private String Estado;
    private String idUsuario;
    private String idEndereco;



    public Map<String, Boolean> address = new HashMap<>();

    public Address(String cep, String rua, String complemento, String cidade, String numero, String bairro, String estado,  String idEndereco) {
        this. Cep = cep;
        this. Rua = rua;
        this. Complemento = complemento;
        this. Cidade = cidade;
        this. Numero = numero;
        this. Bairro = bairro;
        this. Estado = estado;
        this.idEndereco = idEndereco;

    }

    public Address() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("Address");

        setIdEndereco(usuarioRef.push().getKey());

    }

    public void salvar( ){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("Address")
                .child( getIdUsuario() )
                .child(getIdEndereco());

        usuarioRef.setValue(this);


    }



    public String getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(String idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getCep() {
        return Cep;
    }

    public void setCep(String cep) {
        Cep = cep;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String rua) {
        Rua = rua;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        Complemento = complemento;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }



    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cep", Cep);
        result.put("rua",Rua );
        result.put("complemento", Complemento);
        result.put("cidade", Cidade);
        result.put("numero", Numero);
        result.put("bairro", Bairro);
        result.put("estado", Estado);
        result.put("idEndereco",idEndereco);
        result.put("Address", address);

        return result;
    }
}
