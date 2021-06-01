package com.daam.mascotas.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.model.loader.JSONPetLoaderImpl;
import com.daam.mascotas.model.loader.PetLoader;
import com.daam.mascotas.model.loader.TextPetLoaderImpl;
import com.daam.mascotas.model.loader.XMLPetLoaderImpl;

import java.util.ArrayList;
import java.util.List;

public class PetModel implements Parcelable {

    private int events;
    private int processed;
    private final List<Pet> pets;
    
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

    public void update(List<Pet> newPets, boolean updatePetList){
        for(Pet p: newPets){
            if(!this.exists(p)){
                if(updatePetList) {
                    this.processed++;
                    this.pets.add(0, p);
                }
                this.events++;
            }
        }
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

    public boolean exists(Pet p) {
        for(Pet pet: this.pets){
            if(pet.getId().equals(p.getId())){
                return true;
            }
        }
        return false;
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

}
