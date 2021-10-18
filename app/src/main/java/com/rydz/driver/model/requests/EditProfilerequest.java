package com.rydz.driver.model.requests;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EditProfilerequest implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    public final static Parcelable.Creator<EditProfilerequest> CREATOR = new Creator<EditProfilerequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EditProfilerequest createFromParcel(Parcel in) {
            return new EditProfilerequest(in);
        }

        public EditProfilerequest[] newArray(int size) {
            return (new EditProfilerequest[size]);
        }

    }
            ;

    protected EditProfilerequest(Parcel in) {
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.bio = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
    }

    public EditProfilerequest() {
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driverId", driverId).append("firstName", firstName).append("lastName", lastName).append("countryCode", contryCode).append("phone", phone).append("dob", dob).append("gender", gender).append("bio", bio).append("profilePic", profilePic).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(driverId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(contryCode);
        dest.writeValue(phone);
        dest.writeValue(dob);
        dest.writeValue(gender);
        dest.writeValue(bio);
        dest.writeValue(profilePic);
    }

    public int describeContents() {
        return 0;
    }

}
