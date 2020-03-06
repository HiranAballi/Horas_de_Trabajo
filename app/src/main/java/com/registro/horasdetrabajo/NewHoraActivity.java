package com.registro.horasdetrabajo;

// 7- Ahora agregará una Actividad que le permite al usuario usar la FAB para ingresar
// nuevas horas.

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewHoraActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.registro.horasdetrabajo.REPLY";
    private EditText mEditHoraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hora);

        mEditHoraView = findViewById(R.id.edit_hora);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if (TextUtils.isEmpty(mEditHoraView.getText())){
                    setResult(RESULT_CANCELED, i);
                } else {
                    String hora = mEditHoraView.getText().toString();
                    i.putExtra(EXTRA_REPLY, hora);
                    setResult(RESULT_OK, i);
                }
                finish();
            }
        });
    }
}
