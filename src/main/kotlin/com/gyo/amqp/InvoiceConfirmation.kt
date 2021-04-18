package com.gyo.amqp

import com.fasterxml.jackson.annotation.JsonProperty

data class InvoiceConfirmation constructor(
        @JsonProperty("Fattura")
        val numFatt: Long,
        @JsonProperty("StatoFattura")
        val Status: String)
