package com.example.robottech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.robottech.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Novasenha extends AppCompatActivity {

    // Declarando variáveis para os elementos de UI e o objeto FirebaseAuth
    private EditText editEmail; // Campo de entrada de e-mail
    private Button btnRedefinirSenha; // Botão para redefinir a senha
    private FirebaseAuth firebaseAuth; // Objeto FirebaseAuth para autenticação

    // Método chamado quando a atividade é criada
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novasenha);

        // Inicialização do objeto FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Vinculação dos elementos de UI aos objetos Java
        editEmail = findViewById(R.id.edit_mail_novasenha); // Referência ao campo de e-mail
        btnRedefinirSenha = findViewById(R.id.bt_novasenha); // Referência ao botão de redefinição de senha

        // Configuração do listener do botão para redefinir a senha
        btnRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redefinirSenha(); // Chama o método para redefinir a senha
            }
        });

        // Configuração do listener do botão para voltar para a tela principal
        ImageButton imageButtonVoltar = findViewById(R.id.imageButtonvoltar1);
        imageButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarParaMainActivity(); // Chama o método para voltar para a tela principal
            }
        });
    }

    // Método para redefinir a senha do usuário
    private void redefinirSenha() {
        String email = editEmail.getText().toString().trim(); // Obtém o e-mail digitado pelo usuário

        // Verifica se o campo de e-mail está vazio
        if (TextUtils.isEmpty(email)) {
            showToast("Digite seu e-mail para redefinir a senha."); // Exibe uma mensagem solicitando o preenchimento do campo de e-mail
            return;
        }

        // Envia um e-mail de redefinição de senha usando o método sendPasswordResetEmail do FirebaseAuth
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showToast("E-mail para redefinição de senha enviado com sucesso. Verifique sua caixa de entrada."); // Exibe uma mensagem de sucesso
                        } else {
                            showToast("Falha ao enviar e-mail para redefinição de senha. Verifique se o e-mail está correto e tente novamente."); // Exibe uma mensagem de falha
                        }
                    }
                });
    }

    // Método para exibir um Toast com a mensagem fornecida
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Método para voltar para a tela principal (MainActivity)
    public void voltarParaMainActivity() {
        Intent intent = new Intent(Novasenha.this, MainActivity.class);
        startActivity(intent); // Inicia a MainActivity
        finish(); // Finaliza a atividade atual (Novasenha)
    }
}