package com.example.usosonclick;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgviewFoto;
    private Uri imgURI;
    private ListView lstdepartamentos;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        lstdepartamentos = findViewById(R.id.lstDepartamentos);
        items = new ArrayList<>();
        items.add("1 - Ahuachapán");
        items.add("2 - Santa Ana");
        items.add("3 - Sonsonate");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lstdepartamentos.setAdapter(adapter);

        imgviewFoto = findViewById(R.id.ivImagenLogo);
        if (savedInstanceState != null) {
            String uriGuardada = savedInstanceState.getString("img_uri");
            if (uriGuardada != null) {
                imgURI = Uri.parse(uriGuardada);
                imgviewFoto.setImageURI(imgURI);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Switch swModo = findViewById(R.id.SwCambiarModo);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        EditText tvestadomodo = findViewById(R.id.edtEstadoModo);
        CheckBox chkboxdesamodo = findViewById(R.id.chboxDesactivacambiomodo);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        swModo.setOnClickListener(view -> {
            boolean estado = ((Switch) view).isChecked();
            if (estado) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                tvestadomodo.setText("Modo oscuro está activado");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                tvestadomodo.setText("");
            }
        });

        chkboxdesamodo.setOnClickListener(view -> swModo.setEnabled(!chkboxdesamodo.isChecked()));

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                RadioButton radioButtonSelec = findViewById(checkedId);
                int indexbutton = group.indexOfChild(radioButtonSelec);
                if (indexbutton == 1) {
                    imgviewFoto.setVisibility(View.INVISIBLE);
                } else {
                    imgviewFoto.setVisibility(View.VISIBLE);
                }
            }
        });

        lstdepartamentos.setOnItemClickListener((adapterView, view, position, id) -> {
            String elementoSeleccionado = items.get(position);
            View rootLayout = findViewById(R.id.main);
            Snackbar.make(rootLayout, "El elemento seleccionado es: " + elementoSeleccionado, Snackbar.LENGTH_SHORT).show();
        });

        fab.setOnClickListener(view -> finish());

        imgviewFoto.setOnClickListener(view -> openGallery());
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imgURI != null) {
            outState.putString("img_uri", imgURI.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgURI = data.getData();
            imgviewFoto.setImageURI(imgURI);
        }
    }
}
