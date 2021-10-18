package com.rydz.driver.model.chat;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChatHistoryRequest implements Parcelable
{

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    public final static Parcelable.Creator<ChatHistoryRequest> CREATOR = new Creator<ChatHistoryRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatHistoryRequest createFromParcel(Parcel in) {
            return new ChatHistoryRequest(in);
        }

        public ChatHistoryRequest[] newArray(int size) {
            return (new ChatHistoryRequest[size]);
        }

    }
            ;

    protected ChatHistoryRequest(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.bookingId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ChatHistoryRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("driverId", driverId).append("bookingId", bookingId).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(driverId);
        dest.writeValue(bookingId);
    }

    public int describeContents() {
        return 0;
    }

}
