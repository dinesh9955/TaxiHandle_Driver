package com.rydz.driver.model.googleMapApiresponse;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Leg implements Parcelable
{

    @SerializedName("distance")
    @Expose
    private Distance distance;
    @SerializedName("duration")
    @Expose
    private Duration duration;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_location")
    @Expose
    private EndLocation endLocation;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("traffic_speed_entry")
    @Expose
    private List<Object> trafficSpeedEntry = null;
    @SerializedName("via_waypoint")
    @Expose
    private List<Object> viaWaypoint = null;
    public final static Parcelable.Creator<Leg> CREATOR = new Creator<Leg>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Leg createFromParcel(Parcel in) {
            return new Leg(in);
        }

        public Leg[] newArray(int size) {
            return (new Leg[size]);
        }

    }
            ;

    protected Leg(Parcel in) {
        this.distance = ((Distance) in.readValue((Distance.class.getClassLoader())));
        this.duration = ((Duration) in.readValue((Duration.class.getClassLoader())));
        this.endAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.endLocation = ((EndLocation) in.readValue((EndLocation.class.getClassLoader())));
        this.startAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.startLocation = ((StartLocation) in.readValue((StartLocation.class.getClassLoader())));
        in.readList(this.steps, (Step.class.getClassLoader()));
        in.readList(this.trafficSpeedEntry, (java.lang.Object.class.getClassLoader()));
        in.readList(this.viaWaypoint, (java.lang.Object.class.getClassLoader()));
    }

    public Leg() {
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public EndLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public StartLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endAddress", endAddress).append("endLocation", endLocation).append("startAddress", startAddress).append("startLocation", startLocation).append("steps", steps).append("trafficSpeedEntry", trafficSpeedEntry).append("viaWaypoint", viaWaypoint).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(distance);
        dest.writeValue(duration);
        dest.writeValue(endAddress);
        dest.writeValue(endLocation);
        dest.writeValue(startAddress);
        dest.writeValue(startLocation);
        dest.writeList(steps);
        dest.writeList(trafficSpeedEntry);
        dest.writeList(viaWaypoint);
    }

    public int describeContents() {
        return 0;
    }

}
