package com.jose.freedelivery2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jose.freedelivery2.Model.ItemPedido;
import com.jose.freedelivery2.Model.Pedido;
import com.jose.freedelivery2.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPendente extends RecyclerView.Adapter<AdapterPendente.MyViewHolder> {

    private List<Pedido> pedidos;
    private Context context;

    public AdapterPendente(List<Pedido> pedidos, Context context) {
        this.pedidos = pedidos;
        this.context = context;

    }


    @NonNull
    @Override
    public AdapterPendente.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterpendente, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Pedido pedido = pedidos.get(i);
        holder.telefone.setText(pedido.getTelefone());
        holder.nome.setText( pedido.getNome() );
        holder.observacao.setText( "Nº das Caixas: "+ pedido.getObservacao() );

        List<ItemPedido> itens = new ArrayList<>();
        itens = pedido.getItens();
        String descricaoItens = "";

        int numeroItem = 1;
        Double total = 0.0;
        for( ItemPedido itemPedido : itens ){

            int qtde = itemPedido.getQuantidade();
            total += (qtde);



            String cep = itemPedido.getCep();
            String rua = itemPedido.getRua();
            String Bairro = itemPedido.getBairro();
            String Complemento = itemPedido.getComplemento();
            String Numero = itemPedido.getNumero();
            String Cidade = itemPedido.getCidade();
            String Estado = itemPedido.getEstado();
            descricaoItens  += numeroItem + ") Numero da loja " + qtde + " -" + rua + " , " + Numero +"  " + Bairro +" , " +
                    "" + Complemento +" - " + Cidade +" - " + Estado +", " + cep + "  \n"; numeroItem++;




        }
        descricaoItens += ": " + total ;
        holder.itens.setText(descricaoItens);

        int metodoPagamento = pedido.getMetodoPagamento();
        String pagamento = metodoPagamento == 0 ? "Confirma envio" : "não" ;


    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView observacao;
        TextView itens;
        TextView telefone;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome        = itemView.findViewById(R.id.textPedidoNome1);
            observacao  = itemView.findViewById(R.id.textPedidoObs1);
            itens       = itemView.findViewById(R.id.textPedidoItens1);
            telefone       = itemView.findViewById(R.id.texttelefonee1);

        }
    }
}
