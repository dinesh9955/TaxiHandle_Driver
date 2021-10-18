package com.rydz.driver.model.socketResponse;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.reactivex.annotations.Nullable;

public class Booking implements Serializable
{




    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @Nullable
    @SerializedName("userId")
    @Expose
    private UserId userId;
    @SerializedName("driverId")
    @Expose
    private String driverId;

    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("mapImage")
    @Expose
    private String mapImage;
    @SerializedName("tripEndTime")
    @Expose
    private Long tripEndTime;
    @SerializedName("tripStartTime")
    @Expose
    private Long tripStartTime;
    @SerializedName("isPaid")
    @Expose
    private Integer isPaid;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("commission")
    @Expose
    private Double commission;
    @SerializedName("subtotalFare")
    @Expose
    private Double subtotalFare;
    @SerializedName("fare")
    @Expose
    private Double fare;
    @SerializedName("couponAmount")
    @Expose
    private Double couponAmount;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("stopPoints")
    @Expose
    private List<Object> stopPoints = null;
    @SerializedName("destination")
    @Expose
    private Destination destination;
    @SerializedName("source")
    @Expose
    private Source source;
   /* @SerializedName("driverRate")
    @Expose
    private List<Object> driverRate = null;*/


    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("bookingType")
    @Expose
    private String bookingType;




    @SerializedName("storeId")
    @Expose
    private Store storeId;
    @Nullable
    @SerializedName("msgRead")
    @Expose
    private Boolean msgRead=false;

    @SerializedName("itemAmount")
    @Expose
    private Double itemAmount;
    @SerializedName("OrderNo")
    @Expose
    private Number OrderNo;


    public Double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(Double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private final static long serialVersionUID = -7943402607627220316L;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
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

    public String getMapImage() {
        return mapImage;
    }

    public void setMapImage(String mapImage) {
        this.mapImage = mapImage;
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

    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getSubtotalFare() {
        return subtotalFare;
    }

    public void setSubtotalFare(Double subtotalFare) {
        this.subtotalFare = subtotalFare;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
/*
    public List<Object> getDriverRate() {
        return driverRate;
    }

    public void setDriverRate(List<Object> driverRate) {
        this.driverRate = driverRate;
    }*/


    public Store getStoreId() {
        return storeId;
    }

    public void setStoreId(Store storeId) {
        this.storeId = storeId;
    }


    public Boolean getMsgRead() {
        return msgRead;
    }

    public void setMsgRead(Boolean msgRead) {
        this.msgRead = msgRead;
    }

    public Number getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(Number orderNo) {
        OrderNo = orderNo;
    }
}



