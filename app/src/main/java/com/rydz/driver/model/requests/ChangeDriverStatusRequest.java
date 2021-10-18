package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChangeDriverStatusRequest implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Parcelable.Creator<ChangeDriverStatusRequest> CREATOR = new Creator<ChangeDriverStatusRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChangeDriverStatusRequest createFromParcel(Parcel in) {
            return new ChangeDriverStatusRequest(in);
        }

        public ChangeDriverStatusRequest[] newArray(int size) {
            return (new ChangeDriverStatusRequest[size]);
        }

    }
            ;

    protected ChangeDriverStatusRequest(Parcel in) {
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ChangeDriverStatusRequest() {
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driverId", driverId).append("status", status).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(driverId);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}
