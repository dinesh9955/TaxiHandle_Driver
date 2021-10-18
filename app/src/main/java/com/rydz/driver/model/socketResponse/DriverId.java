package com.rydz.driver.model.socketResponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DriverId implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("dob")
    @Expose
    private Long dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicleColor")
    @Expose
    private String vehicleColor;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("manufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("isAvailable")
    @Expose
    private Long isAvailable;
    @SerializedName("isVerified")
    @Expose
    private Long isVerified;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("ratings")
    @Expose
    private Object ratings;
    public final static Parcelable.Creator<DriverId> CREATOR = new Creator<DriverId>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DriverId createFromParcel(Parcel in) {
            return new DriverId(in);
        }

        public DriverId[] newArray(int size) {
            return (new DriverId[size]);
        }

    }
            ;

    protected DriverId(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((Long) in.readValue((Long.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.bio = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleType = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicleColor = ((String) in.readValue((String.class.getClassLoader())));
        this.model = ((String) in.readValue((String.class.getClassLoader())));
        this.manufacturerName = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceType = ((String) in.readValue((String.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.coordinates, (java.lang.Double.class.getClassLoader()));
        this.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
        this.isAvailable = ((Long) in.readValue((Long.class.getClassLoader())));
        this.isVerified = ((Long) in.readValue((Long.class.getClassLoader())));
        this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        this.ratings = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public DriverId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Long isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Long getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Long isVerified) {
        this.isVerified = isVerified;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Object getRatings() {
        return ratings;
    }

    public void setRatings(Object ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("updatedAt", updatedAt).append("createdAt", createdAt).append("firstName", firstName).append("lastName", lastName).append("email", email).append("countryCode", contryCode).append("phone", phone).append("dob", dob).append("gender", gender).append("bio", bio).append("vehicleType", vehicleType).append("vehicleNumber", vehicleNumber).append("vehicleColor", vehicleColor).append("model", model).append("manufacturerName", manufacturerName).append("deviceId", deviceId).append("deviceType", deviceType).append("v", v).append("coordinates", coordinates).append("latitude", latitude).append("longitude", longitude).append("isAvailable", isAvailable).append("isVerified", isVerified).append("profilePic", profilePic).append("ratings", ratings).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(updatedAt);
        dest.writeValue(createdAt);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(email);
        dest.writeValue(contryCode);
        dest.writeValue(phone);
        dest.writeValue(dob);
        dest.writeValue(gender);
        dest.writeValue(bio);
        dest.writeValue(vehicleType);
        dest.writeValue(vehicleNumber);
        dest.writeValue(vehicleColor);
        dest.writeValue(model);
        dest.writeValue(manufacturerName);
        dest.writeValue(deviceId);
        dest.writeValue(deviceType);
        dest.writeValue(v);
        dest.writeList(coordinates);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(isAvailable);
        dest.writeValue(isVerified);
        dest.writeValue(profilePic);
        dest.writeValue(ratings);
    }

    public int describeContents() {
        return 0;
    }

}
