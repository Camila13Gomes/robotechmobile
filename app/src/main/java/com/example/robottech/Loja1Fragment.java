package com.example.robottech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Loja1Fragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    ViewHolder viewHolder;
    ArrayList<Model> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_loja1, container, false);

        // Referência ao RecyclerView no layout XML
        recyclerView = view.findViewById(R.id.recyclerView);

        // Referência ao nó "Data" no banco de dados Firebase
        database = FirebaseDatabase.getInstance().getReference("Data");

        // Configuração do RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicialização da lista e do adaptador do RecyclerView
        list = new ArrayList<>();
        viewHolder = new ViewHolder(requireContext(), list);
        recyclerView.setAdapter(viewHolder);

        // Listener para detectar mudanças nos dados do banco de dados Firebase
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Limpa a lista antes de adicionar novos dados

                // Itera por todos os dados no nó "Data" e adiciona à lista
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    list.add(model);
                }

                viewHolder.notifyDataSetChanged(); // Notifica o adaptador sobre a mudança nos dados
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Trate o erro de leitura do Firebase aqui
            }
        });

        return view; // Retorna a visualização do fragmento
    }
}
