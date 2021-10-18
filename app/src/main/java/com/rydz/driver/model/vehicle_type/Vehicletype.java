package com.rydz.driver.model.vehicle_type;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Vehicletype implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fareRate")
    @Expose
    private Double fareRate;
    @SerializedName("baseFare")
    @Expose
    private Double baseFare;
    @SerializedName("persons")
    @Expose
    private Long persons;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("fareRateAfter")
    @Expose
    private Double fareRateAfter;
    @SerializedName("fareChangekm")
    @Expose
    private Double fareChangekm;
    @SerializedName("activeImage")
    @Expose
    private String activeImage;
    @SerializedName("image")
    @Expose
    private String image;
    public final static Parcelable.Creator<Vehicletype> CREATOR = new Creator<Vehicletype>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Vehicletype createFromParcel(Parcel in) {
            return new Vehicletype(in);
        }

        public Vehicletype[] newArray(int size) {
            return (new Vehicletype[size]);
        }

    }
            ;

    protected Vehicletype(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.fareRate = ((Double) in.readValue((Double.class.getClassLoader())));
        this.baseFare = ((Double) in.readValue((Double.class.getClassLoader())));
        this.persons = ((Long) in.readValue((Long.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        this.fareRateAfter = ((Double) in.readValue((Double.class.getClassLoader())));
        this.fareChangekm = ((Double) in.readValue((Double.class.getClassLoader())));
        this.activeImage = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Vehicletype() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFareRate() {
        return fareRate;
    }

    public void setFareRate(Double fareRate) {
        this.fareRate = fareRate;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Long getPersons() {
        return persons;
    }

    public void setPersons(Long persons) {
        this.persons = persons;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Double getFareRateAfter() {
        return fareRateAfter;
    }

    public void setFareRateAfter(Double fareRateAfter) {
        this.fareRateAfter = fareRateAfter;
    }

    public Double getFareChangekm() {
        return fareChangekm;
    }

    public void setFareChangekm(Double fareChangekm) {
        this.fareChangekm = fareChangekm;
    }

    public String getActiveImage() {
        return activeImage;
    }

    public void setActiveImage(String activeImage) {
        this.activeImage = activeImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("fareRate", fareRate).append("baseFare", baseFare).append("persons", persons).append("v", v).append("fareRateAfter", fareRateAfter).append("fareChangekm", fareChangekm).append("activeImage", activeImage).append("image", image).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(fareRate);
        dest.writeValue(baseFare);
        dest.writeValue(persons);
        dest.writeValue(v);
        dest.writeValue(fareRateAfter);
        dest.writeValue(fareChangekm);
        dest.writeValue(activeImage);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}