package com.rydz.driver.model.alert;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AlertResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("alerts")
    @Expose
    private ArrayList<Alert> alerts = null;
    public final static Parcelable.Creator<AlertResponse> CREATOR = new Creator<AlertResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AlertResponse createFromParcel(Parcel in) {
            return new AlertResponse(in);
        }

        public AlertResponse[] newArray(int size) {
            return (new AlertResponse[size]);
        }

    }
            ;

    protected AlertResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.alerts, (Alert.class.getClassLoader()));
    }

    public AlertResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(ArrayList<Alert> alerts) {
        this.alerts = alerts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("alerts", alerts).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(alerts);
    }

    public int describeContents() {
        return 0;
    }

}
