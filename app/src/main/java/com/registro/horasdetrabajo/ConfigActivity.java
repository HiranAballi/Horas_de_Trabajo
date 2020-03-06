package com.registro.horasdetrabajo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Button btn_Confirmar = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.editText);

        btn_Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.toast, Toast.LENGTH_LONG).show();
                } else {
                    String num = editText.getText().toString();
                    SharedPreferences dato = getSharedPreferences("info", 0);
                    SharedPreferences.Editor editor = dato.edit();
                    editor.putString("EuroHora", num);
                    editor.apply();

                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });
        SharedPreferences dato = getSharedPreferences("info", 0);
        String stringdinHora = dato.getString("EuroHora", "-1");
        editText.setText(stringdinHora);
    }
}
