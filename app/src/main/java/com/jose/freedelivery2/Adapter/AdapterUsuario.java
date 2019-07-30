package com.jose.freedelivery2.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jose.freedelivery2.Model.User;
import com.jose.freedelivery2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.MyViewHolder> {

    private List<User> users;


    public AdapterUsuario(List<User> user) {
        this.users = user;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_user, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        User user = users.get(i);
        holder.nome.setText(user.getNome2());
        holder.Cpf.setText(user.getCpf());
        holder.telefone.setText(user.getTelefone());


        //Carregar imagem
        String urlImagem = user.getUrlImagem();
        Picasso.get().load(urlImagem).into(holder.imagemuser);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemuser;
        TextView nome;
        TextView Cpf;
        TextView telefone;


        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            Cpf = itemView.findViewById(R.id.textcpf);
            telefone = itemView.findViewById(R.id.texttelefone);
            imagemuser = itemView.findViewById(R.id.imageUser);

        }
    }
}
