package com.daam.mascotas.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        int requestCode = intent.getExtras().getInt("requestCode");
        boolean updatePetList = (requestCode == PetService.SUCCESS_NOTIFICATION_INTENT);

        if(updatePetList){
            Log.d("ASDF", "updating list");
        }else{
            Log.d("ASDF", "NOT updating list");
        }

        // update list and number of events
        List<Pet> pets = (List<Pet>) intent.getSerializableExtra(PetService.PET_KEY);
        PetModel model = PetModel.build();
        model.update(pets, updatePetList);

        // update UI
        // MainActivity.getInstance().updateUi();
    }



}
