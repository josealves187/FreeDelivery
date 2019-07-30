package com.jose.freedelivery2.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.jose.freedelivery2.Model.Anuncio;
import com.jose.freedelivery2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterOfertas extends RecyclerView.Adapter<AdapterOfertas.ViewHolder> {

    private List<Anuncio> moferta;
    private Context context;



    public AdapterOfertas(List<Anuncio> moferta, Context context ){
        this.context = context;
        this.moferta = moferta;

    }




    @Override
    public AdapterOfertas.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_ofertas_semana, parent, false);
        return new AdapterOfertas.ViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final AdapterOfertas.ViewHolder viewHolder, int posiotion) {

        Anuncio items = moferta.get(posiotion);
        viewHolder.nome.setText(items.getNome());
        viewHolder.descricao.setText(items.getDescricao());
        viewHolder.preco.setText(items.getPreco());

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();




        final  int height = (displayMetrics.heightPixels /4);
        final int width = (displayMetrics.widthPixels / 2);

        String urlImagem = items.getUrlImage();
        Picasso.get().load(urlImagem).resize(width,height)
                .centerCrop().into(viewHolder.imagemuser);


    }

    @Override
    public int getItemCount() {
       return moferta.size();
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView imagemuser;
        protected TextView nome;
        protected TextView descricao;
        protected TextView preco;
        protected LinearLayout linearLayoutProdutoOfetas;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            nome = itemView.findViewById(R.id.nomeProduto);
            descricao = itemView.findViewById(R.id.descricao);
            preco = itemView.findViewById(R.id.preco);
            imagemuser = itemView.findViewById(R.id.imageView1);
            linearLayoutProdutoOfetas = itemView.findViewById(R.id.OfertaSemana);

        }
    }
}
