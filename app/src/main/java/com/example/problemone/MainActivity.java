package com.example.problemone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private EditText pet1, pet2, pet3, pet4;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pet1=(EditText)findViewById(R.id.pet1);
        pet2=(EditText)findViewById(R.id.pet2);
        pet3=(EditText)findViewById(R.id.pet3);
        pet4=(EditText)findViewById(R.id.pet4);

        spinner1 = (Spinner) findViewById(R.id.pSpinner);
        String [] opciones = {"Ninguno","Cuadro neurovegetativos","Transtornos de conciencia","Signos de deshidratación","Sepsis", "Patologías agudas cardiovascular neurológica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);
    }

    // cambia a la pantalla de consultar paciente
    public void CambiarAEstado(View v) {
        Intent i = new Intent(MainActivity.this, pacienteEstable.class);
        startActivity(i);
    }

    // registra un paciente a sqlite
    public void RegistrarPaciente(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "clinica", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nom = pet1.getText().toString();
        String ape = pet2.getText().toString();
        String ced = pet3.getText().toString();
        String eps = pet4.getText().toString();
        String sin = spinner1.getSelectedItem().toString();

        ContentValues registro = new ContentValues();
        registro.put("nombre", nom);
        registro.put("apellido", ape);
        registro.put("cedula", ced);
        registro.put("eps", eps);
        registro.put("sintoma", sin);

        bd.insert("pacientes", null, registro);
        bd.close();
        pet1.setText("");
        pet2.setText("");
        pet3.setText("");
        pet4.setText("");
        String [] opciones = {"Ninguno","Cuadro neurovegetativos","Transtornos de conciencia","Signos de deshidratación","Sepsis", "Patologías agudas cardiovascular neurológica"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner1.setAdapter(adapter);
        Toast.makeText(this, "Se cargaron los datos del paciente", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(MainActivity.this, pacienteEstable.class);
        startActivity(i);
    }

}