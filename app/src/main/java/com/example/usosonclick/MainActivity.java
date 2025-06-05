package com.example.usosonclick;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Constantes y propiedades
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgviewFoto;
    private Uri imgURI;
    ListView lstdepartamentos;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Referencias de UI
        lstdepartamentos = findViewById(R.id.lstDepartamentos);
        imgviewFoto = findViewById(R.id.ivImagenLogo);
        Switch swModo = findViewById(R.id.SwCambiarModo);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        EditText tvestadomodo = findViewById(R.id.edtEstadoModo);
        CheckBox chkboxdesamodo = findViewById(R.id.chboxDesactivacambiomodo);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        ImageButton imgbuttonfecha = findViewById(R.id.imgButtonFecha);
        ImageButton imgbuttonhora = findViewById(R.id.imgButtonHora);

        // Inicializar ListView
        items = new ArrayList<>();
        items.add("1 - Ahuchapán");
        items.add("2 - Santa Ana");
        items.add("3 - Sonsonate");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lstdepartamentos.setAdapter(adapter);

        // Restaurar imagen si se cambia entre modos
        if (savedInstanceState != null) {
            String uriGuardada = savedInstanceState.getString("img_uri");
            if (uriGuardada != null) {
                imgURI = Uri.parse(uriGuardada);
                imgviewFoto.setImageURI(imgURI);
            }
        }

        // Ajustes visuales
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Evento Switch
        swModo.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Cambiando modo")
                    .setMessage("Se cambiará el modo de la aplicación")
                    .setPositiveButton("OK", null)
                    .show();

            boolean estado = swModo.isChecked();
            if (estado) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                tvestadomodo.setText("Modo oscuro está activado");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                tvestadomodo.setText("");
            }
        });

        // Evento RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                RadioButton radioButtonSelec = findViewById(checkedId);
                int indexbutton = group.indexOfChild(radioButtonSelec);

                if (indexbutton == 1) {
                    View dialogView = getLayoutInflater().inflate(R.layout.dialogo_personalizado, null);
                    EditText razonocultar = dialogView.findViewById(R.id.edtJustificacion);

                    new AlertDialog.Builder(MainActivity.this)
                            .setView(dialogView)
                            .setTitle("Venta - Razón para ocultar")
                            .setPositiveButton("Registrar", (dialog, which) -> {
                                String razon = razonocultar.getText().toString();
                                tvestadomodo.setText(razon);
                            })
                            .setNegativeButton("Cancelar", (dialog, i) -> tvestadomodo.setText(""))
                            .show();

                    imgviewFoto.setVisibility(View.INVISIBLE);
                } else {
                    imgviewFoto.setVisibility(View.VISIBLE);
                    tvestadomodo.setText("");
                }
            }
        });

        // Evento FloatingActionButton
        fab.setOnClickListener(view -> finish());

        // Evento CheckBox
        chkboxdesamodo.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirmar cambio de modo")
                    .setMessage("¿Está seguro que desea deshabilitar el cambio de modo?")
                    .setPositiveButton("Sí", (dialog, i) -> swModo.setEnabled(!chkboxdesamodo.isChecked()))
                    .setNegativeButton("No", (dialog, i) -> chkboxdesamodo.setChecked(!chkboxdesamodo.isChecked()))
                    .show();
        });

        // Evento ListView (modificar texto de ítem)
        lstdepartamentos.setOnItemClickListener((adapterView, view, position, id) -> {
            TextView itemselected = (TextView) view;
            EditText nuevotexto = new EditText(MainActivity.this);
            nuevotexto.setInputType(InputType.TYPE_CLASS_TEXT);

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Ingrese el nuevo valor para el elemento seleccionado")
                    .setView(nuevotexto)
                    .setPositiveButton("Actualizar", (dialog, which) -> {
                        String textonuevo = nuevotexto.getText().toString();
                        itemselected.setText(textonuevo);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Evento ImageView (abrir galería)
        imgviewFoto.setOnClickListener(view -> OpenGalery());

        // Evento botón fecha
        imgbuttonfecha.setOnClickListener(view -> {
            LocalDate now = LocalDate.now();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view1, year, month, dayOfMonth) -> {
                        String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvestadomodo.setText("Fecha: " + fecha);
                    },
                    now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth()
            );
            datePickerDialog.show();
        });

        // Evento botón hora
        imgbuttonhora.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int minutos = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    MainActivity.this,
                    (view12, hourOfDay, minute) -> {
                        String horaStr = hourOfDay + ":" + String.format("%02d", minute);
                        tvestadomodo.setText("Hora: " + horaStr);
                    },
                    hora, minutos, true
            );
            timePickerDialog.show();
        });
    }

    // Método para abrir galería de imágenes
    private void OpenGalery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    // Recibir imagen seleccionada
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgURI = data.getData();
            imgviewFoto.setImageURI(imgURI);
        }
    }

    // Guardar URI de imagen al rotar pantalla o cambiar modo
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imgURI != null) {
            outState.putString("img_uri", imgURI.toString());
        }
    }
}
