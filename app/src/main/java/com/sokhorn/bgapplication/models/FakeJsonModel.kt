package com.sokhorn.bgapplication.models


import com.google.gson.annotations.SerializedName

class FakeJsonModel : ArrayList<FakeJsonModel.FakeJsonModelItem>(){
    data class FakeJsonModelItem(
        @SerializedName("albumId")
        val albumId: Int = 0,
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("thumbnailUrl")
        val thumbnailUrl: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("url")
        val url: String = ""
    )
}