package com.rydz.driver.model.googleMapApiresponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GeocodedWaypoint implements Parcelable
{

    @SerializedName("geocoder_status")
    @Expose
    private String geocoderStatus;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Parcelable.Creator<GeocodedWaypoint> CREATOR = new Creator<GeocodedWaypoint>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GeocodedWaypoint createFromParcel(Parcel in) {
            return new GeocodedWaypoint(in);
        }

        public GeocodedWaypoint[] newArray(int size) {
            return (new GeocodedWaypoint[size]);
        }

    }
            ;

    protected GeocodedWaypoint(Parcel in) {
        this.geocoderStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.placeId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.types, (java.lang.String.class.getClassLoader()));
    }

    public GeocodedWaypoint() {
    }

    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("geocoderStatus", geocoderStatus).append("placeId", placeId).append("types", types).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(geocoderStatus);
        dest.writeValue(placeId);
        dest.writeList(types);
    }

    public int describeContents() {
        return 0;
    }

}
