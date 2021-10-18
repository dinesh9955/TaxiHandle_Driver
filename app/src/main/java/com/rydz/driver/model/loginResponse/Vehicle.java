package com.rydz.driver.model.loginResponse;

public class Vehicle {
    private String _id;
    private String name;
    private float fareRate;
    private float baseFare;
    private float persons;
    private float __v;
    private String image;

    private int type;
    // Getter Methods

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public float getFareRate() {
        return fareRate;
    }

    public float getBaseFare() {
        return baseFare;
    }

    public float getPersons() {
        return persons;
    }

    public float get__v() {
        return __v;
    }

    public String getImage() {
        return image;
    }

    // Setter Methods

    public void set_id( String _id ) {
        this._id = _id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setFareRate( float fareRate ) {
        this.fareRate = fareRate;
    }

    public void setBaseFare( float baseFare ) {
        this.baseFare = baseFare;
    }

    public void setPersons( float persons ) {
        this.persons = persons;
    }

    public void set__v( float __v ) {
        this.__v = __v;
    }

    public void setImage( String image ) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
