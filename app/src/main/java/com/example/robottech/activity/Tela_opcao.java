package com.example.robottech.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.robottech.Loja1Fragment;
import com.example.robottech.R;
import com.example.robottech.RoboFragment;
import com.example.robottech.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tela_opcao extends AppCompatActivity {

    static BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_opcao);

        // Referência ao BottomNavigationView no layout XML
        navigationView = findViewById(R.id.bottomNavigation);

        // Configuração do listener de seleção de item do BottomNavigationView
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                // Se o item selecionado for 'RoboFragment'
                case R.id.navrobo:
                    // Substitui o conteúdo do placeholder com o RoboFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, new RoboFragment())
                            .commit();
                    return true;
                // Se o item selecionado for 'Loja1Fragment'
                case R.id.navloja:
                    // Substitui o conteúdo do placeholder com o Loja1Fragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, new Loja1Fragment())
                            .commit();
                    return true;
                // Se o item selecionado for 'UserFragment'
                case R.id.navuser:
                    // Substitui o conteúdo do placeholder com o UserFragment
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholder, new UserFragment())
                            .commit();
                    return true;
            }
            return false;
        });

        // Define o item selecionado como 'UserFragment' por padrão
        navigationView.setSelectedItemId(R.id.navuser);
    }
}
