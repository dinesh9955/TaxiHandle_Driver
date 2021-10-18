package com.rydz.driver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingSource implements Parcelable
{
    @Nullable
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @Nullable
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @Nullable
    @SerializedName("name")
    @Expose
    private String name;
    public final static Parcelable.Creator<BookingSource> CREATOR = new Creator<BookingSource>() {


        @SuppressWarnings({
                "unchecked"
        })
        public BookingSource createFromParcel(Parcel in) {
            return new BookingSource(in);
        }

        public BookingSource[] newArray(int size) {
            return (new BookingSource[size]);
        }

    }
            ;

    protected BookingSource(Parcel in) {
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public BookingSource() {
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
