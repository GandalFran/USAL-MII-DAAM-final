package com.daam.mascotas.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daam.mascotas.MainActivity;
import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.PetModel;

import java.util.List;

public class PetServiceReceiver extends BroadcastReceiver {

    public PetServiceReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ASDF", String.format("recibido"));

        // check if notification is cancelled or accepted
        boolean cancel = intent.getExtras().getBoolean(PetService.CANCEL_KEY);

        // update list and number of events
        if(!cancel) {
            PetModel.build().commitPending();
        }else{
            PetModel.build().dismissPending();
        }

        // update UI
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }




}
