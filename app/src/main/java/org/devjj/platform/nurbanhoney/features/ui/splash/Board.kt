package org.devjj.platform.nurbanhoney.features.ui.splash

import android.os.Parcel
import org.devjj.platform.nurbanhoney.core.platform.KParcelable
import org.devjj.platform.nurbanhoney.core.platform.parcelableCreator

data class Board(val id: Int, val name: String, val address: String) : KParcelable {
    companion object{
        @JvmField
        val CREATOR = parcelableCreator(::Board)
    }

    constructor(parcel : Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest){
            writeInt(id)
            writeString(name)
            writeString(address)
        }
    }
}