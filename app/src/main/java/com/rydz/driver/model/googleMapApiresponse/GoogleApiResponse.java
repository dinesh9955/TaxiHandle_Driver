package com.rydz.driver.model.googleMapApiresponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GoogleApiResponse implements Parcelable
{

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Parcelable.Creator<GoogleApiResponse> CREATOR = new Creator<GoogleApiResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GoogleApiResponse createFromParcel(Parcel in) {
            return new GoogleApiResponse(in);
        }

        public GoogleApiResponse[] newArray(int size) {
            return (new GoogleApiResponse[size]);
        }

    }
            ;

    protected GoogleApiResponse(Parcel in) {
        in.readList(this.geocodedWaypoints, (GeocodedWaypoint.class.getClassLoader()));
        in.readList(this.routes, (Route.class.getClassLoader()));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public GoogleApiResponse() {
    }

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("geocodedWaypoints", geocodedWaypoints).append("routes", routes).append("status", status).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(geocodedWaypoints);
        dest.writeList(routes);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}