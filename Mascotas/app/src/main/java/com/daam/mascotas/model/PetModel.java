package com.daam.mascotas.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.bean.PetAdapter;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PetModel implements Parcelable {

    private int events;
    private int processed;
    private PetAdapter adapter;
    private final List<Pet> pets;
    private final List<Pet> pendingPets;

    private static PetModel instance = null;

    public static PetModel build(){
        if(instance == null){
            PetModel.instance = new PetModel();
        }
        return instance;
    }

    private PetModel(){
        this.events = 0;
        this.processed = 0;
        this.pets = new ArrayList<>();
        this.pendingPets = new ArrayList<>();
    }

    protected PetModel(Parcel in) {
        this.pets = in.createTypedArrayList(Pet.CREATOR);
        this.pendingPets = in.createTypedArrayList(Pet.CREATOR);
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
        d.writeTypedList(this.pendingPets);
    }

    public void addPending(Pet p){
        this.pendingPets.add(p);
        this.events++;
    }

    public void commitPending(){
        this.processed += this.pendingPets.size();
        this.adapter.addAll(this.pendingPets);
        this.pendingPets.clear();
        this.adapter.sort(Pet::compareTo);
    }

    public void dismissPending(){
        this.pendingPets.clear();
    }

    public void load(String url){

        PetLoader loader;
        if(url.toLowerCase().contains("json")){
            loader = new JSONPetLoaderImpl();
        }else if(url.toLowerCase().contains("xml")){
            loader = new XMLPetLoaderImpl();
        }else{
            loader = new TextPetLoaderImpl();
        }

        try {
            List<Pet> loadedPets = loader.load(url);
            this.pets.addAll(loadedPets);
        }catch (Exception ignored){
        }
    }

    public List<Pet> getPets() {
        return pets;
    }

    public int getEvents() {
        return events;
    }

    public int getProcessed() {
        return processed;
    }

    public List<Pet> getPending() {
        return this.pendingPets;
    }
}
