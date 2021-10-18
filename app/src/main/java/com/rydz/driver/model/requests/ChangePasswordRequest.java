package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChangePasswordRequest implements Parcelable
{

    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    public final static Parcelable.Creator<ChangePasswordRequest> CREATOR = new Creator<ChangePasswordRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChangePasswordRequest createFromParcel(Parcel in) {
            return new ChangePasswordRequest(in);
        }

        public ChangePasswordRequest[] newArray(int size) {
            return (new ChangePasswordRequest[size]);
        }

    }
            ;

    protected ChangePasswordRequest(Parcel in) {
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.oldPassword = ((String) in.readValue((String.class.getClassLoader())));
        this.newPassword = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ChangePasswordRequest() {
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("driverId", driverId).append("oldPassword", oldPassword).append("newPassword", newPassword).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(driverId);
        dest.writeValue(oldPassword);
        dest.writeValue(newPassword);
    }

    public int describeContents() {
        return 0;
    }

}
