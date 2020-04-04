package fr.lewon.bot.stocks.rest

import com.fasterxml.jackson.annotation.JsonProperty

data class StocksSession(@field: JsonProperty var token: String = "")