package com.rydz.driver.model.googleMapApiresponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Step implements Parcelable
{

    @SerializedName("distance")
    @Expose
    private Distance_ distance;
    @SerializedName("duration")
    @Expose
    private Duration_ duration;
    @SerializedName("end_location")
    @Expose
    private EndLocation_ endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private Polyline polyline;
    @SerializedName("start_location")
    @Expose
    private StartLocation_ startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;
    public final static Parcelable.Creator<Step> CREATOR = new Creator<Step>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return (new Step[size]);
        }

    }
            ;

    protected Step(Parcel in) {
        this.distance = ((Distance_) in.readValue((Distance_.class.getClassLoader())));
        this.duration = ((Duration_) in.readValue((Duration_.class.getClassLoader())));
        this.endLocation = ((EndLocation_) in.readValue((EndLocation_.class.getClassLoader())));
        this.htmlInstructions = ((String) in.readValue((String.class.getClassLoader())));
        this.polyline = ((Polyline) in.readValue((Polyline.class.getClassLoader())));
        this.startLocation = ((StartLocation_) in.readValue((StartLocation_.class.getClassLoader())));
        this.travelMode = ((String) in.readValue((String.class.getClassLoader())));
        this.maneuver = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Step() {
    }

    public Distance_ getDistance() {
        return distance;
    }

    public void setDistance(Distance_ distance) {
        this.distance = distance;
    }

    public Duration_ getDuration() {
        return duration;
    }

    public void setDuration(Duration_ duration) {
        this.duration = duration;
    }

    public EndLocation_ getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation_ endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocation_ getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation_ startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endLocation", endLocation).append("htmlInstructions", htmlInstructions).append("polyline", polyline).append("startLocation", startLocation).append("travelMode", travelMode).append("maneuver", maneuver).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeValue(endLocation);
        dest.writeValue(htmlInstructions);
        dest.writeValue(polyline);
        dest.writeValue(startLocation);
        dest.writeValue(travelMode);
        dest.writeValue(maneuver);
    }

    public int describeContents() {
        return 0;
    }

}