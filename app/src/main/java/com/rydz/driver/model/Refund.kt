package com.rydz.driver.model

import android.os.Parcel
import android.os.Parcelable

data class Refund(
	val reason: String? = null,
	val description: String? = null,
	val refundAmount: Double? = null,
	val status: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Double::class.java.classLoader) as? Double,
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(reason)
		parcel.writeString(description)
		parcel.writeValue(refundAmount)
		parcel.writeString(status)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Refund> {
		override fun createFromParcel(parcel: Parcel): Refund {
			return Refund(parcel)
		}

		override fun newArray(size: Int): Array<Refund?> {
			return arrayOfNulls(size)
		}
	}


}
