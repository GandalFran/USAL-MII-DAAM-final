package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.bean.PetAdapter;
import com.daam.mascotas.model.scraper.PetScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScraperActivity extends AppCompatActivity {

    private List<Pet> pets;
    private PetAdapter adapter;

    private TextView minTv;
    private TextView maxTv;
    private ListView petsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraper);

        this.pets = new ArrayList<>();

        this.petsListView =  findViewById(R.id.pets);
        this.minTv =  findViewById(R.id.minEditText);
        this.maxTv =  findViewById(R.id.maxEditText);

        this.adapter = new PetAdapter(this, (ArrayList<Pet>) this.pets);
        this.petsListView.setAdapter(this.adapter);
    }

    public void add(View view){

        Pet p = null;
        PetScraper petScraper = new PetScraper();
        try {
            petScraper.registerPet(p);
        } catch (IOException e) {
            Toast.makeText(this.getApplicationContext(),"An error occurred during request", Toast.LENGTH_LONG).show();
        }
    }

    public void load(View view){

        int minId, maxId;
        try {
             minId = Integer.parseInt(this.minTv.getText().toString());
             maxId = Integer.parseInt(this.maxTv.getText().toString());
        }catch (Exception e){
            Toast.makeText(this,"Bad parameteres provided", Toast.LENGTH_LONG).show();
            return;
        }

        if(minId >= maxId){
            Toast.makeText(this,"Minimun must be lower than maximum", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            PetScraper petScraper = new PetScraper();
            List<Pet>newPets = petScraper.getPetList(minId, maxId);
            this.adapter.clear();
            this.adapter.addAll(newPets);
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),"An error occurred during request", Toast.LENGTH_LONG).show();
        }
    }

    public void exit(View view) {
        this.finish();
    }
}