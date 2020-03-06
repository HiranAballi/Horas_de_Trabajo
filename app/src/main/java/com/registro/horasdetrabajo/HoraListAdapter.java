package com.registro.horasdetrabajo;

// 6- Se crea esta clase
// Va a mostrar los datos en un RecyclerView, que es un poco mejor que simplemente lanzar
// los datos en un TextView.

// El adaptador almacena datos en caché y llena el RecyclerView con él. La clase interna
// HoraViewHolder contiene y administra una vista para un elemento de la lista.

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HoraListAdapter extends RecyclerView.Adapter<HoraListAdapter.HoraViewHolder> {

    private final LayoutInflater mInflater;
    private List<Hora> mHoras;

    public HoraListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HoraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new HoraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HoraViewHolder holder, int position) {
        if (mHoras != null){
            Hora current = mHoras.get(position);
            holder.horaItemView.setText("" + current.getHora());
            holder.fechaItemView.setText("" + getCurrentDateTime(current.getForDay()));
        } else {
            holder.horaItemView.setText("No hay Hora");
        }
    }

    void setHoras(List<Hora> horas){
        mHoras = horas;
        notifyDataSetChanged();
    }

    private String getCurrentDateTime(Date fecha){
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(fecha);
    }

    public Hora getHoraAtPosition(int position){
        return mHoras.get(position);
    }

    @Override
    public int getItemCount() {
        if (mHoras != null)
            return mHoras.size();
        else return 0;
    }

    class HoraViewHolder extends RecyclerView.ViewHolder {
        private final TextView horaItemView;
        private final TextView fechaItemView;

        private HoraViewHolder(View itemView) {
            super(itemView);
            horaItemView = itemView.findViewById(R.id.textView);
            fechaItemView = itemView.findViewById(R.id.textView2);
        }
    }
}
