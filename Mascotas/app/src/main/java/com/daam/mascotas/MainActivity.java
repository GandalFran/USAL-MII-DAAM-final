package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PETS_LIST_STATUS = "PETS_LIST_STATUS";

    private Button button;
    private ListView petsListView;
    private ProgressBar progressBar;
    private LinearLayout elementsLayout1;
    private LinearLayout elementsLayout2;

    private PetModel model;
    private PetAdapter adapter;
    private boolean isServiceStarted;

    public MainActivity(){
        this.model  = new PetModel(this);
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        this.isServiceStarted = false;

        // retrieve
        this.petsListView =  findViewById(R.id.pets);
        this.button =  findViewById(R.id.switchButton);
        this.progressBar =  findViewById(R.id.progressBar);
        this.elementsLayout1 =  findViewById(R.id.textLayout1);
        this.elementsLayout2 =  findViewById(R.id.textLayout2);

        // set adapter
        this.adapter = new PetAdapter(this, (ArrayList<Pet>) this.model.getPets());
        this.petsListView.setAdapter(this.adapter);

        // retrieve the instance status
        if (state != null) {
            Parcelable status = state.getParcelable(PETS_LIST_STATUS);
            this.petsListView.onRestoreInstanceState(status);
        }

        // create storage and populate
        this.model  = new PetModel(this);
        this.populate();
    }

    public void switchService(View view){
        this.button.setText(this.isServiceStarted ? R.string.stop : R.string.start);

        if(this.isServiceStarted){

        }else{

        }
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
}