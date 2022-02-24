package org.devjj.platform.nurbanhoney.features

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.core.platform.KParcelable
import org.devjj.platform.nurbanhoney.core.platform.parcelableCreator

data class Board(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String
) : KParcelable {
    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Board)

        val empty = Board(-1, "Board doesn't exist", "")
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(name)
            writeString(address)
        }
    }
}