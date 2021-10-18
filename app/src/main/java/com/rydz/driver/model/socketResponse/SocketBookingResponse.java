package com.rydz.driver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocketBookingResponse    implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("booking")
    @Expose
    private BookingResponse booking;

    @SerializedName("cancelledRides")
    @Expose
    private Double cancelledRides;


    protected SocketBookingResponse(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        booking = in.readParcelable(BookingResponse.class.getClassLoader());
        if (in.readByte() == 0) {
            cancelledRides = null;
        } else {
            cancelledRides = in.readDouble();
        }
    }

    public static final Creator<SocketBookingResponse> CREATOR = new Creator<SocketBookingResponse>() {
        @Override
        public SocketBookingResponse createFromParcel(Parcel in) {
            return new SocketBookingResponse(in);
        }

        @Override
        public SocketBookingResponse[] newArray(int size) {
            return new SocketBookingResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        parcel.writeParcelable(booking, i);
        if (cancelledRides == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(cancelledRides);
        }
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BookingResponse getBooking() {
        return booking;
    }

    public void setBooking(BookingResponse booking) {
        this.booking = booking;
    }

    public Double getCancelledRides() {
        return cancelledRides;
    }

    public void setCancelledRides(Double cancelledRides) {
        this.cancelledRides = cancelledRides;
    }
}