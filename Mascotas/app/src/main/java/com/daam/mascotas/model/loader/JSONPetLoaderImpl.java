package com.daam.mascotas.model.loader;

import com.daam.mascotas.bean.Pet;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


class JSONPetModel {
    public String ID;
    public String nMASCOTA;
    public String nPROPIETARIO;
    public String ADOPCION;
    public String TIPO;
    public String VACUNADO;

    private Date getDate(){
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(this.ADOPCION);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean getVaccination(){
        return this.VACUNADO.equals("Si");
    }

    public Pet toPet(){
        return new Pet(this.ID,this.getDate(),this.nMASCOTA,this.nPROPIETARIO,this.TIPO,this.getVaccination());
    }
}

public class JSONPetLoaderImpl implements PetLoader{

    private String requestJson(String uri) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    @Override
    public Pet loadOne(String url) throws LoadException {
        return this.load(url).get(0);
    }

    @Override
    public List<Pet> load(String url) throws LoadException {
        List<Pet> pets;
        try {
            String json = this.requestJson(url);
            Gson gson = new Gson();
            JSONPetModel [] rawPets = gson.fromJson(json, JSONPetModel[].class);
            pets = new ArrayList<>();
            for(JSONPetModel rawP: rawPets){
                pets.add(rawP.toPet());
            }
        } catch (IOException e) {
            throw new LoadException(e.getMessage());
        }
        return pets;
    }
}
