package com.daam.mascotas.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daam.mascotas.PetListActivity;
import com.daam.mascotas.model.PetModel;

public class PetServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // check if notification is cancelled or accepted
        boolean cancel = intent.getExtras().getBoolean(PetService.CANCEL_KEY);

        // update list and number of events
        if(!cancel) {
            PetModel.build().commitPending();
        }else{
            PetModel.build().dismissPending();
        }

        PetListActivity.updateUi();
    }

    public PetServiceReceiver(){
        super();
    }
}
