package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegistrerCheckStatusRequest implements Parcelable
{

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    public final static Parcelable.Creator<RegistrerCheckStatusRequest> CREATOR = new Creator<RegistrerCheckStatusRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RegistrerCheckStatusRequest createFromParcel(Parcel in) {
            return new RegistrerCheckStatusRequest(in);
        }

        public RegistrerCheckStatusRequest[] newArray(int size) {
            return (new RegistrerCheckStatusRequest[size]);
        }

    }
            ;

    protected RegistrerCheckStatusRequest(Parcel in) {
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RegistrerCheckStatusRequest() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("adminId", adminId).append("email", email).append("countryCode", contryCode).append("phone", phone).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adminId);
        dest.writeValue(email);
        dest.writeValue(contryCode);
        dest.writeValue(phone);
    }

    public int describeContents() {
        return 0;
    }

}
