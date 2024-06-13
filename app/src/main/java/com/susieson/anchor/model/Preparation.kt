package com.susieson.anchor.model

data class Preparation(
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList(),
)
