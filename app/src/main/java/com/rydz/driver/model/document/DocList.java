package com.rydz.driver.model.document;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DocList implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("__v")
    @Expose
    private Integer v;
    public final static Parcelable.Creator<DocList> CREATOR = new Creator<DocList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DocList createFromParcel(Parcel in) {
            return new DocList(in);
        }

        public DocList[] newArray(int size) {
            return (new DocList[size]);
        }

    }
            ;

    protected DocList(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.v = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DocList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("image", image).append("v", v).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(image);
        dest.writeValue(v);
    }

    public int describeContents() {
        return 0;
    }

}