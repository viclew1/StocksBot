package fr.lewon.bot.stocks.rest

import fr.lewon.bot.runner.Bot
import fr.lewon.bot.stocks.dto.SymbolDTO
import fr.lewon.bot.stocks.rest.query.QueryParametersBuilder

class StocksRequestProcessor {

    fun getSymbols(bot: Bot): List<SymbolDTO> {
        val uri = "$REF_DATA$IEX$SYMBOLS"
        return getForResponse<Array<SymbolDTO>>(bot, uri, QueryParametersBuilder())
                ?.toList()
                ?: emptyList()
    }

    private inline fun <reified T> getForResponse(bot: Bot, uri: String, queryParametersBuilder: QueryParametersBuilder): T? {
        val sessionHolder = bot.sessionManager.buildSessionHolder()
        val session = sessionHolder.sessionObject as StocksSession
        val webClient = sessionHolder.webClient
        queryParametersBuilder.addParamIfAbsent("token", getToken(bot, session))
        val uriWithParameters = getBaseUrl(bot) + uri + queryParametersBuilder.build()
        return webClient.get()
                .uri(uriWithParameters)
                .retrieve()
                .bodyToMono(T::class.java)
                .block()
    }

    private fun getToken(bot: Bot, session: StocksSession): String {
        val isTest = bot.botPropertyStore.getByKey("test_mode") as Boolean
        return if (isTest) {
            session.cTokenTest
        } else session.cTokenProd
    }

    private fun getBaseUrl(bot: Bot): String {
        val isTest = bot.botPropertyStore.getByKey("test_mode") as Boolean
        return if (isTest) {
            BASE_URL_TEST
        } else BASE_URL
    }

    companion object {

        const val BASE_URL = "cloud.iexapis.com/stable/"
        const val BASE_URL_TEST = "https://sandbox.iexapis.com/stable/"

        const val REF_DATA = "/ref-data"
        const val IEX = "/IEX"

        const val SYMBOLS = "/symbols"
    }

}