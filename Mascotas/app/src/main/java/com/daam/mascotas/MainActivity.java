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


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
    }

    public void normal(View view){
        Intent intent = new Intent(this, PetListActivity.class);
        startActivityForResult(intent, 0);
    }

    public void addPet(View view){
        Intent intent = new Intent(this, ScraperActivity.class);
        startActivityForResult(intent, 0);
    }

    public void selectPets(View view){
        Intent intent = new Intent(this, ScraperActivity.class);
        startActivityForResult(intent, 0);
    }
}