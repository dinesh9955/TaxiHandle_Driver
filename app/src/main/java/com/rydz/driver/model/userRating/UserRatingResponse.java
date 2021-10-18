package com.rydz.driver.model.userRating;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserRatingResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ratingList")
    @Expose
    private List<RatingList> ratingList = null;
    @SerializedName("totalRating")
    @Expose
    private Double totalRating;
    @SerializedName("accepted")
    @Expose
    private Integer accepted;
    @SerializedName("canceled")
    @Expose
    private Integer canceled;
    public final static Parcelable.Creator<UserRatingResponse> CREATOR = new Creator<UserRatingResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserRatingResponse createFromParcel(Parcel in) {
            return new UserRatingResponse(in);
        }

        public UserRatingResponse[] newArray(int size) {
            return (new UserRatingResponse[size]);
        }

    }
            ;

    protected UserRatingResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ratingList, (RatingList.class.getClassLoader()));
        this.totalRating = ((Double) in.readValue((Double.class.getClassLoader())));
        this.accepted = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.canceled = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public UserRatingResponse() {
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

    public List<RatingList> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<RatingList> ratingList) {
        this.ratingList = ratingList;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public Integer getAccepted() {
        return accepted;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public Integer getCanceled() {
        return canceled;
    }

    public void setCanceled(Integer canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("ratingList", ratingList).append("totalRating", totalRating).append("accepted", accepted).append("canceled", canceled).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(ratingList);
        dest.writeValue(totalRating);
        dest.writeValue(accepted);
        dest.writeValue(canceled);
    }

    public int describeContents() {
        return 0;
    }

}