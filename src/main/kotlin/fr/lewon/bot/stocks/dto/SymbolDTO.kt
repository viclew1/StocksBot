package fr.lewon.bot.stocks.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SymbolDTO(
        @field:JsonProperty val symbol: String = "",
        @field:JsonProperty val date: Date = Date(),
        @field:JsonProperty val isEnabled: Boolean = false
)