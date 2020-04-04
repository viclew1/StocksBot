package fr.lewon.bot.stocks.rest

import fr.lewon.bot.runner.bot.props.BotPropertyStore
import fr.lewon.bot.runner.session.AbstractSessionManager
import org.springframework.web.reactive.function.client.WebClient

class StocksSessionManager(login: String, loginPropertyStore: BotPropertyStore, webClientBuilder: WebClient.Builder) : AbstractSessionManager(login, loginPropertyStore, 2 * 3600 * 1000, webClientBuilder) {

    override fun generateSessionObject(webClient: WebClient, login: String, loginPropertyStore: BotPropertyStore): Any {
        val cTokenProd = loginPropertyStore.getByKey("CToken_PROD") as String
        val cTokenTest = loginPropertyStore.getByKey("CToken_TEST") as String
        return StocksSession(cTokenProd = cTokenProd, cTokenTest = cTokenTest)
    }

}