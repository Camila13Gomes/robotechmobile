package com.example.robottech.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robottech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {



    private EditText edit_nome, edit_email, edit_senha;
    private Button bt_cadastro;
    String[] mensagens = {"Por favor, preencha todos os campos", "Cadastro realizado com sucesso", "Digite uma senha com no mínimo 6 caracteres", "Usuário já cadastrado",
            "Email inválido, digite um email correto", "Erro ao cadastrar", "Sucesso ao salvar os dados", "Erro ao salvar os dados"};
    String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializa os componentes da UI
        IniciarComponentes();

        // Configura o listener do botão de cadastro
        bt_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os dados dos campos de entrada
                String nome = edit_nome.getText().toString();
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                // Verifica se algum campo está vazio
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    // Exibe uma mensagem de erro caso haja campos vazios
                    exibirSnackbar(v, mensagens[0]);
                } else {
                    // Realiza o cadastro do usuário
                    CadastrarUsuario(v);
                }
            }
        });
    }

    // Método para cadastrar um novo usuário
    private void CadastrarUsuario(View v) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        // Cria um usuário com email e senha no Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Se o cadastro for bem-sucedido, salva os dados do usuário
                            SalvarDadosUsuario();
                            exibirSnackbar(v, mensagens[1]); // Exibe mensagem de sucesso
                        } else {
                            // Se houver algum erro, identifica e exibe a mensagem correspondente
                            String erro;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = mensagens[2];
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = mensagens[3];
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = mensagens[4];
                            } catch (Exception e) {
                                erro = mensagens[5];
                            }
                            exibirSnackbar(v, erro); // Exibe a mensagem de erro
                        }
                    }
                });
    }

    // Método para salvar os dados do usuário no Firestore
    private void SalvarDadosUsuario() {
        String nome = edit_nome.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Cria um mapa para os dados do usuário
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);

        // Obtém o ID do usuário atualmente logado
        UsuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Referência ao documento do usuário no Firestore e salva os dados
        DocumentReference documentReference = db.collection("Usuarios").document(UsuarioID);
        documentReference.set(usuarios)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", mensagens[6]); // Log de sucesso
                        abrirTelaOpcao(); // Abre a tela de opções após salvar os dados
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_error", mensagens[7] + e.toString()); // Log de erro
                    }
                });
    }

    // Inicializa os componentes da UI
    private void IniciarComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_mail);
        edit_senha = findViewById(R.id.edit_senha);
        bt_cadastro = findViewById(R.id.bt_cadastro);

        ImageButton imageButtonVoltar = findViewById(R.id.imageButtonvoltar);

        // Configura o listener do botão de voltar
        imageButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarParaMainActivity(); // Volta para a MainActivity
            }
        });
    }

    // Método para exibir uma Snackbar
    private void exibirSnackbar(View view, String mensagem) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }

    // Método para voltar para a MainActivity
    public void voltarParaMainActivity() {
        Intent intent = new Intent(Cadastro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Método para abrir a tela de opções
    private void abrirTelaOpcao() {
        Intent intent = new Intent(Cadastro.this, Tela_opcao.class);
        startActivity(intent);
        finish(); // Finaliza a activity atual
    }
}
