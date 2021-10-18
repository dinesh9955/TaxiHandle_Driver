package com.rydz.driver.model.socketResponse;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;

import java.util.List;

public class BookingResponse  implements Parcelable {

    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("CountryCode")
    @Expose
    public String CountryCode;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("isPaid")
    @Expose
    public Integer isPaid;
    @SerializedName("bookingType")
    @Expose
    public Integer bookingType;
    @SerializedName("type")
    @Expose
    public Integer type;
    @Nullable
    @SerializedName("source")
    @Expose
    private BookingSource source;
    @Nullable
    @SerializedName("destination")
    @Expose
    private BookingDestination destination;

    @Nullable
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @Nullable
    @SerializedName("userId")
    @Expose
    private String userId;
    @Nullable
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @Nullable
    @SerializedName("triptime")
    @Expose
    private String triptime;
    @Nullable
    @SerializedName("fare")
    @Expose
    private Double fare;
    @Nullable
    @SerializedName("tax")
    @Expose
    private Double tax;
    @Nullable
    @SerializedName("subtotalFare")
    @Expose
    private Double subtotalFare;
    @Nullable
    @SerializedName("walletAmountUsed")
    @Expose
    private Double walletAmountUsed;
    @Nullable
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @Nullable
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @Nullable
    @SerializedName("userRating")
    @Expose
    private Double userRating;
    @Nullable
    @SerializedName("date")
    @Expose
    private Long date;
    @Nullable
    @SerializedName("socketId")
    @Expose
    private String socketId;
    @Nullable
    @SerializedName("note")
    @Expose
    private String note;
    @Nullable
    @SerializedName("notiType")
    @Expose
    private Long notiType;
    @Nullable
    @SerializedName("scheduleId")
    @Expose
    private String scheduleId;
    @Nullable
    @SerializedName("storeId")
    @Expose
    private String storeId;
    @Nullable
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("isPaidInitial")
    @Expose
    private Number isPaidInitial;

    @SerializedName("itemAmount")
    @Expose
    private Number itemAmount;

    @SerializedName("slabs")
    @Expose
    private List<Response> slabs;


    @Nullable
    @SerializedName("distance")
    @Expose
    private String distance;

    @Nullable
    @SerializedName("cardId")
    @Expose
    private String cardId;


    protected BookingResponse(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        CountryCode = in.readString();
        phone = in.readString();
        if (in.readByte() == 0) {
            isPaid = null;
        } else {
            isPaid = in.readInt();
        }
        if (in.readByte() == 0) {
            bookingType = null;
        } else {
            bookingType = in.readInt();
        }
        if (in.readByte() == 0) {
            type = null;
        } else {
            type = in.readInt();
        }
        source = in.readParcelable(BookingSource.class.getClassLoader());
        destination = in.readParcelable(BookingDestination.class.getClassLoader());
        adminId = in.readString();
        userId = in.readString();
        vehicleType = in.readString();
        triptime = in.readString();
        if (in.readByte() == 0) {
            fare = null;
        } else {
            fare = in.readDouble();
        }
        if (in.readByte() == 0) {
            tax = null;
        } else {
            tax = in.readDouble();
        }
        if (in.readByte() == 0) {
            subtotalFare = null;
        } else {
            subtotalFare = in.readDouble();
        }
        if (in.readByte() == 0) {
            walletAmountUsed = null;
        } else {
            walletAmountUsed = in.readDouble();
        }
        paymentMode = in.readString();
        driverId = in.readString();
        if (in.readByte() == 0) {
            userRating = null;
        } else {
            userRating = in.readDouble();
        }
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        socketId = in.readString();
        note = in.readString();
        if (in.readByte() == 0) {
            notiType = null;
        } else {
            notiType = in.readLong();
        }
        scheduleId = in.readString();
        storeId = in.readString();
        _id = in.readString();
        slabs = in.createTypedArrayList(Response.CREATOR);
        distance = in.readString();
        cardId = in.readString();
    }

    public static final Creator<BookingResponse> CREATOR = new Creator<BookingResponse>() {
        @Override
        public BookingResponse createFromParcel(Parcel in) {
            return new BookingResponse(in);
        }

        @Override
        public BookingResponse[] newArray(int size) {
            return new BookingResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(CountryCode);
        parcel.writeString(phone);
        if (isPaid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isPaid);
        }
        if (bookingType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(bookingType);
        }
        if (type == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(type);
        }
        parcel.writeParcelable(source, i);
        parcel.writeParcelable(destination, i);
        parcel.writeString(adminId);
        parcel.writeString(userId);
        parcel.writeString(vehicleType);
        parcel.writeString(triptime);
        if (fare == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(fare);
        }
        if (tax == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(tax);
        }
        if (subtotalFare == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(subtotalFare);
        }
        if (walletAmountUsed == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(walletAmountUsed);
        }
        parcel.writeString(paymentMode);
        parcel.writeString(driverId);
        if (userRating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(userRating);
        }
        if (date == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(date);
        }
        parcel.writeString(socketId);
        parcel.writeString(note);
        if (notiType == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(notiType);
        }
        parcel.writeString(scheduleId);
        parcel.writeString(storeId);
        parcel.writeString(_id);
        parcel.writeTypedList(slabs);
        parcel.writeString(distance);
        parcel.writeString(cardId);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public Integer getBookingType() {
        return bookingType;
    }

    public void setBookingType(Integer bookingType) {
        this.bookingType = bookingType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BookingSource getSource() {
        return source;
    }

    public void setSource(BookingSource source) {
        this.source = source;
    }

    public BookingDestination getDestination() {
        return destination;
    }

    public void setDestination(BookingDestination destination) {
        this.destination = destination;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTriptime() {
        return triptime;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getSubtotalFare() {
        return subtotalFare;
    }

    public void setSubtotalFare(Double subtotalFare) {
        this.subtotalFare = subtotalFare;
    }

    public Double getWalletAmountUsed() {
        return walletAmountUsed;
    }

    public void setWalletAmountUsed(Double walletAmountUsed) {
        this.walletAmountUsed = walletAmountUsed;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getNotiType() {
        return notiType;
    }

    public void setNotiType(Long notiType) {
        this.notiType = notiType;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Number getIsPaidInitial() {
        return isPaidInitial;
    }

    public void setIsPaidInitial(Number isPaidInitial) {
        this.isPaidInitial = isPaidInitial;
    }

    public Number getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(Number itemAmount) {
        this.itemAmount = itemAmount;
    }

    public List<Response> getSlabs() {
        return slabs;
    }

    public void setSlabs(List<Response> slabs) {
        this.slabs = slabs;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public static Creator<BookingResponse> getCREATOR() {
        return CREATOR;
    }
}