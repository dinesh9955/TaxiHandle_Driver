package com.rydz.driver.model.tripHistory;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TripHistoryResponse  implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("bookingHistory")
    @Expose
    private ArrayList<BookingHistory> bookingHistory = null;
    @SerializedName("totalEarning")
    @Expose
    private Double totalEarning;




    public TripHistoryResponse() {
    }


    protected TripHistoryResponse(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        bookingHistory = in.createTypedArrayList(BookingHistory.CREATOR);
        if (in.readByte() == 0) {
            totalEarning = null;
        } else {
            totalEarning = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        dest.writeTypedList(bookingHistory);
        if (totalEarning == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(totalEarning);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripHistoryResponse> CREATOR = new Creator<TripHistoryResponse>() {
        @Override
        public TripHistoryResponse createFromParcel(Parcel in) {
            return new TripHistoryResponse(in);
        }

        @Override
        public TripHistoryResponse[] newArray(int size) {
            return new TripHistoryResponse[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<BookingHistory> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(ArrayList<BookingHistory> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public Double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(Double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public static Creator<TripHistoryResponse> getCREATOR() {
        return CREATOR;
    }
}

