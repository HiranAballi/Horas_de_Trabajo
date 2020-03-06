package com.registro.horasdetrabajo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Se crea una variable miembro para ViewModel, porque todas las interacciones de la actividad
    // son solo con WordViewModel.
    private HoraViewModel mHoraViewModel;
    private TextView txt;

    public static final int NEW_HOUR_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.textView);
        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab = findViewById(R.id.fab);

        if (recuperarPreferencia() == -1){
            Intent i = new Intent();
            i.setClass(getApplicationContext(), ConfigActivity.class);
            startActivity(i);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewHoraActivity.class);
                startActivityForResult(intent, NEW_HOUR_ACTIVITY_REQUEST_CODE);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final HoraListAdapter adapter = new HoraListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Cojes un ViewModel de la clase ViewModelProviders.
        mHoraViewModel = ViewModelProviders.of(this).get(HoraViewModel.class);

        // Cuando los datos observados cambian mientras la actividad está en primer plano, se
        // invoca el método onChanged () y actualiza los datos almacenados en caché en el
        // adaptador. Tenga en cuenta que en este caso, cuando se abre la aplicación, se agregan
        // los datos iniciales, por lo que se llama al método onChanged ().
        mHoraViewModel.getAllHoras().observe(this, new Observer<List<Hora>>() {
            @Override
            public void onChanged(List<Hora> horas) {
                adapter.setHoras(horas);
                double sumHoras = mHoraViewModel.getSumaHoras(horas, recuperarPreferencia());
                String stringSum = String.valueOf(sumHoras);
                txt.setText(stringSum);
            }
        });

        // Funcionalidad para deslizar elementos en la vista de reciclador para
        // eliminar ese elemento
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        // Se coge la posicion
                        int position = viewHolder.getAdapterPosition();

                        // Se define el adapatador
                        Hora myHora = adapter.getHoraAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                "Deleting " + myHora.getHora() + " hours.",
                                Toast.LENGTH_SHORT).show();

                        // Se elimina la hora llamando al metodo del ViewModel
                        mHoraViewModel.deleteHora(myHora);
                    }
                }
        );
        helper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_HOUR_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            String time = data.getStringExtra(NewHoraActivity.EXTRA_REPLY);
            Hora hora = new Hora(time);
            Date date = getCurrentDateTime();
            hora.setForDay(date);
            mHoraViewModel.insert(hora);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.setting){
            Intent i = new Intent(MainActivity.this, ConfigActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private Date getCurrentDateTime(){
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    private float recuperarPreferencia(){
        SharedPreferences dato = getSharedPreferences("info", 0);
        String stringdinHora = dato.getString("EuroHora", "-1");
        float dinHora = Float.parseFloat(stringdinHora);
        return dinHora;
    }

    private void showDialogDelete(){
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage(R.string.confirmation)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHoraViewModel.deleteAllDatabase();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                }).show();
    }

}