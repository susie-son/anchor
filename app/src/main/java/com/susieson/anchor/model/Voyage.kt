package com.susieson.anchor.model

data class Voyage(
    val title: String = "",
    val description: String? = null,
    val thoughts: List<String>? = null,
    val interpretations: List<String>? = null,
    val behaviors: List<String>? = null,
    val actions: List<String>? = null
)