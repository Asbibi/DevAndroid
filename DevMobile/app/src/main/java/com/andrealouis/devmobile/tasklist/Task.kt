package com.andrealouis.devmobile.tasklist

data class Task(val id: String, val title: String, val description: String = "-")
{
    /*
    override fun toString(): String {
        return title + "\n" + description
    }*/
}