package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UpdateEmailRequest implements Parcelable
{

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    public final static Parcelable.Creator<UpdateEmailRequest> CREATOR = new Creator<UpdateEmailRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UpdateEmailRequest createFromParcel(Parcel in) {
            return new UpdateEmailRequest(in);
        }

        public UpdateEmailRequest[] newArray(int size) {
            return (new UpdateEmailRequest[size]);
        }

    }
            ;

    protected UpdateEmailRequest(Parcel in) {
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UpdateEmailRequest() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminId", adminId).append("email", email).append("driverId", driverId).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adminId);
        dest.writeValue(email);
        dest.writeValue(driverId);
    }

    public int describeContents() {
        return 0;
    }

}
