package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
    }

    public void normal(View view){
        Intent intent = new Intent(this, PetListActivity.class);
        startActivity(intent);
    }

    public void addPet(View view){
        Intent intent = new Intent(this, PetAddScraperActivity.class);
        startActivity(intent);
    }

    public void selectPets(View view){
        Intent intent = new Intent(this, PetListScraperActivity.class);
        startActivity(intent);
    }
}