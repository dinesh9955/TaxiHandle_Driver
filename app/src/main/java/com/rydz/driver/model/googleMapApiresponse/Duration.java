package com.rydz.driver.model.googleMapApiresponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Duration implements Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private Long value;
    public final static Parcelable.Creator<Duration> CREATOR = new Creator<Duration>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Duration createFromParcel(Parcel in) {
            return new Duration(in);
        }

        public Duration[] newArray(int size) {
            return (new Duration[size]);
        }

    }
            ;

    protected Duration(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.value = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public Duration() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("text", text).append("value", value).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
        dest.writeValue(value);
    }

    public int describeContents() {
        return 0;
    }

}
