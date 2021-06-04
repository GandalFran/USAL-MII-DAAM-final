package com.daam.mascotas.model.scraper;

import android.util.Log;

import com.daam.mascotas.Constants;
import com.daam.mascotas.bean.Pet;
import com.daam.mascotas.service.PetService;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    public List<Pet> getPetList(int minId, int maxId) throws IOException, InterruptedException {
        List<Pet> pets = new ArrayList<>();

        // thread created to allow network requests
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // make post to get HTML
                String html = null;
                try {
                    html = PetScraper.this.requestPetList(Constants.PET_LIST_URL, Constants.PET_LIST_USER, minId, maxId);
                } catch (IOException e) {
                    return;
                }

                // parse with Jsoup
                Document doc = Jsoup.parse(html);
                Element link = doc.select("a").first();
                String url = link.attr("href");

                // parse with json
                String jsonHtml = null;
                try {
                    jsonHtml = PetScraper.this.requestJson(url);
                } catch (IOException e) {
                    return;
                }

                // retrieve json from html
                doc = Jsoup.parse(jsonHtml);
                String json  = Jsoup.clean(doc.select("body").first().text(), Whitelist.basic()).replace("&quot;","\"");

                // parse json
                Gson gson = new Gson();
                JSONPetModel[] rawPets = gson.fromJson(json, JSONPetModel[].class);
                for(JSONPetModel rawP: rawPets){
                    pets.add(rawP.toPet());
                }
            }
        });

        t.start();
        t.join();

        return pets;

    }

    public void registerPet(Pet p) throws IOException {
        String response = this.requestRegisterPet(Constants.PET_ADD_URL, Constants.PET_LIST_USER,p);
    }

    private String requestPetList(String url, String user, int minId, int maxId) throws IOException {

        String params = String.format("usuario=%s&limit_inf=%s&limit_sup=%s&data_format=%s",user, Integer.valueOf(minId).toString(), Integer.valueOf(maxId).toString(),"JSON");
        byte[] postData = params.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty( "charset", "utf-8");
        connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
            wr.write( postData );
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

        String params = String.format("usuario=%s&nMascota=%s&nPropietario=%s&Fecha_adop=%s&Tipo_mascota=%s&comentario=%s"
            ,user
            ,p.getName()
            , p.getOwner()
            , new SimpleDateFormat("yyyy-MM-dd").format(p.getDate())
            , p.getType()
            , ""
        );
        if(p.isVaccinated()) {
            params += "&vacuna=Vacunado";
        }
        byte[] postData = params.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty( "charset", "utf-8");
        connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
            wr.write( postData );
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
