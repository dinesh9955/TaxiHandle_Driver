package com.rydz.driver.model.userRating;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RatingList implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("status")
    @Expose
    private Integer status;
    public final static Parcelable.Creator<RatingList> CREATOR = new Creator<RatingList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RatingList createFromParcel(Parcel in) {
            return new RatingList(in);
        }

        public RatingList[] newArray(int size) {
            return (new RatingList[size]);
        }

    }
            ;

    protected RatingList(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.user = ((Object) in.readValue((Object.class.getClassLoader())));
        this.driver = ((String) in.readValue((String.class.getClassLoader())));
        this.bookingId = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((Double) in.readValue((Double.class.getClassLoader())));
        this.review = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((Long) in.readValue((Long.class.getClassLoader())));
        this.v = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public RatingList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("user", user).append("driver", driver).append("bookingId", bookingId).append("rating", rating).append("review", review).append("date", date).append("v", v).append("status", status).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(user);
        dest.writeValue(driver);
        dest.writeValue(bookingId);
        dest.writeValue(rating);
        dest.writeValue(review);
        dest.writeValue(date);
        dest.writeValue(v);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}
