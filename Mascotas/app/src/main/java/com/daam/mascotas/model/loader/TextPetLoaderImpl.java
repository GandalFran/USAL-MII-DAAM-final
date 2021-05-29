package com.daam.mascotas.model.loader;

import com.daam.mascotas.bean.Pet;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

class TextPetModel {

    private final static String ID_KEY = "ID";
    private final static String NAME_KEY = "nMASCOTA";
    private final static String OWNER_KEY = "nPROPIETARIO";
    private final static String DATE_KEY = "ADOPCION";
    private final static String TYPE_KEY = "TIPO";
    private final static String VACCINATED_KEY = "VACUNADO";

    public String id;
    public String name;
    public String owner;
    public String date;
    public String type;
    public String vaccinated;

    public TextPetModel(String line){
        String headerLine = "ID\tnMASCOTA\tnPROPIETARIO\tADOPCION\tTIPO\tVACUNADO";

        List<String> headers = Arrays.asList(headerLine.split("\t"));
        List<String> values = Arrays.asList(line.split("\t|\n"));

        this.id = values.get(headers.indexOf(ID_KEY));
        this.name = values.get(headers.indexOf(NAME_KEY));
        this.owner = values.get(headers.indexOf(OWNER_KEY));
        this.date = values.get(headers.indexOf(DATE_KEY));
        this.type = values.get(headers.indexOf(TYPE_KEY));
        this.vaccinated = values.get(headers.indexOf(VACCINATED_KEY));
    }

    public TextPetModel(String headerLine, String line){
        List<String> headers = Arrays.asList(headerLine.split("\t"));
        List<String> values = Arrays.asList(line.split("\t|\n"));

        this.id = values.get(headers.indexOf(ID_KEY));
        this.name = values.get(headers.indexOf(NAME_KEY));
        this.owner = values.get(headers.indexOf(OWNER_KEY));
        this.date = values.get(headers.indexOf(DATE_KEY));
        this.type = values.get(headers.indexOf(TYPE_KEY));
        this.vaccinated = values.get(headers.indexOf(VACCINATED_KEY));
    }

    private Date getDate(){
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(this.date);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean getVaccination(){
        return this.vaccinated.equals("Si");
    }

    public Pet toPet(){
        return new Pet(this.id,this.getDate(),this.name,this.owner,this.type,this.getVaccination());
    }
}


public class TextPetLoaderImpl implements PetLoader{

    private String requestText(String uri) throws IOException {
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
        String text;
        try {
            text = this.requestText(url);
        } catch (IOException e) {
            throw new LoadException(e.getMessage());
        }
        TextPetModel rawP = new TextPetModel(text);
        return rawP.toPet();
    }

    @Override
    public List<Pet> load(String url) throws LoadException {

        String text;
        try {
            text = this.requestText(url);
        } catch (IOException e) {
            throw new LoadException(e.getMessage());
        }

        // split different if is a CSV format or a single format
        List<String> lines = null;
        if(text.contains("\n\n")){
           lines = Arrays.asList(text.split("\n\n"));
        }else{
            lines = Arrays.asList(text.split("\n"));
        }

        String headerLine = lines.get(0);
        lines = lines.subList(1,lines.size());

        List<Pet> pets = new ArrayList<>();
        for(String line: lines){
            TextPetModel rawP = new TextPetModel(headerLine, line);
            pets.add(rawP.toPet());
        }

        return pets;
    }
}
