package com.andrealouis.devmobile.task


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Task (
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "-") : java.io.Serializable
{
    /*
    override fun toString(): String {
        return title + "\n" + description
    }*/
}