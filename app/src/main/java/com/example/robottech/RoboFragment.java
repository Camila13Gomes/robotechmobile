package com.example.robottech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class RoboFragment extends Fragment {

    // Interface para lidar com cliques no ImageButton
    public interface OnImageButtonClickListener {
        void onImageButtonClick();
    }

    private OnImageButtonClickListener listener; // Objeto para lidar com cliques no ImageButton

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_robo, container, false);

        // Configurando o botão para baixar APK
        Button btnBaixarAPK = view.findViewById(R.id.btn);
        btnBaixarAPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLinkDropbox(); // Chama o método para abrir o link do Dropbox
            }
        });

        // Configurando o ImageButton
        ImageButton imageButton = view.findViewById(R.id.imageButton6);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirConfiguracoesAplicativos(); // Chama o método para abrir as configurações do aplicativo
            }
        });

        return view; // Retorna a view do fragmento
    }

    // Método para abrir o link do Dropbox no navegador
    private void abrirLinkDropbox() {
        String urlDropbox = "https://www.dropbox.com/scl/fo/oosd2hhulvyw1p2fj1dcc/h?dl=0&preview=LAFVIN_4WD_Robot_Arm_Smart_Car_V2_1.apk&rlkey=62l38tz07mzfnriz71akoumsn";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlDropbox));
        startActivity(intent); // Inicia a atividade para abrir o link do Dropbox
    }

    // Método para abrir as configurações do aplicativo no dispositivo
    private void abrirConfiguracoesAplicativos() {
        String packageName = getActivity().getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); // Inicia a atividade para abrir as configurações do aplicativo
    }
}
