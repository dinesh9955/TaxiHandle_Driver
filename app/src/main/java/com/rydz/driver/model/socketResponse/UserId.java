package com.rydz.driver.model.socketResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserId implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("inviteCode")
    @Expose
    private String inviteCode;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("userType")
    @Expose
    private Long userType;
    @SerializedName("isFullUrl")
    @Expose
    private Long isFullUrl;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("wallet")
    @Expose
    private Double wallet;
    @SerializedName("ratings")
    @Expose
    private Object ratings;
    private final static long serialVersionUID = 3844572494737492065L;

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

    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getIsFullUrl() {
        return isFullUrl;
    }

    public void setIsFullUrl(Long isFullUrl) {
        this.isFullUrl = isFullUrl;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public Object getRatings() {
        return ratings;
    }

    public void setRatings(Object ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("updatedAt", updatedAt).append("createdAt", createdAt).append("firstName", firstName).append("lastName", lastName).append("email", email).append("countryCode", contryCode).append("phone", phone).append("inviteCode", inviteCode).append("v", v).append("userType", userType).append("isFullUrl", isFullUrl).append("profilePic", profilePic).append("wallet", wallet).append("ratings", ratings).toString();
    }

}