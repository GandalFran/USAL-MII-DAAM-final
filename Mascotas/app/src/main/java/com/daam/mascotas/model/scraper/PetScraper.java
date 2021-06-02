package com.daam.mascotas.model.scraper;

import com.daam.mascotas.Constants;
import com.daam.mascotas.bean.Pet;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class PetScraper {

    public List<Pet> getPetList(int minId, int maxId) throws IOException {

        // make post to get HTML
        String html = this.requestPetList(Constants.PET_LIST_URL, Constants.PET_LIST_USER, minId, maxId);

        // parse with Jsoup
        Document doc = Jsoup.parse(html);
        Element link = doc.select("a").first();
        String url = link.attr("href");

        // parse with json
        String json = this.requestJson(url);
        Gson gson = new Gson();
        JSONPetModel[] rawPets = gson.fromJson(json, JSONPetModel[].class);
        List<Pet> pets = new ArrayList<>();
        for(JSONPetModel rawP: rawPets){
            pets.add(rawP.toPet());
        }

        return pets;
    }

    public void registerPet(Pet p) throws IOException {
        String response = this.requestRegisterPet(Constants.PET_ADD_URL, Constants.PET_LIST_USER,p);
    }

    private String requestPetList(String url, String user, int minId, int maxId) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("usuario", user);
        connection.setRequestProperty("limit_inf", Integer.valueOf(minId).toString());
        connection.setRequestProperty("limit_sup", Integer.valueOf(maxId).toString());
        connection.setRequestProperty("data_format", "JSON");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

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

    private String requestRegisterPet(String url, String user, Pet p) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("usuario", user);
        connection.setRequestProperty("nMascota", p.getName());
        connection.setRequestProperty("nPropietario", p.getOwner());
        connection.setRequestProperty("Fecha_adop", new SimpleDateFormat("yyyy-MM-dd").format(p.getDate()));
        connection.setRequestProperty("Tipo_mascota", p.getType());
        connection.setRequestProperty("comentario: ", "");
        if(p.isVaccinated()) {
            connection.setRequestProperty("vacuna", "Vacunado");
        }
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }
}
