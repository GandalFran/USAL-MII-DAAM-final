package com.daam.mascotas.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.daam.mascotas.Constants;
import com.daam.mascotas.PetListActivity;
import com.daam.mascotas.R;
import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.PetModel;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class PetService extends Service {

    public static final String PET_KEY = "PET_KEY";
    public static final String CANCEL_KEY = "CANCEL_KEY";

    private static final int PET_NOTIFICATION_ID = 1;
    private final String NOTIFICATION_CHANNEL_ID = "com.daam.mascotas.petservice";

    private int currentOrder = 1;
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        this.currentOrder = 0;

        // build notification channel
        this.manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String name = getString(R.string.notification_title);
        String description = getString(R.string.notification_description);
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.enableVibration(true);
        this.manager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // thread created to allow network requests
        new Thread(new Runnable() {
            @Override
            public void run() {
                PetService.this.pollForPets();
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

    public void pollForPets(){
        while(true) {
            // retrieve p
            Pet p = PetService.this.checkForPets(Constants.CENTINEL_URL);
            p.setOrder(++this.currentOrder);

            if(!PetModel.build().getPending().contains(p) && !PetModel.build().getPets().contains(p)){
                PetModel.build().addPending(p);
                PetService.this.notifyPets();
            }

            // sleep
            try {
                Thread.sleep(Constants.PET_POLL_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
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

    private void notifyPets(){
        Intent successIntent = new Intent(PetService.this, PetServiceReceiver.class);
        successIntent.putExtra(CANCEL_KEY, false);
        successIntent.putParcelableArrayListExtra(PET_KEY, (ArrayList<? extends Parcelable>) PetModel.build().getPending());
        PendingIntent successPendingIntent = PendingIntent.getBroadcast (getApplicationContext(), 0, successIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(PetService.this, PetServiceReceiver.class);
        cancelIntent.putExtra(CANCEL_KEY, true);
        cancelIntent.putParcelableArrayListExtra(PET_KEY, (ArrayList<? extends Parcelable>) PetModel.build().getPending());
        PendingIntent deletePendingIntent = PendingIntent.getBroadcast (getApplicationContext(), 1, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews("com.daam.mascotas", R.layout.notification_layout);
        contentView.setTextViewText(R.id.numReceivedNotification, Integer.valueOf(PetModel.build().getPending().size()).toString());

        Notification.Builder builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentIntent(successPendingIntent)
                .setDeleteIntent(deletePendingIntent)
                .setCustomContentView(contentView);

        this.manager.notify(PET_NOTIFICATION_ID, builder.build());
    }
}
