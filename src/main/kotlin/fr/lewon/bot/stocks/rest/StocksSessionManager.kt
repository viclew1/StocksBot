package fr.lewon.bot.stocks.rest

import fr.lewon.bot.runner.session.AbstractSessionManager
import org.springframework.web.reactive.function.client.WebClient

class StocksSessionManager(login: String, password: String, webClientBuilder: WebClient.Builder) : AbstractSessionManager(login, password, 2 * 3600 * 1000, webClientBuilder) {

    override fun generateSessionObject(webClient: WebClient, login: String, password: String): Any {
        val requestProcessor = StocksRequestProcessor()
        val loginWebClient = WebClient.builder()
                .codecs { c -> c.defaultCodecs().maxInMemorySize(-1) }
                .baseUrl("https://iexcloud.io/")
                .build()

        val cToken = requestProcessor.getCToken(loginWebClient)
        requestProcessor.cloudLogin(loginWebClient, cToken)
        return requestProcessor.getSession(login, password, cToken, loginWebClient)
    }

}