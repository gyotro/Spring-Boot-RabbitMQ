package com.gyo.amqp

import com.fasterxml.jackson.annotation.JsonProperty

data class Fattura constructor(
        @JsonProperty("Fattura")
        val numFatt: Long = 1L,
        @JsonProperty("Cliente")
        val customer: String = "",
        val company: String = "",
        val anno: Int = 0)

