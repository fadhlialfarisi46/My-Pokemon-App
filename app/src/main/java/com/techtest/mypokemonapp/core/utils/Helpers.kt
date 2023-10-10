package com.techtest.mypokemonapp.core.utils

object Helpers {

    fun convertURLtoId(url: String): String {
        val indexOfId = 6
        val segments = url.split("/")
        return segments[indexOfId]
    }
}
