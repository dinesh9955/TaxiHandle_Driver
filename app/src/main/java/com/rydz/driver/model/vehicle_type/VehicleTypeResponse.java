package com.rydz.driver.model.vehicle_type;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class VehicleTypeResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("vehicletype")
    @Expose
    private List<Vehicletype> vehicletype = null;
    public final static Parcelable.Creator<VehicleTypeResponse> CREATOR = new Creator<VehicleTypeResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VehicleTypeResponse createFromParcel(Parcel in) {
            return new VehicleTypeResponse(in);
        }

        public VehicleTypeResponse[] newArray(int size) {
            return (new VehicleTypeResponse[size]);
        }

    }
            ;

    protected VehicleTypeResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.vehicletype, (com.rydz.driver.model.vehicle_type.Vehicletype.class.getClassLoader()));
    }

    public VehicleTypeResponse() {
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

    public List<Vehicletype> getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(List<Vehicletype> vehicletype) {
        this.vehicletype = vehicletype;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("vehicletype", vehicletype).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(vehicletype);
    }

    public int describeContents() {
        return 0;
    }

}