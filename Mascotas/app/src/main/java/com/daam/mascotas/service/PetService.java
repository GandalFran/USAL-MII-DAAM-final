package com.daam.mascotas.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.daam.mascotas.Constants;
import com.daam.mascotas.MainActivity;
import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.bean.PetAdapter;
import com.daam.mascotas.model.PetModel;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class PetService extends Service {

    private PetModel model;
    private List<Pet> petsToRegister;

    @Override
    public void onCreate() {
        super.onCreate();
        this.model = PetModel.build(null);
        this.petsToRegister = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // thread created to allow network requests
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Log.d("ASDF", "POLLING");
                    // sleep
                    try {
                        Thread.sleep(Constants.PET_POLL_INTERVAL);
                    } catch (InterruptedException e) {
                    }

                    // retrieve p
                    Pet p = PetService.this.checkForPets(Constants.CENTINEL_URL);
                    if(p!=null && !PetService.this.model.exists(p) && !PetService.this.exists(p)){
                        PetService.this.petsToRegister.add(p);
                        PetService.this.notifyPet(p);
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Pet checkForPets(String url){

        PetLoader loader;
        if(url.toLowerCase().contains("json")){
            loader = new JSONPetLoaderImpl();
        }else if(url.toLowerCase().contains("xml")){
            loader = new XMLPetLoaderImpl();
        }else{
            loader = new TextPetLoaderImpl();
        }

        Pet p = null;
        try {
            p = loader.loadOne(url);
        }catch (Exception ignored){
            Log.d("ASDF", "exception");
        }

        return p;
    }

    private void notifyPet(Pet p){
        Log.d("ASDF", "Nueva mascota: " + p.toString());
    }

    private boolean exists(Pet p) {
        for(Pet pet: this.petsToRegister){
            if(pet.getId().equals(p.getId())){
                return true;
            }
        }
        return false;
    }
}
