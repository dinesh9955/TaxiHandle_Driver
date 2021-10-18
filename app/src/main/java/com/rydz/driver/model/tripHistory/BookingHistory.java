package com.rydz.driver.model.tripHistory;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rydz.driver.model.Refund;
import com.rydz.driver.model.socketResponse.Destination;
import com.rydz.driver.model.socketResponse.Source;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BookingHistory   implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("vehicleType")
    @Expose
    private VehicleType vehicleType;
    @SerializedName("fare")
    @Expose
    private Double fare;
    @SerializedName("subtotalFare")
    @Expose
    private Double subtotalFare;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("tripEndTime")
    @Expose
    private Long tripEndTime;
    @SerializedName("tripStartTime")
    @Expose
    private Long tripStartTime;
    @SerializedName("isPaid")
    @Expose
    private Double isPaid;

    @SerializedName("stopPoints")
    @Expose
    private List<Object> stopPoints = null;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("refund")
    @Expose
    private Refund refund;

    protected BookingHistory(Parcel in) {
        id = in.readString();
        userId = in.readString();
        driverId = in.readString();
        vehicleType = in.readParcelable(VehicleType.class.getClassLoader());
        if (in.readByte() == 0) {
            fare = null;
        } else {
            fare = in.readDouble();
        }
        if (in.readByte() == 0) {
            subtotalFare = null;
        } else {
            subtotalFare = in.readDouble();
        }
        paymentMode = in.readString();
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readLong();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readLong();
        }
        if (in.readByte() == 0) {
            tripEndTime = null;
        } else {
            tripEndTime = in.readLong();
        }
        if (in.readByte() == 0) {
            tripStartTime = null;
        } else {
            tripStartTime = in.readLong();
        }
        if (in.readByte() == 0) {
            isPaid = null;
        } else {
            isPaid = in.readDouble();
        }
        if (in.readByte() == 0) {
            tax = null;
        } else {
            tax = in.readDouble();
        }
        in.readList(this.stopPoints, (java.lang.Object.class.getClassLoader()));
        this.destination = ((Destination) in.readValue((Destination.class.getClassLoader())));
        this.source = ((Source) in.readValue((Source.class.getClassLoader())));
        refund = in.readParcelable(Refund.class.getClassLoader());
    }

    public static final Creator<BookingHistory> CREATOR = new Creator<BookingHistory>() {
        @Override
        public BookingHistory createFromParcel(Parcel in) {
            return new BookingHistory(in);
        }

        @Override
        public BookingHistory[] newArray(int size) {
            return new BookingHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(driverId);
        parcel.writeParcelable(vehicleType, i);
        if (fare == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(fare);
        }
        if (subtotalFare == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(subtotalFare);
        }
        parcel.writeString(paymentMode);
        if (date == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(date);
        }
        if (v == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(v);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(status);
        }
        if (tripEndTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(tripEndTime);
        }
        if (tripStartTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(tripStartTime);
        }
        if (isPaid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(isPaid);
        }
        if (tax == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(tax);
        }
        parcel.writeList(stopPoints);
        parcel.writeValue(destination);
        parcel.writeValue(source);
        parcel.writeParcelable(refund, i);
    }




/*
    protected BookingHistory(Parcel in) {
        id = in.readString();
        userId = in.readString();
        driverId = in.readString();
        vehicleType = in.readParcelable(VehicleType.class.getClassLoader());
        if (in.readByte() == 0) {
            fare = null;
        } else {
            fare = in.readDouble();
        }
        if (in.readByte() == 0) {
            subtotalFare = null;
        } else {
            subtotalFare = in.readDouble();
        }
        paymentMode = in.readString();
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readLong();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readLong();
        }
        if (in.readByte() == 0) {
            tripEndTime = null;
        } else {
            tripEndTime = in.readLong();
        }
        if (in.readByte() == 0) {
            tripStartTime = null;
        } else {
            tripStartTime = in.readLong();
        }
        if (in.readByte() == 0) {
            isPaid = null;
        } else {
            isPaid = in.readDouble();
        }
        if (in.readByte() == 0) {
            tax = null;
        } else {
            tax = in.readDouble();
        }
        in.readList(this.stopPoints, (java.lang.Object.class.getClassLoader()));
        this.destination = ((Destination) in.readValue((Destination.class.getClassLoader())));
        this.source = ((Source) in.readValue((Source.class.getClassLoader())));
        refund = in.readParcelable(Refund.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(driverId);
        dest.writeParcelable(vehicleType, flags);
        if (fare == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(fare);
        }
        if (subtotalFare == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(subtotalFare);
        }
        dest.writeString(paymentMode);
        if (date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(date);
        }
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(v);
        }
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(status);
        }
        if (tripEndTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(tripEndTime);
        }
        if (tripStartTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(tripStartTime);
        }
        if (isPaid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(isPaid);
        }
        if (tax == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(tax);
        }
        dest.writeList(stopPoints);
        dest.writeValue(destination);
        dest.writeValue(source);
        dest.writeParcelable(refund, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookingHistory> CREATOR = new Creator<BookingHistory>() {
        @Override
        public BookingHistory createFromParcel(Parcel in) {
            return new BookingHistory(in);
        }

        @Override
        public BookingHistory[] newArray(int size) {
            return new BookingHistory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getSubtotalFare() {
        return subtotalFare;
    }

    public void setSubtotalFare(Double subtotalFare) {
        this.subtotalFare = subtotalFare;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(Long tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public Long getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Long tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Double getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Double isPaid) {
        this.isPaid = isPaid;
    }

    public List<Object> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<Object> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getSubtotalFare() {
        return subtotalFare;
    }

    public void setSubtotalFare(Double subtotalFare) {
        this.subtotalFare = subtotalFare;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTripEndTime() {
        return tripEndTime;
    }

    public void setTripEndTime(Long tripEndTime) {
        this.tripEndTime = tripEndTime;
    }

    public Long getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(Long tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

    public Double getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Double isPaid) {
        this.isPaid = isPaid;
    }

    public List<Object> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<Object> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public static Creator<BookingHistory> getCREATOR() {
        return CREATOR;
    }



}
