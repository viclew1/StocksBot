package fr.lewon.bot.stocks.dto.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class UserInfo(
        @field:JsonProperty val email: String = "",
        @field:JsonProperty val password: String = "",
        @field:JsonProperty("_ctoken") val cToken: String = ""
)