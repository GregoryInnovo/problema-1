package com.example.problemone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class resultadoDiabetico extends AppCompatActivity {
    // mensajes que detallan las recomendaciones
    String patologia1 = "HIPERGLICEMIA AISLADA: Indicar glucemia en ayunas y TGP en pacientes sin diagnóstico.\n" +
            "- Si deshidratación, rehidratación oral o EV según las demandas. \n" +
            "- Reevaluar conducta terapéutica en diabéticos y cumplimiento de los pilares.\n" +
            "- Reevaluar dosis de hipoglucemiantes.";

    String patologia2 = "CETOACIDOSIS DIABÉTICA: Coordinar traslado y comenzar tratamiento.\n" +
            "- Hidratación con Solución salina 40 ml/Kg en las primeras 4 horas. 1-2 L la primera hora. \n" +
            "- Administrar potasio al restituirse la diuresis o signos de hipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T).\n" +
            "- Evitar insulina hasta desaparecer signos de hipopotasemia. \n" +
            "- Administrar insulina simple 0,1 U/kg EV después de hidratar";

    String patologia3 = "ESTADO HIPEROSMOLAR HIPERGLUCÉMICO NO CETÓSICO: Coordinar traslado y comenzar tratamiento.\n" +
            "- Hidratación con SoluciónSalina 10-15 ml/Kg/h hasta conseguir estabilidad hemodinámica. \n" +
            "- Administrar potasio al restituirse la diuresis o signos de hipopotasemia (depresión del ST, Onda U ≤ 1mv, ondas U≤ T).";

    private EditText consultaLabel, consultaNivel;
    private TextView richTextRecomendacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_diabetico);

        consultaLabel = (EditText) findViewById(R.id.consultaLabel);
        consultaNivel = (EditText) findViewById(R.id.consultaNivel);
        richTextRecomendacion = (TextView) findViewById(R.id.richTextRecomendacion);

        String labelInfo = getIntent().getStringExtra("cedula");
        String nglucemia = getIntent().getStringExtra("nivel");

        consultaLabel.setText(labelInfo);
        consultaNivel.setText(nglucemia);

        // consulta si hay un dato ya guardado para su recomendación
        consultarRecomendacion();

    }

    // obtiene la recomendación para que el usuario la vea
    public void ObtenerRecomendacion(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"clinica", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String cod = consultaLabel.getText().toString();
        String patologia = consultaNivel.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("tipo", patologia);

        int cant = bd.update("pacientes", registro, "cedula=" + cod, null);
        bd.close();
        if (cant == 1) {
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No existe un paciente con la cédula ingresada", Toast.LENGTH_SHORT).show();
        }

        String nivelString = consultaNivel.getText().toString();

        String message = DeterminarRecomendacion(nivelString);

        richTextRecomendacion.setText(message);
    }

    // determina que recomendación se debe mostrar
    public String DeterminarRecomendacion(String n) {
        String res = "";
        if(n.equalsIgnoreCase("HIPERGLICEMIA AISLADA")) {
            res = patologia1;
        } else if(n.equalsIgnoreCase("CETOACIDOSIS DIABÉTICA")) {
            res = patologia2;
        } else if(n.equalsIgnoreCase("ESTADO HIPEROSMOLAR HIPERGLUCÉMICO NO CETÓSICO")) {
            res = patologia3;
        } else {
            res = "No hay recomendaciones";
        }
        return res;
    }

    // función que determina si un paciente ha se la hecho la recomendación
    public void consultarRecomendacion() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"clinica", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod = consultaLabel.getText().toString();
        Cursor fila = bd.rawQuery("select tipo from pacientes where cedula=" + cod, null);
        if (fila.moveToFirst()) {
            String descripcionPatologia = fila.getString(0);
            String message = "Resultado y recomendaciones";
            if(descripcionPatologia.equals("Ninguno")) {
                richTextRecomendacion.setText(message);
            } else {
                String recomendaciones = DeterminarRecomendacion(descripcionPatologia);
                richTextRecomendacion.setText(recomendaciones);
            }
        } else {
            Toast.makeText(this, "No existe un paciente con dicha cédula",
                    Toast.LENGTH_SHORT).show();
        }

        bd.close();
    }
}