package com.rydz.driver.model.uploadImage;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UploadImageResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pic")
    @Expose
    private Pic pic;
    public final static Parcelable.Creator<UploadImageResponse> CREATOR = new Creator<UploadImageResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UploadImageResponse createFromParcel(Parcel in) {
            return new UploadImageResponse(in);
        }

        public UploadImageResponse[] newArray(int size) {
            return (new UploadImageResponse[size]);
        }

    }
            ;

    protected UploadImageResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.pic = ((Pic) in.readValue((Pic.class.getClassLoader())));
    }

    public UploadImageResponse() {
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

    public Pic getPic() {
        return pic;
    }

    public void setPic(Pic pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("pic", pic).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(pic);
    }

    public int describeContents() {
        return 0;
    }

    public class Pic implements Parcelable
    {

        @SerializedName("profilePic")
        @Expose
        private String profilePic;
        public final  Parcelable.Creator<Pic> CREATOR = new Creator<Pic>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Pic createFromParcel(Parcel in) {
                return new Pic(in);
            }

            public Pic[] newArray(int size) {
                return (new Pic[size]);
            }

        }
                ;

        protected Pic(Parcel in) {
            this.profilePic = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Pic() {
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("profilePic", profilePic).toString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(profilePic);
        }

        public int describeContents() {
            return 0;
        }

    }
}
