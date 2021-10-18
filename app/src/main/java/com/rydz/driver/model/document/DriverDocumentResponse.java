package com.rydz.driver.model.document;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DriverDocumentResponse implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("docList")
    @Expose
    private List<DocList> docList = null;
    public final static Parcelable.Creator<DriverDocumentResponse> CREATOR = new Creator<DriverDocumentResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DriverDocumentResponse createFromParcel(Parcel in) {
            return new DriverDocumentResponse(in);
        }

        public DriverDocumentResponse[] newArray(int size) {
            return (new DriverDocumentResponse[size]);
        }

    }
            ;

    protected DriverDocumentResponse(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.docList, (DocList.class.getClassLoader()));
    }

    public DriverDocumentResponse() {
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

    public List<DocList> getDocList() {
        return docList;
    }

    public void setDocList(List<DocList> docList) {
        this.docList = docList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("message", message).append("docList", docList).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeList(docList);
    }

    public int describeContents() {
        return 0;
    }

}
