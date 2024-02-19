package com.example.robottech.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.robottech.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    // Declarações de variáveis

    private static final int RC_SIGN_IN = 123; // Código de solicitação para login via Google
    private GoogleApiClient mGoogleApiClient; // Cliente Google API para autenticação
    private FirebaseAuth mAuth; // Objeto de autenticação do Firebase
    private TextView text_tela_cadastro; // TextView para redirecionar para a tela de cadastro
    private TextView text_telanovasenha; // TextView para redirecionar para a tela de nova senha

    private EditText edit_emaillogin, edit_senhalogin; // Campos de entrada de email e senha
    private Button bt_entrar; // Botão para iniciar o processo de login
    private ProgressBar progressBar; // Barra de progresso durante o login

    String[] mensagens = {"Por favor, preencha todos os campos", "Login Efetuado com Sucesso", "Digite uma senha com no minimo 6 caracteres", "Usuario já cadastrado", "Email invalido, digite email correto", "Erro ao logar"};// Mensagens para diferentes estados de autenticação

    private static final String CHANNEL_ID = "com.example.robottech.NOTIFICATIONS";
    private static final CharSequence CHANNEL_NAME = "RobotTech Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications from RobotTech App";
    private boolean senhaVisivel = false;  // Indicador de visibilidade da senha


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel(); // Cria o canal de notificação
        IniciarComponentes();// Inicializa os componentes da interface do usuário

        // Inicializa e configura os listeners para os botões e campos de texto
        ImageButton btnMostrarSenha = findViewById(R.id.btnMostrarSenha);
        EditText editSenha = findViewById(R.id.edit_senhalogin);

        // Configuração do listener para redirecionar para a tela de cadastro
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });

        // Configuração do listener para o botão de login
        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se os campos de email e senha estão preenchidos
                String email = edit_emaillogin.getText().toString();
                String senha = edit_senhalogin.getText().toString();

                // Exibe uma mensagem de aviso caso os campos não estejam preenchidos
                if (email.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AutenticarUsuario(v); // Inicia o processo de autenticação do usuário


                }
            }
        });

        // Configuração do listener para redirecionar para a tela de nova senha
        text_telanovasenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Novasenha.class);
                startActivity(intent1);
            }
        });

        // Configura um listener para o botão de alternar visibilidade da senha
        btnMostrarSenha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Alternar a visibilidade da senha
                if (senhaVisivel) {
                    // Se a senha estiver visível, ocultá-la
                    editSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnMostrarSenha.setImageResource(R.drawable.ic_eye);
                    senhaVisivel = false;
                } else {
                    // Se a senha estiver oculta, torná-la visível
                    editSenha.setInputType(InputType.TYPE_CLASS_TEXT);
                    btnMostrarSenha.setImageResource(R.drawable.ic_eye);
                    senhaVisivel = true;
                }
            }
        });


        // Configuração do listener para o botão de login com o Google
        SignInButton signInButton = findViewById(R.id.sing_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();// Inicia o processo de login com o Google
            }
        });

        configureGoogleSignIn();// Configura a autenticação com o Google
        mAuth = FirebaseAuth.getInstance(); // Inicializa a instância de autenticação do Firebase
    }

    // Configura as opções de login com o Google
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail() // Solicita acesso ao endereço de e-mail
                .build();

        // Configura o cliente do Google API
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        // Em caso de falha na conexão com o Google API
                        Log.d("TAG", "Conexão falhou: " + connectionResult.getErrorMessage());

                        // Exibir uma mensagem de erro para o usuário usando Snackbar
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Falha na conexão", Snackbar.LENGTH_LONG);
                        snackbar.show();

                        Toast.makeText(MainActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso) // Adiciona a API de login do Google
                .build();
    }

    //Inicia o processo de login com o Google.
    private void signInWithGoogle() {
        // Cria um intent para o login com o Google
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);  // Inicia a atividade para o login
    }


    // Verifica o resultado do login com o Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            // Se o login for bem-sucedido, obtém a conta do usuário e autentica com o Firebase
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

            }
        }
    }

    // Obtém as credenciais de autenticação do Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Se a autenticação for bem-sucedida, obtém o usuário autenticado e navega para a tela principal

                    FirebaseUser user = mAuth.getCurrentUser();
                    TelaPrincipal();
                } else {
                    // Em caso de falha na autenticação, exibe uma mensagem de erro
                    Toast.makeText(MainActivity.this, "Falha no login do Google", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para desconectar o usuário da conta do Google
    private void desconectarContaGoogle() {
        FirebaseAuth.getInstance().signOut(); // Desconectar do Firebase
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            // Limpar dados ou executar ações após desconectar
                        }
                    });
        }
    }


    // Realiza a autenticação no Firebase com e-mail e senha fornecidos
    private void AutenticarUsuario(View view) {

        String email = edit_emaillogin.getText().toString();
        String senha = edit_senhalogin.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                            enviarNotificacao(); // Chamando o método para enviar a notificação após o login
                        }
                    }, 4000);
                } else {

                    String erro;
                    try {
                        throw task.getException();

                    } catch (Exception e) {

                        erro = mensagens[5];
                    }

                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();


                }

            }
        });

        desconectarContaGoogle();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser userAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (userAtual != null) {
            TelaPrincipal(); // Navega para a tela principal
        }
    }

    /**
     * Redireciona para a tela principal do aplicativo.
     * Inicia uma nova atividade e finaliza a atividade atual.
     */
    private void TelaPrincipal() {
        Intent intent = new Intent(MainActivity.this, Tela_opcao.class);
        startActivity(intent); // Inicia a tela de opções
        finish(); // Finaliza a atividade atual
    }

    /**
     * Inicializa os componentes da interface do usuário.
     */
    private void IniciarComponentes() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        text_telanovasenha = findViewById(R.id.text_telanovasenha);
        edit_emaillogin = findViewById(R.id.edit_emaillogin);
        edit_senhalogin = findViewById(R.id.edit_senhalogin);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
    }

    /**
     * Cria um canal de notificação para dispositivos com Android Oreo (API 26) ou superior.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Envia uma notificação ao usuário após o login bem-sucedido.
     * Cria e exibe uma notificação simples com um ícone, título e texto.
     */
    private void enviarNotificacao() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        // ID único para a notificação
        int notificationId = 1;

        // Constrói a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.robo) // Ícone da notificação
                .setContentTitle("Bem-vindo!") // Título da notificação
                .setContentText("Você realizou o login com sucesso.") // Texto da notificação
                .setPriority(NotificationCompat.PRIORITY_DEFAULT); // Prioridade da notificação

        // Exibe a notificação
        notificationManager.notify(notificationId, builder.build());
    }
}