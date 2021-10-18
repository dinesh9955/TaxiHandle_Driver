package com.rydz.driver.model.googleMapApiresponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Route implements Parcelable
{

    @SerializedName("bounds")
    @Expose
    private Bounds bounds;
    @SerializedName("copyrights")
    @Expose
    private String copyrights;
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = null;
    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("warnings")
    @Expose
    private List<Object> warnings = null;
    @SerializedName("waypoint_order")
    @Expose
    private List<Object> waypointOrder = null;
    public final static Parcelable.Creator<Route> CREATOR = new Creator<Route>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return (new Route[size]);
        }

    }
            ;

    protected Route(Parcel in) {
        this.bounds = ((Bounds) in.readValue((Bounds.class.getClassLoader())));
        this.copyrights = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.legs, (Leg.class.getClassLoader()));
        this.overviewPolyline = ((OverviewPolyline) in.readValue((OverviewPolyline.class.getClassLoader())));
        this.summary = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.warnings, (java.lang.Object.class.getClassLoader()));
        in.readList(this.waypointOrder, (java.lang.Object.class.getClassLoader()));
    }

    public Route() {
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    public List<Object> getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(List<Object> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bounds", bounds).append("copyrights", copyrights).append("legs", legs).append("overviewPolyline", overviewPolyline).append("summary", summary).append("warnings", warnings).append("waypointOrder", waypointOrder).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bounds);
        dest.writeValue(copyrights);
        dest.writeList(legs);
        dest.writeValue(overviewPolyline);
        dest.writeValue(summary);
        dest.writeList(warnings);
        dest.writeList(waypointOrder);
    }

    public int describeContents() {
        return 0;
    }

}