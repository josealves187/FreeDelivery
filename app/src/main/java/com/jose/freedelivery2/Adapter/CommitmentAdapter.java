package com.jose.freedelivery2.Adapter;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jose.freedelivery2.Model.Commitment;
import com.jose.freedelivery2.R;


import java.util.List;


public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {

    private List<Commitment> mCommitmentList;
    private Context context;
    private Dialog dialog;
    private DatabaseReference mDatabase;


    public CommitmentAdapter(List<Commitment> mCommitmentList, Context c) {
        this.context = c;
        this.mCommitmentList = mCommitmentList;
    }


    @Override
    public CommitmentAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate
                (R.layout.al_commitment, viewGroup, false);
        return new CommitmentAdapter.ViewHolder(itemView);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final CommitmentAdapter.ViewHolder holder, int position) {


//        final Commitment item = mCommitmentList.get(position);


        final Commitment commitment = mCommitmentList.get(position);
        holder.txtName.setText(commitment.getName());
        holder.txtDate.setText(commitment.getDate());
        holder.txtDescricaçao.setText(commitment.getDescription());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialog(commitment);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCommitmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtName;
        protected TextView txtDate;
        protected TextView txtDescricaçao;
        protected LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtDescricaçao = (TextView) itemView.findViewById(R.id.edtDescription);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }


    private void abrirDialog(final Commitment commitment) {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.alert_update_status_commitment);

        BootstrapButton btnDelete = (BootstrapButton) dialog.findViewById(R.id.btnDelete);
        BootstrapButton btnUpdateStatus = (BootstrapButton) dialog.findViewById(R.id.btnUpdateStatus);
        TextView txtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
        TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
        TextView txtDate = (TextView) dialog.findViewById(R.id.txtDate);
        TextView txtDescricaçao = (TextView) dialog.findViewById(R.id.edtDescription);

        if (commitment.getStatus().equals("Passado")) {
            btnUpdateStatus.setText("Mudar para Pendente");
        } else if (commitment.getStatus().equals("Pendente")) {
            btnUpdateStatus.setText("Mudar para Passado");
        }

        txtName.setText(commitment.getName().toString());
        txtDescription.setText(commitment.getDescription().toString());
        txtDate.setText(commitment.getDate().toString());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("commitment").child(commitment.getKeyCommitment()).removeValue();
                dialog.dismiss();
            }
        });

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commitment.getStatus().equals("Passado")) {
                    mDatabase.child("commitment").child(commitment.getKeyCommitment())
                            .child("status").setValue("Pendente");
                } else if (commitment.getStatus().equals("Pendente")) {
                    mDatabase.child("commitment").child(commitment.getKeyCommitment())
                            .child("status").setValue("Passado");
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
