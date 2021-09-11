package com.example.problemone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class pacienteEstable extends AppCompatActivity {

    private EditText consultaLabel, nglucemia;
    private Button btn_verificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_estable);

        consultaLabel = (EditText) findViewById(R.id.consultaLabel);
        nglucemia = (EditText) findViewById(R.id.nglucemia);
        btn_verificar = (Button) findViewById(R.id.btn_verificar);
    }

    // consulta si existe un paciente con dicha cédula
    public void consultaPorCedula(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "clinica", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = consultaLabel.getText().toString();
        Cursor fila = bd.rawQuery(
                "select nombre,sintoma from pacientes where cedula=" + cod, null);
        if (fila.moveToFirst()) {
            String sintoma = fila.getString(1);
            String message = "";
            if(sintoma.equals("Ninguno")) {
                message = "El paciente " + fila.getString(0) + " no tiene ningún sintoma o patología";
                btn_verificar.setEnabled(false);
            } else {
                message = "El paciente " + fila.getString(0) + " tiene el sintoma " + fila.getString(1);
                // habilita el espacio para consultar la patologia
                btn_verificar.setEnabled(true);
            }

            Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe un paciente con dicha cédula", Toast.LENGTH_SHORT).show();
            // desahabilita el espacio para consultar la patologia
            btn_verificar.setEnabled(false);
        }

        bd.close();
    }

    // elimina un paciente por su cédula
    public void eliminaPorCedula(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "clinica", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod= consultaLabel.getText().toString();
        int cant = bd.delete("pacientes", "cedula=" + cod, null);
        bd.close();
        consultaLabel.setText("");
        nglucemia.setText("");

        if (cant == 1) {
            Toast.makeText(this, "Se borró el paciente con dicha cédula", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No existe un paciente con dicha cédula", Toast.LENGTH_SHORT).show();
        }
        btn_verificar.setEnabled(false);
    }

    // envia por Intent los valores de para actualizar el tipo y la recomendación
    public void ConsultarNivel(View v) {
        Intent i = new Intent(pacienteEstable.this, resultadoDiabetico.class);
        // Creo una variable i donde vamos a mandar la información
        i.putExtra("cedula", consultaLabel.getText()+"");
        double nivelGlucemia = Double.parseDouble(nglucemia.getText().toString());
        String nivelString = DeterminarNombreDePatologia(nivelGlucemia);
        i.putExtra("nivel", nivelString);
        startActivity(i);
    }

    // determina la patologia (tipo db) del paciente
    public String DeterminarNombreDePatologia(double glu) {
        String res = "";
        if(glu >= 7.0 && glu < 13.8) {
            res = "HIPERGLICEMIA AISLADA";
        } else if(glu >= 13.8 && glu < 33) {
            res = "CETOACIDOSIS DIABÉTICA";
        } else if(glu >= 33.0) {
            res = "ESTADO HIPEROSMOLAR HIPERGLUCÉMICO NO CETÓSICO";
        } else {
            res = "Ninguna patología";
        }
        return res;
    }
}