package com.daam.mascotas.model;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.contentprovider.PetContentProvider;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class PetModel implements Parcelable {

    private List<Pet> pets;
    private PetContentProvider provider;

    public PetModel(Context context){
        this.pets = new ArrayList<>();
        this.provider = new PetContentProvider(context, this.pets);
    }

    protected PetModel(Parcel in) {
        this.pets = in.createTypedArrayList(Pet.CREATOR);
    }

    public static final Creator<PetModel> CREATOR = new Creator<PetModel>() {
        @Override
        public PetModel createFromParcel(Parcel in) {
            return new PetModel(in);
        }

        @Override
        public PetModel[] newArray(int size) {
            return new PetModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel d, int flags) {
        d.writeTypedList(this.pets);
    }

    public void add(Pet p){
        this.provider.insert(PetContentProvider.CONTENT_URI, p.buildContentValues());
        this.pets.add(p);
    }

    public void addAll(List<Pet> newPets){
        for(Pet p: newPets){
            this.provider.insert(PetContentProvider.CONTENT_URI, p.buildContentValues());
        }
        this.pets.addAll(newPets);
    }

    public void update(Pet p, int position){
        this.provider.update(PetContentProvider.CONTENT_URI, p.buildContentValues(), String.format("id = %s", p.getId()), null);
        this.pets.set(position, p);
    }

    public void remove(Pet p){
        this.provider.delete(PetContentProvider.CONTENT_URI, String.format("id = %s", p.getId()), null);
        this.pets.remove(p);
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void load(String url){

        PetLoader loader;
        if(url.toLowerCase().contains("txt")){
            loader = new TextPetLoaderImpl();
        }else if(url.toLowerCase().contains("json")){
            loader = new JSONPetLoaderImpl();
        }else{
            loader = new XMLPetLoaderImpl();
        }

        try {
            List<Pet> loadedPets = loader.load(url);
            this.addAll(loadedPets);
        }catch (Exception ignored){
        }
    }

}
