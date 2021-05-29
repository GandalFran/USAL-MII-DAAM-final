package com.daam.mascotas.model.loader;

import com.daam.mascotas.bean.Pet;

import java.util.List;

public interface PetLoader {
    Pet loadOne(String url) throws LoadException;
    List<Pet> load(String url) throws LoadException;
}
