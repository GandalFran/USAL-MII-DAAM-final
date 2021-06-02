package com.daam.mascotas.bean;


import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Pet implements Parcelable, Serializable, Comparable<Pet> {

    public final static String PET_ID_KEY = "PET_ID_KEY";
    public final static String PET_DATE_KEY = "PET_DATE_KEY";
    public final static String PET_NAME_KEY = "PET_NAME_KEY";
    public final static String PET_OWNER_KEY = "PET_OWNER_KEY";
    public final static String PET_TYPE_KEY = "PET_TYPE_KEY";
    public final static String PET_VACCINATED_KEY = "PET_VACCINATED_KEY";

    private final static String DATE_SERIALIZATION_FORMAT = "yyyy-MM-dd";

    private int order;
    private String id;
    private Date date;
    private String name;
    private String owner;
    private String type;
    private boolean vaccinated;

    public Pet(String id, Date date, String name, String owner, String type, boolean vaccinated) {
        this.order = 0;
        this.id = id;
        this.date = date;
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.vaccinated = vaccinated;
    }

    protected Pet(Parcel in) {
        this.order = 0;
        this.id = in.readString();
        this.date = (Date) in.readSerializable();
        this.name = in.readString();
        this.owner = in.readString();
        this.type = in.readString();
        this.vaccinated = (in.readInt() == 1);
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel d, int flags) {
        d.writeString(this.id);
        d.writeSerializable(this.date);
        d.writeString(this.name);
        d.writeString(this.owner);
        d.writeString(this.type);
        d.writeInt(this.vaccinated ? 1 : 0);
    }

    public ContentValues buildContentValues(){
        ContentValues values = new ContentValues();
        values.put(Pet.PET_ID_KEY, this.id);
        values.put(Pet.PET_NAME_KEY, this.name);
        values.put(Pet.PET_OWNER_KEY, this.owner);
        values.put(Pet.PET_TYPE_KEY, this.type);
        values.put(Pet.PET_DATE_KEY, new SimpleDateFormat(DATE_SERIALIZATION_FORMAT).format(this.date));
        values.put(Pet.PET_VACCINATED_KEY, this.vaccinated);
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return vaccinated == pet.vaccinated &&
                id.equals(pet.id) &&
                Objects.equals(date, pet.date) &&
                Objects.equals(name, pet.name) &&
                Objects.equals(owner, pet.owner) &&
                Objects.equals(type, pet.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, name, owner, type, vaccinated);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", type='" + type + '\'' +
                ", vaccinated=" + vaccinated +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Pet o) {
        return Integer.compare(this.order, o.order);
    }
}