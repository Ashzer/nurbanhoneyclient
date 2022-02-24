package org.devjj.platform.nurbanhoney.features.network.entities

import com.google.gson.annotations.SerializedName
import org.devjj.platform.nurbanhoney.features.Board

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