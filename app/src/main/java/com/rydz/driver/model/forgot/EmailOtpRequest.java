package com.rydz.driver.model.forgot;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EmailOtpRequest implements Parcelable
{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("code")
    @Expose
    private String code;
    public final static Parcelable.Creator<EmailOtpRequest> CREATOR = new Creator<EmailOtpRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EmailOtpRequest createFromParcel(Parcel in) {
            return new EmailOtpRequest(in);
        }

        public EmailOtpRequest[] newArray(int size) {
            return (new EmailOtpRequest[size]);
        }

    }
            ;

    protected EmailOtpRequest(Parcel in) {
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.adminId = ((String) in.readValue((String.class.getClassLoader())));
        this.code=((String) in.readValue((String.class.getClassLoader())));
    }

    public EmailOtpRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).append("adminId", adminId).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(adminId);
        dest.writeValue(code);
    }

    public int describeContents() {
        return 0;
    }

}
