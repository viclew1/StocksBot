package fr.lewon.bot.stocks.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import fr.lewon.bot.stocks.dto.auth.UserInfo
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class StocksRequestProcessor {

    private val objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getCToken(loginWebClient: WebClient): String {
        val response = loginWebClient.get()
                .uri("/cloud-login")
                .header("Accept-Language", "fr-FR,fr;q=0.9,en-GB;q=0.8,en;q=0.7,en-US;q=0.6")
                .header("Connection", "keep-alive")
                .header("Host", "iexcloud.io")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .exchange()
                .block()
        try {
            val setCookieHeader = response
                    ?.headers()
                    ?.asHttpHeaders()
                    ?.getFirst("Set-Cookie")
                    ?: throw Exception("Failed to retrieve Set-Cookie header")

            val matchResult = Regex("ctoken=(.*?);").find(setCookieHeader)
            return matchResult?.destructured?.component1() ?: throw Exception("CToken not found in Set-Cookie header")
        } finally {
            response?.releaseBody()
        }
    }

    fun cloudLogin(loginWebClient: WebClient, cToken: String) {
        loginWebClient.get()
                .uri("/build/cloud-login.js")
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "fr-FR,fr;q=0.9,en-GB;q=0.8,en;q=0.7,en-US;q=0.6")
                .header("Connection", "keep-alive")
                .header("Cookie", "ctoken=$cToken")
                .header("Host", "iexcloud.io")
                .header("Referer", "https://iexcloud.io/cloud-login")
                .header("Sec-Fetch-Dest", "script")
                .header("Sec-Fetch-Mode", "no-cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .retrieve()
                .toBodilessEntity()
                .block()
    }

    fun getSession(login: String, password: String, cToken: String, loginWebClient: WebClient): StocksSession {
        val ga = "GA1.2.35536147.1585917650"
        val gid = "GA1.2.1197708899.1585917650"
        val authResponse = loginWebClient.post()
                .uri("/cloud-login/auth")
                .headers {
                    it.clear()
                    it.add("Accept", "application/json")
                    it.add("Accept-Encoding", "gzip, deflate, br")
                    it.add("Accept-Language", "fr-FR,fr;q=0.9,en-GB;q=0.8,en;q=0.7,en-US;q=0.6")
                    it.add("Connection", "keep-alive")
                    it.add("Content-Type", "application/json")
                    it.add("Host", "iexcloud.io")
                    it.add("Origin", "https://iexcloud.io")
                    it.add("Referer", "https://iexcloud.io/cloud-login")
                    it.add("Sec-Fetch-Dest", "empty")
                    it.add("Sec-Fetch-Mode", "cors")
                    it.add("Sec-Fetch-Site", "same-origin")
                    it.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                }
                .bodyValue(objectMapper.writeValueAsString(UserInfo(login, password, cToken)))
                .retrieve()
                .bodyToMono<String>()
                .block()
        println(authResponse)
        return StocksSession()
    }

}