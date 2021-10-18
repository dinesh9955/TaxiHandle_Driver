package com.rydz.driver.model.tripHistory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TriphistoryRequest  implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("skip")
    @Expose
    private int skip;
    @SerializedName("type")
    @Expose
    private int type;

    protected TriphistoryRequest(Parcel in) {
        driverId = in.readString();
        skip = in.readInt();
        type = in.readInt();
    }

    public TriphistoryRequest() {
    }

    public static final Creator<TriphistoryRequest> CREATOR = new Creator<TriphistoryRequest>() {
        @Override
        public TriphistoryRequest createFromParcel(Parcel in) {
            return new TriphistoryRequest(in);
        }

        @Override
        public TriphistoryRequest[] newArray(int size) {
            return new TriphistoryRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(driverId);
        parcel.writeInt(skip);
        parcel.writeInt(type);
    }


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Creator<TriphistoryRequest> getCREATOR() {
        return CREATOR;
    }
}