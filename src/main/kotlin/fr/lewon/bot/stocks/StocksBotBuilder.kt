package fr.lewon.bot.stocks

import fr.lewon.bot.runner.AbstractBotBuilder
import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.props.BotPropertyDescriptor
import fr.lewon.bot.runner.bot.props.BotPropertyStore
import fr.lewon.bot.runner.bot.props.BotPropertyType
import fr.lewon.bot.runner.bot.task.BotTask
import fr.lewon.bot.runner.session.AbstractSessionManager
import fr.lewon.bot.stocks.rest.StocksSessionManager
import fr.lewon.bot.stocks.tasks.TestTask
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

class StocksBotBuilder : AbstractBotBuilder(
        expectedLoginProperties = listOf(
                BotPropertyDescriptor("CToken_PROD", BotPropertyType.STRING, null, "Token used on the production API. You can find it on your iexcloud user page.", isNeeded = true, isNullable = false),
                BotPropertyDescriptor("CToken_TEST", BotPropertyType.STRING, null, "Token used on the sandbox API. You can find it on your iexcloud user page.", isNeeded = true, isNullable = false)
        ),
        botPropertyDescriptors = listOf(
                BotPropertyDescriptor("test_mode", BotPropertyType.BOOLEAN, true, "Defines if the API used is the sandbox or the production one. true by default", isNeeded = false, isNullable = false, acceptedValues = listOf(true, false))
        ),
        botOperations = listOf()) {

    override fun buildSessionManager(login: String, loginPropertyStore: BotPropertyStore): AbstractSessionManager {
        return StocksSessionManager(login, loginPropertyStore, WebClient.builder()
                .codecs { c -> c.defaultCodecs().maxInMemorySize(-1) }
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
    }

    override fun getInitialTasks(bot: Bot): List<BotTask> {
        return listOf(
                TestTask(bot)
        )
    }

}