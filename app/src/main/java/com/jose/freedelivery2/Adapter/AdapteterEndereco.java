package com.jose.freedelivery2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jose.freedelivery2.Model.Address.Address;
import com.jose.freedelivery2.R;

import java.util.List;




public class AdapteterEndereco extends RecyclerView.Adapter<AdapteterEndereco.MyViewHolder> {

    private List<Address> addresses;
    private Context context;


    public AdapteterEndereco(List<Address> addresses, Context context) {
        this.addresses = addresses;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_empresa, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {




        Address address = addresses.get(i);
        holder.cep.setText(address.getCep());
        holder.rua.setText(address.getRua());
        holder.complemento.setText(address.getComplemento());
        holder.cidade.setText(address.getCidade());
        holder.estado.setText(address.getEstado());
        holder.bairro.setText(address.getBairro());
        holder.numero.setText(address.getNumero());


    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView estado;
        TextView bairro;
        TextView cep;
        TextView rua;
        TextView complemento;
        TextView cidade;
        TextView numero;



        public MyViewHolder(View itemView) {
            super(itemView);

            rua = itemView.findViewById(R.id.textNomeRua);
            bairro = itemView.findViewById(R.id.textbairro);
            complemento = itemView.findViewById(R.id.textcomplemento);
            cidade = itemView.findViewById(R.id.textcidade);
            cep = itemView.findViewById(R.id.textcep);
            estado = itemView.findViewById(R.id.textestado);
            numero = itemView.findViewById(R.id.textnumero);

        }
    }
}
