package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PhoneStatusRequest implements Parcelable
{

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    public final static Parcelable.Creator<PhoneStatusRequest> CREATOR = new Creator<PhoneStatusRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PhoneStatusRequest createFromParcel(Parcel in) {
            return new PhoneStatusRequest(in);
        }

        public PhoneStatusRequest[] newArray(int size) {
            return (new PhoneStatusRequest[size]);
        }

    }
            ;

    protected PhoneStatusRequest(Parcel in) {
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.countryCode=((String) in.readValue((String.class.getClassLoader())));
    }

    public PhoneStatusRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("phone", phone).append("countryCode", countryCode).append("driverId", driverId).append("adminId", adminId).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(phone);
        dest.writeValue(driverId);
        dest.writeValue(adminId);
        dest.writeValue(countryCode);
    }

    public int describeContents() {
        return 0;
    }

}
