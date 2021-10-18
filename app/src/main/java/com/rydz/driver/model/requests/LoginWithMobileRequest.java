package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginWithMobileRequest implements Parcelable
{

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    public final static Parcelable.Creator<LoginWithMobileRequest> CREATOR = new Creator<LoginWithMobileRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LoginWithMobileRequest createFromParcel(Parcel in) {
            return new LoginWithMobileRequest(in);
        }

        public LoginWithMobileRequest[] newArray(int size) {
            return (new LoginWithMobileRequest[size]);
        }

    }
            ;

    protected LoginWithMobileRequest(Parcel in) {
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.deviceType = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LoginWithMobileRequest() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminId", adminId).append("countryCode", contryCode).append("phone", phone).append("password", password).append("deviceId", deviceId).append("deviceType", deviceType).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adminId);
        dest.writeValue(contryCode);
        dest.writeValue(phone);
        dest.writeValue(password);
        dest.writeValue(deviceId);
        dest.writeValue(deviceType);
    }

    public int describeContents() {
        return 0;
    }

}
