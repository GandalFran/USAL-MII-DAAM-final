package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.bean.PetAdapter;
import com.daam.mascotas.model.PetModel;
import com.daam.mascotas.service.PetService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PETS_LIST_STATUS = "PETS_LIST_STATUS";

    private Button button;
    private TextView numEventsTv;
    private ListView petsListView;
    private TextView numProcessedTv;
    private ProgressBar progressBar;
    private LinearLayout elementsLayout1;
    private LinearLayout elementsLayout2;

    private PetModel model;
    private PetAdapter adapter;
    private boolean isServiceStarted;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        this.isServiceStarted = false;

        // create storage and populate
        this.model  = PetModel.build();
        this.populate();

        // retrieve
        this.petsListView =  findViewById(R.id.pets);
        this.button =  findViewById(R.id.switchButton);
        this.numEventsTv =  findViewById(R.id.numEvents);
        this.progressBar =  findViewById(R.id.progressBar);
        this.elementsLayout1 =  findViewById(R.id.textLayout1);
        this.elementsLayout2 =  findViewById(R.id.textLayout2);
        this.numProcessedTv =  findViewById(R.id.numProcessed);

        // set adapter
        this.adapter = new PetAdapter(this, (ArrayList<Pet>) this.model.getPets());
        this.petsListView.setAdapter(this.adapter);

        // retrieve the instance status
        if (state != null) {
            Parcelable status = state.getParcelable(PETS_LIST_STATUS);
            this.petsListView.onRestoreInstanceState(status);
        }

    }

    @Override
    protected void onActivityResult(int code, int result, Intent data) {
        super.onActivityResult(code, result, data);
        this.updateUi();
    }

    public void switchService(View view){

        if(!this.isServiceStarted){
            startService(new Intent(MainActivity.this, PetService.class));
        }else{
            stopService(new Intent(MainActivity.this, PetService.class));
        }

        this.button.setText(!this.isServiceStarted ? R.string.stop : R.string.start);

        this.isServiceStarted = ! this.isServiceStarted;

    }

    private void populate(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                MainActivity.this.model.load(Constants.DEFAULT_URL);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.progressBar.setVisibility(View.GONE);
                        MainActivity.this.button.setVisibility(View.VISIBLE);
                        MainActivity.this.petsListView.setVisibility(View.VISIBLE);
                        MainActivity.this.elementsLayout1.setVisibility(View.VISIBLE);
                        MainActivity.this.elementsLayout2.setVisibility(View.VISIBLE);

                        MainActivity.this.adapter = new PetAdapter(MainActivity.this, (ArrayList<Pet>) MainActivity.this.model.getPets());
                        MainActivity.this.petsListView.setAdapter(MainActivity.this.adapter);
                    }
                });
            }
        }).start();
    }

    private void updateUi(){
        this.numEventsTv.setText(this.model.getEvents());
        this.numProcessedTv.setText(this.model.getProcessed());
        this.adapter.notifyDataSetChanged();
    }


}