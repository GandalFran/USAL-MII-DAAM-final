package com.daam.mascotas.model.loader;

import com.daam.mascotas.bean.Pet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class XMLPetModel {

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

    public XMLPetModel(Element element){

        NodeList nl = element.getElementsByTagName(ID_KEY);
        Element e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.id = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(NAME_KEY);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.name = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(OWNER_KEY);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.owner = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(DATE_KEY);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.date = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(TYPE_KEY);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.type = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(VACCINATED_KEY);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        this.vaccinated = nl.item(0).getNodeValue();
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

public class XMLPetLoaderImpl implements PetLoader{

    private final static String PET_LIST_KEY = "Mascotas";

    public NodeList requestXml(String uri) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new URL(uri).openStream()));
        doc.getDocumentElement().normalize();
        return doc.getElementsByTagName(PET_LIST_KEY);
    }

    @Override
    public Pet loadOne(String url) throws LoadException {
        return this.load(url).get(0);
    }

    @Override
    public List<Pet> load(String url) throws LoadException {

        NodeList nl;
        try {
            nl = this.requestXml(url);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new LoadException(e.getMessage());
        }

        List<Pet> pets = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            XMLPetModel rawP = new XMLPetModel(e);
            pets.add(rawP.toPet());
        }

        return pets;
    }
}
