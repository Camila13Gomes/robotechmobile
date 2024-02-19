package com.example.robottech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robottech.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserFragment extends Fragment {
    // Declaração de variáveis
    private TextView nomeUsuario, emailUsuario;
    private Button btnDeslogar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    // Método chamado quando o fragmento é criado
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    // Método chamado após a criação da view do fragmento
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializa os componentes da interface do usuário
        IniciarComponentes(view);

        // Configura a ação ao clicar no botão de deslogar
        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Realiza o logout do usuário
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent); // Inicia a MainActivity após o logout
            }
        });
    }

    // Método chamado ao iniciar o fragmento
    @Override
    public void onStart() {
        super.onStart();

        // Obtém o e-mail e o ID do usuário atualmente logado
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Referência ao documento do usuário na coleção "Usuarios"
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);

        // Listener para atualizar dados do usuário quando houver mudanças no documento
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    // Define o nome do usuário e exibe o e-mail
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    // Método para inicializar os componentes da interface do usuário
    private void IniciarComponentes(View view) {
        nomeUsuario = view.findViewById(R.id.textusuario);
        emailUsuario = view.findViewById(R.id.texEmailusr);
        btnDeslogar = view.findViewById(R.id.btnDeslogar);
    }
}