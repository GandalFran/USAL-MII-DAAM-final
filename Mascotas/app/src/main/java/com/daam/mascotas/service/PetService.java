package com.daam.mascotas.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.daam.mascotas.Constants;
import com.daam.mascotas.R;
import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class PetService extends Service {

    private static final int PET_NOTIFICATION_ID = 1;
    private static final String PET_KEY = "PET_KEY";
    private final String NOTIFICATION_CHANNEL_ID = "com.daam.mascotas.petservice";

    private List<Pet> receivedPets;
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        this.buildNotificationChannel();
        this.receivedPets = new ArrayList<>();
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
                    if(p!=null && !PetService.this.exists(p)){
                        PetService.this.receivedPets.add(p);
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

    private void buildNotificationChannel() {
        this.manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String name = getString(R.string.notification_title);
        String description = getString(R.string.notification_description);
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.enableVibration(true);
        this.manager.createNotificationChannel(channel);
    }

    private void notifyPet(Pet p){
        Log.d("ASDF", "Nueva mascota: " + p.toString());
        Intent intent = new Intent(this, PetServiceReceiver.class);
        intent.putExtra(PET_KEY, (Parcelable) p);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(
                getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentTitle("Notificación desde la IU")
                .setContentIntent(pendingIntent);
        this.manager.notify(PET_NOTIFICATION_ID, builder.build());
    }

    private boolean exists(Pet p) {
        for(Pet pet: this.receivedPets){
            if(pet.getId().equals(p.getId())){
                return true;
            }
        }
        return false;
    }
}
