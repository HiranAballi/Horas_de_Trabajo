package com.registro.horasdetrabajo;

// 1- Lo primero que se crea, cuando vas a trabajar con Bases de datos,
// LiveData y ViewMode, es la entidad que es una clase anotada que
// describe una tabla de base de datos.

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "hora_table")
public class Hora {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "hora")
    private String hora;

    @ColumnInfo(name = "fecha")
    private Date forDay;

    public Hora(@NonNull String hora){
        this.hora = hora;
    }

    @NonNull
    public String getHora() {
        return this.hora;
    }

    public void setForDay(Date forDay) {
        this.forDay = forDay;
    }

    public Date getForDay() {
        return this.forDay;
    }

}