package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.net.NetworkInterface;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected() || !networkInfo.getState().toString().equalsIgnoreCase("CONNECTED")){
            Toast.makeText(this.getApplicationContext(),"There is no internet connection",Toast.LENGTH_LONG);
        }

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