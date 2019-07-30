package com.jose.freedelivery2.Dados;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;
import com.jose.freedelivery2.Model.Address.Address;
import com.jose.freedelivery2.Model.ItemPedido;
import com.jose.freedelivery2.Model.Pedido;
import com.jose.freedelivery2.Model.User;
import com.jose.freedelivery2.R;
import com.jose.freedelivery2.Adapter.AdapteterEndereco;
import com.jose.freedelivery2.Helper.ConfiguracaoFirebase;
import com.jose.freedelivery2.Listene.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ExibirAddressActivity extends AppCompatActivity {

    private TextView textNomeRua;
    private RecyclerView recyclerendereco;
    private ImageView imageEndereco;
    private User userSelecionado;

    private AlertDialog dialog;
    private String idUsuarioLogado;
    private User usuario;
    private Pedido pedidoRecuperado;
    private int qtdItensCarrinho;
    private Double totalCarrinho;
    private int metodoPagamento;
    private TextView textCarrinhoQtd, textCarrinhoTotal;

    private AdapteterEndereco adapterEndereco;
    private List<Address> endereco = new ArrayList<>();
    private List<ItemPedido> itens = new ArrayList<>();
    private DatabaseReference firebaseRef;
    private String idEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibi_user);

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarFucionario);
        toolbar.setTitle("Endereço");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configurações iniciais
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Recuperar empresa selecionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userSelecionado = (User) bundle.getSerializable("UsuarioComu");

            textNomeRua.setText(userSelecionado.getNome());
            idUsuarioLogado = userSelecionado.getIdUsuario();

            String url = userSelecionado.getUrlImagem();
            Picasso.get().load(url).into(imageEndereco);
        }


        //Configura recyclerview
        recyclerendereco.setLayoutManager(new LinearLayoutManager(this));
        recyclerendereco.setHasFixedSize(true);
        adapterEndereco = new AdapteterEndereco(endereco, this);
        recyclerendereco.setAdapter( adapterEndereco );

        recyclerendereco.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerendereco,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                confirmarQuantidade(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

//        Recupera produtos para empresa
        recuperarEndereco();
        recuperarDadosUsuario();

    }

    private void confirmarQuantidade(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setMessage("Digite o numero da loja");

        final EditText editQuantidade = new EditText(this);
        editQuantidade.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView( editQuantidade );

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantidade = editQuantidade.getText().toString();

                Address enderecoSelecionado = endereco.get(position);

                ItemPedido itemPedido = new ItemPedido();

                itemPedido.setIdProduto( enderecoSelecionado.getIdEndereco() );
                itemPedido.setNomeProduto(itemPedido.getNomeProduto());
                itemPedido.setCep( enderecoSelecionado.getCep() );
                itemPedido.setBairro( enderecoSelecionado.getBairro() );
                itemPedido.setCidade( enderecoSelecionado.getCidade() );
                itemPedido.setNumero( enderecoSelecionado.getNumero() );
                itemPedido.setComplemento( enderecoSelecionado.getComplemento() );
                itemPedido.setEstado( enderecoSelecionado.getEstado() );
                itemPedido.setRua( enderecoSelecionado.getRua() );
                itemPedido.setQuantidade( Integer.parseInt(quantidade) );

                itens.add( itemPedido );

                if( pedidoRecuperado == null ){
                    pedidoRecuperado = new Pedido(idUsuarioLogado, idEndereco);
                }

                pedidoRecuperado.setNome( usuario.getNome() );
                pedidoRecuperado.setTelefone( usuario.getTelefone() );
                pedidoRecuperado.setItens( itens );
                pedidoRecuperado.salvar();

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        // FORÇA O TECLADO APARECER AO ABRIR O ALERT
        dialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void recuperarDadosUsuario() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();
        DatabaseReference usuarioRef = firebaseRef
                .child("UsuarioComu")
                .child(idUsuarioLogado);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(User.class);

                }
                recuperarPedido();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarPedido() {
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_usuario")
                .child( idUsuarioLogado );
        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itens = new ArrayList<>();

                if(dataSnapshot.getValue() != null){

                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itens = pedidoRecuperado.getItens();


                    for(ItemPedido itemPedido: itens){
                        int qtde = itemPedido.getQuantidade();

                    }
                }

                DecimalFormat df = new DecimalFormat("");

                textCarrinhoQtd.setText( "N Caixa: " + String.valueOf(qtdItensCarrinho) );
                textCarrinhoTotal.setText("" + df.format( totalCarrinho ) );
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void recuperarEndereco(){

        DatabaseReference enderecoRef = firebaseRef
                .child("Address")
                .child(idUsuarioLogado);

        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                endereco.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    endereco.add( ds.getValue(Address.class) );
                }

                adapterEndereco.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comfirma_caixa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_pedido:
                confirmarPedido();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void confirmarPedido() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione uma opção");

        CharSequence[] itens = new CharSequence[]{
                "Confirma envio!"
        };
        builder.setSingleChoiceItems(itens, 0,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                metodoPagamento = which;
            }
        });

        final EditText editObservacao = new EditText(this);
        builder.setTitle("Digite os números das caixas ");
        editObservacao.setInputType(InputType.TYPE_CLASS_TEXT);
        editObservacao.setHint("Digite os números das caixas ");
        builder.setView( editObservacao );

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String observacao = editObservacao.getText().toString();
                pedidoRecuperado.setObservacao( observacao );
                pedidoRecuperado.setStatus("confirmado");
                pedidoRecuperado.confimar();
                pedidoRecuperado.remover();
                pedidoRecuperado = null;

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        // FORÇA O TECLADO APARECER AO ABRIR O ALERT
        dialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void inicializarComponentes() {
        recyclerendereco = findViewById(R.id.recyclerendereco);
        imageEndereco = findViewById(R.id.imageEndereco);
        textNomeRua = findViewById(R.id.textNomeRuad);
        textCarrinhoQtd = findViewById(R.id.textCarrinhoQtd);
        textCarrinhoTotal = findViewById(R.id.textCarrinhoTotal);
    }

}
