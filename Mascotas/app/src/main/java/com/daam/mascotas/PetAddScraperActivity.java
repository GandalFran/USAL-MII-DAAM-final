package com.daam.mascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.bean.PetAdapter;
import com.daam.mascotas.model.scraper.PetScraper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PetAddScraperActivity extends AppCompatActivity {

    private final static String [] PET_TYPES = {"Perro","Gato","Pájaro","Otro"};
    private final static boolean [] VACCINATED_TYPES = {false, true};

    private List<Pet> pets;
    private PetAdapter adapter;

    private TextView dateInput;
    private EditText nameInput;
    private EditText ownerInput;
    private ListView petsListView;
    private TextView commentInput;
    private RadioGroup petTypeInput;
    private RadioGroup vaccinatedInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_add_scraper);

        this.dateInput =  findViewById(R.id.dateInput);
        this.petsListView =  findViewById(R.id.pets);
        this.nameInput =  findViewById(R.id.petName);
        this.petsListView =  findViewById(R.id.pets);
        this.ownerInput =  findViewById(R.id.ownerName);
        this.petTypeInput =  findViewById(R.id.petType);
        this.commentInput =  findViewById(R.id.comment);
        this.vaccinatedInput =  findViewById(R.id.vaccinated);

        this.dateInput.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        this.pets = new ArrayList<>();
        this.adapter = new PetAdapter(this, (ArrayList<Pet>) this.pets);
        this.petsListView.setAdapter(this.adapter);
    }

    public void add(View view){

        String name = this.nameInput.getText().toString();
        String owner = this.ownerInput.getText().toString();
        String dateString = this.dateInput.getText().toString();
        String comment = this.commentInput.getText().toString();
        int petTypIndex = this.petTypeInput.indexOfChild(this.petTypeInput.findViewById(this.petTypeInput.getCheckedRadioButtonId()));
        int vaccinatedIndex = this.vaccinatedInput.indexOfChild(this.vaccinatedInput.findViewById(this.vaccinatedInput.getCheckedRadioButtonId()));

        String petType = PET_TYPES[petTypIndex];
        boolean vaccinated = VACCINATED_TYPES[vaccinatedIndex];
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        Pet p = new Pet("",date,name,owner,petType,vaccinated);

        Pet receivedPet = null;
        try {
            PetScraper petScraper = new PetScraper();
            receivedPet = petScraper.registerPet(p,comment);
            this.adapter.clear();
            this.adapter.add(receivedPet);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),"An error occurred during request", Toast.LENGTH_LONG).show();
        }
    }

    public void exit(View view) {
        this.finish();
    }

    public void changeDate(View view) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = String.format("%d/%d/%d",day,month+1,year);
                PetAddScraperActivity.this.dateInput.setText(date);
            }
        }, c.get(Calendar.YEAR),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.MONTH));
        dpd.show();
    }
}