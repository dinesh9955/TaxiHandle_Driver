package com.rydz.driver.model.googleMapApiresponse;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Duration_ implements Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private Long value;
    public final static Parcelable.Creator<Duration_> CREATOR = new Creator<Duration_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Duration_ createFromParcel(Parcel in) {
            return new Duration_(in);
        }

        public Duration_[] newArray(int size) {
            return (new Duration_[size]);
        }

    }
            ;

    protected Duration_(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.value = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    public Duration_() {
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
