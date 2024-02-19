package com.example.robottech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// Adaptador para o RecyclerView
public class ViewHolder extends RecyclerView.Adapter<ViewHolder.viewHolder> {

    Context context;
    ArrayList<Model> list;

    // Construtor para o adaptador, recebendo o contexto e a lista de modelos
    public ViewHolder(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    // Método chamado quando o ViewHolder é criado
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout de cada item da lista
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new viewHolder(view);
    }

    // Método chamado para associar os dados a cada ViewHolder
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // Obtém o modelo na posição atual da lista
        Model model = list.get(position);

        // Carrega a imagem usando Glide na ImageView
        Glide.with(context)
                .load(model.getImage()) // Supondo que getImage() retorna a URL da imagem como uma String
                .into(holder.Image);

        // Define o título e o preço nos TextViews correspondentes
        holder.Titulo.setText(model.getNome());
        holder.Preco.setText(model.getPreco());
    }

    // Retorna o tamanho da lista de itens
    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder que mantém a referência aos elementos da interface de cada item da lista
    public static class viewHolder extends RecyclerView.ViewHolder {

        ImageView Image;
        TextView Titulo, Preco;

        // Construtor do viewHolder
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa os elementos de interface do item
            Image = itemView.findViewById(R.id.image);
            Titulo = itemView.findViewById(R.id.rTitulo);
            Preco = itemView.findViewById(R.id.rPreco);
        }
    }
}
