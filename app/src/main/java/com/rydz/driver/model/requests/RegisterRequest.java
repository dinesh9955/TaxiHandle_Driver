package com.rydz.driver.model.requests;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisterRequest implements  Parcelable
{

    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("countryCode")
    @Expose
    private String contryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicleName")
    @Expose
    private String vehicleName;
    @SerializedName("vehicleColor")
    @Expose
    private String vehicleColor;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("manufacturerName")
    @Expose
    private String manufacturerName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("isFullUrl")
    @Expose
    private Long isFullUrl;
    @SerializedName("isVerified")
    @Expose
    private Long isVerified;
    @SerializedName("inviteCodeUsed")
    @Expose
    private String inviteCodeUsed;
    @SerializedName("accountNumber")
    @Expose
    private String accountNumber;
    @SerializedName("accountHolderName")
    @Expose
    private String accountHolderName;
    @SerializedName("swiftCode")
    @Expose
    private String swiftCode;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("isSubscribed")
    @Expose
    private Integer isSubscribed;


    public RegisterRequest()
    {

    }

    protected RegisterRequest(Parcel in) {
        adminId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        contryCode = in.readString();
        phone = in.readString();
        dob = in.readString();
        gender = in.readString();
        bio = in.readString();
        deviceId = in.readString();
        deviceType = in.readString();
        vehicleType = in.readString();
        vehicleNumber = in.readString();
        vehicleName = in.readString();
        vehicleColor = in.readString();
        model = in.readString();
        manufacturerName = in.readString();
        profilePic = in.readString();
        if (in.readByte() == 0) {
            isFullUrl = null;
        } else {
            isFullUrl = in.readLong();
        }
        if (in.readByte() == 0) {
            isVerified = null;
        } else {
            isVerified = in.readLong();
        }
        inviteCodeUsed = in.readString();
        accountNumber = in.readString();
        accountHolderName = in.readString();
        swiftCode = in.readString();
        company = in.readString();
        if (in.readByte() == 0) {
            isSubscribed = null;
        } else {
            isSubscribed = in.readInt();
        }
    }

    public static final Creator<RegisterRequest> CREATOR = new Creator<RegisterRequest>() {
        @Override
        public RegisterRequest createFromParcel(Parcel in) {
            return new RegisterRequest(in);
        }

        @Override
        public RegisterRequest[] newArray(int size) {
            return new RegisterRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adminId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(contryCode);
        dest.writeString(phone);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(bio);
        dest.writeString(deviceId);
        dest.writeString(deviceType);
        dest.writeString(vehicleType);
        dest.writeString(vehicleNumber);
        dest.writeString(vehicleName);
        dest.writeString(vehicleColor);
        dest.writeString(model);
        dest.writeString(manufacturerName);
        dest.writeString(profilePic);
        if (isFullUrl == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(isFullUrl);
        }
        if (isVerified == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(isVerified);
        }
        dest.writeString(inviteCodeUsed);
        dest.writeString(accountNumber);
        dest.writeString(accountHolderName);
        dest.writeString(swiftCode);
        dest.writeString(company);
        if (isSubscribed == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isSubscribed);
        }
    }


    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Long getIsFullUrl() {
        return isFullUrl;
    }

    public void setIsFullUrl(Long isFullUrl) {
        this.isFullUrl = isFullUrl;
    }

    public Long getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Long isVerified) {
        this.isVerified = isVerified;
    }

    public String getInviteCodeUsed() {
        return inviteCodeUsed;
    }

    public void setInviteCodeUsed(String inviteCodeUsed) {
        this.inviteCodeUsed = inviteCodeUsed;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Integer isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public static Creator<RegisterRequest> getCREATOR() {
        return CREATOR;
    }
}