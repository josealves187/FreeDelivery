package com.jose.freedelivery2.Model;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;



public class Usuarios {

    private String name;
    private String email;
    private String keyFucionario;
    private String tipoUsuario;
    private String password;
    private String uid;
    private String cpf;


    public Map<String, Boolean> funcionario = new HashMap<>();

    public Usuarios() {
    }



    public Usuarios(String name, String email, String keyFucionario, String tipoUsuario,
                    String password, String uid, String cpf) {
        this.name = name;
        this.email = email;
        this.keyFucionario = keyFucionario;
        this.tipoUsuario = tipoUsuario;
        this.password = password;
        this.uid = uid;
        this.cpf = cpf;

    }


    public Map<String, Boolean> getFuncionario() {
        return funcionario;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getKeyFucionario() {
        return keyFucionario;
    }

    public void setKeyFucionario(String keyFucionario) {
        this.keyFucionario = keyFucionario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("tipoUsuario", tipoUsuario);
        result.put("keyUser", keyFucionario);
        result.put("uid", uid);
        result.put("cpf", cpf);
        result.put("funcionario", funcionario);

        return result;
    }
}
