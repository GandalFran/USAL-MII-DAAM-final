package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.scraper.PetScraper;

import java.io.IOException;
import java.util.List;

public class ScraperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scraper);
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
        int minId = 0, maxId = 0;
        PetScraper petScraper = new PetScraper();
        try {
            List<Pet> petList = petScraper.getPetList(minId, maxId);
        } catch (IOException e) {
            Toast.makeText(this.getApplicationContext(),"An error occurred during request", Toast.LENGTH_LONG).show();
        }

    }
}