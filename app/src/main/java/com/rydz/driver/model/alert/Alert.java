package com.rydz.driver.model.alert;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Alert implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("notiType")
    @Expose
    private Long notiType;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("__v")
    @Expose
    private Long v;
    public final static Parcelable.Creator<Alert> CREATOR = new Creator<Alert>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        public Alert[] newArray(int size) {
            return (new Alert[size]);
        }

    }
            ;

    protected Alert(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.notiType = ((Long) in.readValue((Long.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((Long) in.readValue((Long.class.getClassLoader())));
        this.v = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public Alert() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNotiType() {
        return notiType;
    }

    public void setNotiType(Long notiType) {
        this.notiType = notiType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("notiType", notiType).append("message", message).append("date", date).append("v", v).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(notiType);
        dest.writeValue(message);
        dest.writeValue(date);
        dest.writeValue(v);
    }

    public int describeContents() {
        return 0;
    }

}
