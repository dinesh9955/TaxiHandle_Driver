package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SupportRequest implements Parcelable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("comment")
    @Expose
    private String comment;
    public final static Parcelable.Creator<SupportRequest> CREATOR = new Creator<SupportRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SupportRequest createFromParcel(Parcel in) {
            return new SupportRequest(in);
        }

        public SupportRequest[] newArray(int size) {
            return (new SupportRequest[size]);
        }

    }
            ;

    protected SupportRequest(Parcel in) {
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.subject = ((String) in.readValue((String.class.getClassLoader())));
        this.comment = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SupportRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("userId", userId).append("subject", subject).append("comment", comment).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeValue(userId);
        dest.writeValue(subject);
        dest.writeValue(comment);
    }

    public int describeContents() {
        return 0;
    }

}
