package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserRatingRequest implements Parcelable
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
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;
    public final static Parcelable.Creator<UserRatingRequest> CREATOR = new Creator<UserRatingRequest>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserRatingRequest createFromParcel(Parcel in) {
            return new UserRatingRequest(in);
        }

        public UserRatingRequest[] newArray(int size) {
            return (new UserRatingRequest[size]);
        }

    }
            ;

    protected UserRatingRequest(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.driverId = ((String) in.readValue((String.class.getClassLoader())));
        this.bookingId = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((String) in.readValue((String.class.getClassLoader())));
        this.review = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserRatingRequest() {
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("userId", userId).append("driverId", driverId).append("bookingId", bookingId).append("rating", rating).append("review", review).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(driverId);
        dest.writeValue(bookingId);
        dest.writeValue(rating);
        dest.writeValue(review);
    }

    public int describeContents() {
        return 0;
    }

}
