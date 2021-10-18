package com.rydz.driver.model.phoneStatus;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PhoneStatusResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<PhoneStatusResponse> CREATOR = new Creator<PhoneStatusResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PhoneStatusResponse createFromParcel(Parcel in) {
            return new PhoneStatusResponse(in);
        }

        public PhoneStatusResponse[] newArray(int size) {
            return (new PhoneStatusResponse[size]);
        }

    }
            ;

    protected PhoneStatusResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PhoneStatusResponse() {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}
