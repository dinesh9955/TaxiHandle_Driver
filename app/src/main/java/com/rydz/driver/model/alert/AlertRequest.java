package com.rydz.driver.model.alert;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AlertRequest implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("skip")
    @Expose
    private int skip;
    public final static Creator<AlertRequest> CREATOR = new Creator<AlertRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AlertRequest createFromParcel(Parcel in) {
            return new AlertRequest(in);
        }

        public AlertRequest[] newArray(int size) {
            return (new AlertRequest[size]);
        }

    }
            ;

    protected AlertRequest(Parcel in) {
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.skip = ((int) in.readValue((int.class.getClassLoader())));

    }

    public AlertRequest() {
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


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driverId", driverId).append("skip", skip).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(driverId);
        dest.writeValue(skip);
    }

    public int describeContents() {
        return 0;
    }

}