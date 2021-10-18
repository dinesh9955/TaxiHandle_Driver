package com.rydz.driver.model.socketResponse;

import android.os.Parcel;
import android.os.Parcelable;

public class Response  implements Parcelable {
	private double fairPerKm;
	private String slabPerKm;
	private double fareChangekm;
	private double price;


	protected Response(Parcel in) {
		fairPerKm = in.readDouble();
		slabPerKm = in.readString();
		fareChangekm = in.readDouble();
		price = in.readDouble();
	}

	public static final Creator<Response> CREATOR = new Creator<Response>() {
		@Override
		public Response createFromParcel(Parcel in) {
			return new Response(in);
		}

		@Override
		public Response[] newArray(int size) {
			return new Response[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(fairPerKm);
		dest.writeString(slabPerKm);
		dest.writeDouble(fareChangekm);
		dest.writeDouble(price);
	}
}
