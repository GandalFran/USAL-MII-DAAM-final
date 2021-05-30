package com.daam.mascotas.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daam.mascotas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends ArrayAdapter<Pet> {

    private final static String DATE_FORMAT = "dd/MM/yyyy";

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

        // if view is null, retrieve corresponding row
        if (rowView == null) {
            rowView = LayoutInflater.from(this.getContext()).inflate(R.layout.elem_pet, parent, false);
        }

        // retrieve
        TextView dateTv = rowView.findViewById(R.id.date);
        TextView petaNameTv = rowView.findViewById(R.id.petName);
        ImageView pictureIv =  rowView.findViewById(R.id.picture);
        TextView ownerNameTv = rowView.findViewById(R.id.ownerName);
        TextView vaccinatedNoTv = rowView.findViewById(R.id.vaccinatedNo);
        TextView vaccinatedYesTv = rowView.findViewById(R.id.vaccinatedYes);

        // populate
        petaNameTv.setText(p.getName());
        ownerNameTv.setText(p.getOwner());
        pictureIv.setImageResource(getImage(p));
        dateTv.setText(new SimpleDateFormat(DATE_FORMAT).format(p.getDate()));
        vaccinatedNoTv.setVisibility(p.isVaccinated() ? View.GONE : View.VISIBLE);
        vaccinatedYesTv.setVisibility(p.isVaccinated() ? View.VISIBLE : View.GONE);

        return rowView;
    }

    private int getImage(Pet p){
        String petType = p.getType().toLowerCase();
        switch(petType){
            case "gato":
                return  R.drawable.cat;
            case "pájaro":
                return  R.drawable.bird;
            case "perro":
                return  R.drawable.dog;
            case "otro":
            default:
                return  R.drawable.other;
        }
    }
}
