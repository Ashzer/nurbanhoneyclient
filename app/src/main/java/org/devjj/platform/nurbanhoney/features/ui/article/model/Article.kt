package org.devjj.platform.nurbanhoney.features.ui.article.model

import android.os.Parcel
import org.devjj.platform.nurbanhoney.core.extension.empty
import org.devjj.platform.nurbanhoney.core.platform.KParcelable
import org.devjj.platform.nurbanhoney.core.platform.parcelableCreator

data class Article(
    val id: Int,
    val uuid: String,
    val thumbnail: String,
    val title: String,
    val lossCut: Int?,
    val content: String,
    val inquiries: Int,
    val comments: Int,
    val likes: Int,
    val dislikes: Int,
    val date: String,
    val userId: Int,
    val badge: String,
    val nickname: String,
    val insignia: String,
    val myRating: String,
) : KParcelable {

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Article)
        val empty = Article(-1,String.empty(),String.empty(),String.empty(),-1,String.empty(),0,0,0,0,String.empty(),0,String.empty(),String.empty(),String.empty(),String.empty())
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as Int?,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        with(dest) {
            writeInt(id)
            writeString(uuid)
            writeString(thumbnail)
            writeString(title)
            writeValue(lossCut)
            writeString(content)
            writeInt(inquiries)
            writeInt(comments)
            writeInt(likes)
            writeInt(dislikes)
            writeString(date)
            writeInt(userId)
            writeString(badge)
            writeString(nickname)
            writeString(insignia)
            writeString(myRating)
        }
    }
}