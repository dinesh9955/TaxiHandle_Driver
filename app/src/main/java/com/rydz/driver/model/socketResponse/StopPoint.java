package com.rydz.driver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class StopPoint implements Parcelable
{

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<StopPoint> CREATOR = new Creator<StopPoint>() {


        @SuppressWarnings({
                "unchecked"
        })
        public StopPoint createFromParcel(Parcel in) {
            return new StopPoint(in);
        }

        public StopPoint[] newArray(int size) {
            return (new StopPoint[size]);
        }

    }
            ;

    protected StopPoint(Parcel in) {
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StopPoint() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("latitude", latitude).append("longitude", longitude).append("name", name).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}
