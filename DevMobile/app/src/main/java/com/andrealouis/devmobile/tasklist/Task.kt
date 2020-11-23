package com.andrealouis.devmobile.tasklist

data class Task(private val id:String,  private val title:String, private val description:String = "-")
{
    override fun toString(): String {
        return title + "\n" + description
    }
}