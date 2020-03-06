package com.registro.horasdetrabajo;

// 2- Seguidamente de haber creado la entidad se crea el DAO(Data Access Object),
// que para este ejercicio solo proporciona consultas para obtener todas las horas,
// insertar horas y eliminar todas las horas.

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HoraDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertHora(Hora hora);

    @Query("SELECT * FROM hora_table ORDER BY hora ASC")
    LiveData<List<Hora>> getAllHoras();

    @Query("SELECT * FROM hora_table LIMIT 1")
    Hora[] getAnyHora();

    @Query("DELETE FROM hora_table")
    void deleteAll();

    @Delete
    void deleteHora(Hora hora);
}