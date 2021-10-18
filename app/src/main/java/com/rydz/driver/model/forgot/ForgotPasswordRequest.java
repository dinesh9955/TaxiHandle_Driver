package com.rydz.driver.model.forgot;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ForgotPasswordRequest implements Parcelable
{

    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("newPassword")
    @Expose
    private String newPassword;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    public final static Parcelable.Creator<ForgotPasswordRequest> CREATOR = new Creator<ForgotPasswordRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ForgotPasswordRequest createFromParcel(Parcel in) {
            return new ForgotPasswordRequest(in);
        }

        public ForgotPasswordRequest[] newArray(int size) {
            return (new ForgotPasswordRequest[size]);
        }

    }
            ;

    protected ForgotPasswordRequest(Parcel in) {
        this.contryCode = ((String) in.readValue((String.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.newPassword = ((String) in.readValue((String.class.getClassLoader())));
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ForgotPasswordRequest() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("countryCode", contryCode).append("phone", phone).append("email", email).append("newPassword", newPassword).append("adminId", adminId).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(contryCode);
        dest.writeValue(phone);
        dest.writeValue(email);
        dest.writeValue(newPassword);
        dest.writeValue(adminId);
    }

    public int describeContents() {
        return 0;
    }

}
