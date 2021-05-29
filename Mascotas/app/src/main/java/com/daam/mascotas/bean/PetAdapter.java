package com.daam.mascotas.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends ArrayAdapter<Pet> {

    private final List<Pet> pets;

    public PetAdapter(Context context, ArrayList<Pet> pets) {
        super(context, 0, pets);
        this.pets = pets;
    }

    @Override
    public Pet getItem(int position){
        return this.pets.get(position);
    }

    @Override
    public int getCount(){
        return this.pets.size();
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        Pet p = this.getItem(position);

        /*
        // if view is null, retrieve corresponding row
        if (rowView == null) {
            rowView = LayoutInflater.from(this.getContext()).inflate(R.layout.listview_user, parent, false);
        }

        // retrieve
        TextView name = rowView.findViewById(R.id.name);
        TextView age = rowView.findViewById(R.id.age);
        TextView date = rowView.findViewById(R.id.date);
        TextView phone = rowView.findViewById(R.id.phone);
        TextView englishLevel = rowView.findViewById(R.id.englishLevel);
        TextView drivingLicense = rowView.findViewById(R.id.drivingLicense);

        // populate
        name.setText(String.format("%s %s", p.getName(), p.getSurname()));
        age.setText(p.getAge());
        date.setText(Utils.getDateString(p.getDate()));
        phone.setText(p.getPhone());
        englishLevel.setText(rowView.getResources().getString(R.string.english) + ' ' + this.getEnglishLevelString(rowView,p.getEnglishLevel()) );
        drivingLicense.setText(rowView.getResources().getString(R.string.license) + ' ' + (p.isDrivingLicense() ? rowView.getResources().getString(R.string.yes):rowView.getResources().getString(R.string.no)));
        */

        return rowView;
    }
}
