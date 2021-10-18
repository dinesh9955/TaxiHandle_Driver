package com.rydz.driver.model.message;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Driver implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("ratings")
    @Expose
    private Object ratings;
    public final static Parcelable.Creator<Driver> CREATOR = new Creator<Driver>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        public Driver[] newArray(int size) {
            return (new Driver[size]);
        }

    }
            ;

    protected Driver(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        this.ratings = ((Object) in.readValue((Object.class.getClassLoader())));
    }

    public Driver() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return new ToStringBuilder(this).append("id", id).append("firstName", firstName).append("lastName", lastName).append("profilePic", profilePic).append("ratings", ratings).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(profilePic);
        dest.writeValue(ratings);
    }

    public int describeContents() {
        return 0;
    }

}
