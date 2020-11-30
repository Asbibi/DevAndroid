package com.andrealouis.devmobile.tasklist

import java.io.Serializable

data class Task(val id: String, val title: String, val description: String = "-") : Serializable
{
    /*
    override fun toString(): String {
        return title + "\n" + description
    }*/
}