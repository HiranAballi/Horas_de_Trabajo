package com.registro.horasdetrabajo;

// 4- Seguidamente de crear la base de datos se crea un repositorio, que maneja las operaciones
// de datos y proporciona una API limpia al resto de la aplicación para los datos de la aplicación.

// Un repositorio gestiona hilos de consulta y le permite usar múltiples backends.
// En el ejemplo más común, el repositorio implementa la lógica para decidir si buscar datos
// de una red o usar resultados almacenados en caché en la base de datos local.

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HoraRepository {

    private HoraDao mHoraDao;
    private LiveData<List<Hora>> mAllHoras;

    public HoraRepository(Application application) {
        HoraRoomDatabase db = HoraRoomDatabase.getDatabase(application);
        mHoraDao = db.horaDao();
        mAllHoras = mHoraDao.getAllHoras();
    }

    LiveData<List<Hora>> getAllHoras(){
        return mAllHoras;
    }

    public void insert (Hora hora){
        new insertAsyncTask(mHoraDao).execute(hora);
    }

    public void deleteAll(){
        new deleteAllHorasAsyncTask(mHoraDao).execute();
    }

    public void deleteHora(Hora hora){
        new deleteHoraAsyncTask(mHoraDao).execute(hora);
    }

    private static class insertAsyncTask extends AsyncTask<Hora, Void, Void>{

        private HoraDao mAsyncTaskDao;

        insertAsyncTask(HoraDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Hora... horas) {
            mAsyncTaskDao.insertHora(horas[0]);
            return null;
        }
    }

    private static class deleteAllHorasAsyncTask extends AsyncTask<Void, Void, Void>{

        private HoraDao mAsyncTaskDao;

        deleteAllHorasAsyncTask(HoraDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteHoraAsyncTask extends AsyncTask<Hora, Void,Void>{

        private HoraDao mAsyncTaskDao;

        deleteHoraAsyncTask (HoraDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Hora... horas) {
            mAsyncTaskDao.deleteHora(horas[0]);
            return null;
        }
    }
}