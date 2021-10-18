package com.rydz.driver.model.loginResponse;

import java.util.ArrayList;

public class Driver {
    private String _id;
    private String updatedAt;
    private String createdAt;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private float dob;
    private String gender;
    private String bio;
    Vehicle vehicleType;
    private String vehicleNumber;
    private String vehicleColor;
    private String model;
    private String manufacturerName;
    private String deviceId;
    private String countryCode;
    private String deviceType;
    private float __v;
    private String authToken;
    ArrayList<Object> coordinates = new ArrayList<Object>();
    private float latitude;
    private float longitude;
    private int isAvailable;
    private float isVerified;
    private String profilePic;
    private String id;


    // Getter Methods

    public String get_id() {
        return _id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public float getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public Vehicle getVehicleType() {
        return vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public float get__v() {
        return __v;
    }

    public String getAuthToken() {
        return authToken;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public float getIsVerified() {
        return isVerified;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getId() {
        return id;
    }

    // Setter Methods

    public void set_id( String _id ) {
        this._id = _id;
    }

    public void setUpdatedAt( String updatedAt ) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public void setDob( float dob ) {
        this.dob = dob;
    }

    public void setGender( String gender ) {
        this.gender = gender;
    }

    public void setBio( String bio ) {
        this.bio = bio;
    }

    public void setVehicleType( Vehicle vehicleTypeObject ) {
        this.vehicleType = vehicleTypeObject;
    }

    public void setVehicleNumber( String vehicleNumber ) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setVehicleColor( String vehicleColor ) {
        this.vehicleColor = vehicleColor;
    }

    public void setModel( String model ) {
        this.model = model;
    }

    public void setManufacturerName( String manufacturerName ) {
        this.manufacturerName = manufacturerName;
    }

    public void setDeviceId( String deviceId ) {
        this.deviceId = deviceId;
    }

    public void setDeviceType( String deviceType ) {
        this.deviceType = deviceType;
    }

    public void set__v( float __v ) {
        this.__v = __v;
    }

    public void setAuthToken( String authToken ) {
        this.authToken = authToken;
    }

    public void setLatitude( float latitude ) {
        this.latitude = latitude;
    }

    public void setLongitude( float longitude ) {
        this.longitude = longitude;
    }

    public void setIsAvailable( int isAvailable ) {
        this.isAvailable = isAvailable;
    }

    public void setIsVerified( float isVerified ) {
        this.isVerified = isVerified;
    }

    public void setProfilePic( String profilePic ) {
        this.profilePic = profilePic;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public void setContryCode(String contryCode) {
        this.countryCode = contryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCoordinates(ArrayList<Object> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Object> getCoordinates() {
        return coordinates;
    }
}

