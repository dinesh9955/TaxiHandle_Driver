package com.rydz.driver.model.message;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessageHistory implements Parcelable
{

    @SerializedName("success")
    @Expose
    public Long success;
    @SerializedName("messageList")
    @Expose
    public ArrayList<MessageList> messageList = null;
    public final static Parcelable.Creator<MessageHistory> CREATOR = new Creator<MessageHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MessageHistory createFromParcel(Parcel in) {
            return new MessageHistory(in);
        }

        public MessageHistory[] newArray(int size) {
            return (new MessageHistory[size]);
        }

    }
            ;

    protected MessageHistory(Parcel in) {
        this.success = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.messageList, (com.rydz.driver.model.message.MessageList.class.getClassLoader()));
    }

    public MessageHistory() {
    }

    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    public ArrayList<MessageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageList> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", success).append("messageList", messageList).toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeList(messageList);
    }

    public int describeContents() {
        return 0;
    }

}
