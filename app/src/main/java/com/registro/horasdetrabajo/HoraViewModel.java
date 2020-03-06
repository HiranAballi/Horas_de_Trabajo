package com.registro.horasdetrabajo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// 5- En este paso haremos uso de la clase ViewModel, cuya función es proporcionar
// datos a la interfaz de usuario y sobrevivir a los cambios de configuración.

// Un ViewModel actúa como un centro de comunicación entre el Repositorio y la IU.
// ViewModel es parte de la biblioteca del ciclo de vida.

// La separación de los datos de la interfaz de usuario de su aplicación de sus clases de Actividad
// y Fragmento le permite seguir mejor el principio de responsabilidad única: sus actividades
// y fragmentos son responsables de dibujar datos en la pantalla, mientras que su ViewModel
// es responsable de mantener y procesar todos los datos necesarios para la interfaz de usuario.

// En ViewModel, use LiveData para los datos modificables que la UI usará o mostrará.

// Warnings:
// 1- Nunca pase el contexto a instancias de ViewModel.
// 2- No almacene instancias de actividad, fragmento o vista o su contexto en el modelo de vista.

public class HoraViewModel extends AndroidViewModel {

    //Agrega una variable miembro privada que guarde la referencia al repositorio
    private HoraRepository mRepository;

    //Añade una variable miembro privada, al cache la lista de horas.
    private LiveData<List<Hora>> mAllHoras;

    public HoraViewModel(Application application) {
        super(application);
        mRepository = new HoraRepository(application);
        mAllHoras = mRepository.getAllHoras();
    }

    // Agregue un método "getter" que obtenga todas las palabras. Esto oculta completamente
    // la implementación del UI.
    LiveData<List<Hora>> getAllHoras(){
        return mAllHoras;
    }

    // Cree un método wrapper insert () que llame al método insert () del Repositorio. De esta
    // manera, la implementación de insert () está completamente oculta de la interfaz de usuario.
    public  void insert(Hora hora){
        mRepository.insert(hora);
    }

    public void deleteAllDatabase(){
        mRepository.deleteAll();
    }

    public void deleteHora(Hora hora){
        mRepository.deleteHora(hora);
    }

    public double getSumaHoras(List<Hora> horas, double precio){
        int sum = 0;
        for(Hora hora: horas){
            sum += Integer.parseInt(hora.getHora());
        }
        return sum * precio;
    }
}