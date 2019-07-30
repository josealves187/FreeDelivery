package com.jose.freedelivery2.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class Pedido implements Serializable {

    private String idUsuario;
    private String idEndereco;
    private String idPedido;
    private String nome;
    private String telefone;
    private List<ItemPedido> itens;
    private Double total;
    private String status = "pendente";
    private int metodoPagamento;
    private String observacao;

    public Pedido() {
    }

    public Pedido(String idUsu, String idEnde) {

        setIdUsuario(idUsu);
        setIdEndereco(idEnde);

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(idUsu);
        setIdPedido(pedidoRef.push().getKey());

    }

    public void salvar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(getIdUsuario());
        pedidoRef.setValue(this);

    }

    public void remover() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child(getIdUsuario());
        pedidoRef.removeValue();

    }

    public  void setRemovePedidos(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference removePedidos = firebaseRef
                .child("pedidos")
                .child(getIdPedido());
        Query query = removePedidos.orderByChild("itens")
                .equalTo((String) removePedidos.getDatabase().getReference().getKey());
        removePedidos.removeValue();

    }


    public void confimar() {

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdPedido());
        pedidoRef.setValue(this);

    }

    public void atualizarStatus() {

        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(getIdPedido());
        pedidoRef.updateChildren(status);


    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(String idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(int metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
