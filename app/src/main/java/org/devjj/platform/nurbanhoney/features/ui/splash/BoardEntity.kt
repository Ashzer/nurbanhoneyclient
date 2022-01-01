package org.devjj.platform.nurbanhoney.features.ui.splash

import com.google.gson.annotations.SerializedName

data class BoardEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String
) {
    fun toBoard() = Board(id, name, address)


    companion object {
        val empty = BoardEntity(-1, "", "")
    }
}