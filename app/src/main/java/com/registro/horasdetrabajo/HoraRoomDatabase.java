package com.registro.horasdetrabajo;

// 3- Después de haber creado el DAO, se crea el Room, que es una capa de base de datos,
// sobre una base de datos SQLite. Room actua como una clase auxiliar de base de datos
// como la SQLiteOpenHelper.
// Room utiliza el DAO para emitir consultas a su base de datos.
// Para evitar un bajo rendimiento de la interfaz de usuario, Room no le permite emitir consultas
// de base de datos en el hilo principal. LiveData aplica esta regla ejecutando automáticamente
// la consulta de forma asíncrona en un subproceso en segundo plano, cuando sea necesario.
// Room proporciona comprobaciones en tiempo de compilación de sentencias SQLite.
// La clase Room debe ser abstracta y extender RoomDatabase.
// Por lo general, solo necesita una instancia de la base de datos de Room para toda la aplicación.

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Hora.class}, version = 5, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class HoraRoomDatabase extends RoomDatabase {

    // Defina los DAO que funcionan con la base de datos. Proporcione un método abstracto
    // "getter" para cada @Dao.
    abstract HoraDao horaDao();

    //Cree HoraRoomDatabase como un singleton para evitar que se abran varias instancias
    // de la base de datos al mismo tiempo, lo que sería algo malo
    private static HoraRoomDatabase INSTANCE;

    static HoraRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HoraRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HoraRoomDatabase.class, "hora_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDataBaseCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Para eliminar tod* el contenido y repoblar la base de datos cada vez que se inicia
    // la aplicación, cree un RoomDatabase.Callback y anule el método onOpen (). Como no puede
    // realizar operaciones de la base de datos de Room en el subproceso de la interfaz de
    // usuario, onOpen () crea y ejecuta una AsyncTask para agregar contenido a la base de datos.
    // Para eliminar tod* el contenido y repoblar la base de datos cada vez que se inicia la
    // aplicación, cree un RoomDatabase.Callback y anule el método onOpen (). Como no puede
    // realizar operaciones de la base de datos de Room en el subproceso de la interfaz de
    // usuario, onOpen () crea y ejecuta una AsyncTask para agregar contenido a la base de datos.
    private static RoomDatabase.Callback sRoomDataBaseCallBack = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    // Cree una clase interna PopulateDbAsync que extienda AsycTask. Implemente el
    // método doInBackground () para eliminar todas las palabras y luego cree otras nuevas. Aquí
    // está el código para AsyncTask
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void>{

        private final HoraDao mDao;
        String[] horas = new String[0];

        PopulateDbAsync(HoraRoomDatabase db) {
            mDao = db.horaDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Cree la lista inicial de horas
            if (mDao.getAnyHora().length < 1){
                for (int i = 0; i <= horas.length - 1; i++){
                    Hora hora = new Hora(horas[i]);
                    mDao.insertHora(hora);
                }
            }
            return null;
        }
    }
}