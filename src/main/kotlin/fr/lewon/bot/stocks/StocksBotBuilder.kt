package fr.lewon.bot.stocks

import fr.lewon.bot.runner.AbstractBotBuilder
import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.task.BotTask
import fr.lewon.bot.runner.session.AbstractSessionManager
import fr.lewon.bot.stocks.rest.StocksSessionManager
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.function.client.WebClient

class StocksBotBuilder : AbstractBotBuilder(listOf(), listOf()) {

    override fun buildSessionManager(login: String, password: String): AbstractSessionManager {
        return StocksSessionManager(login, password, WebClient.builder()
                .codecs { c -> c.defaultCodecs().maxInMemorySize(-1) })
    }

    override fun getInitialTasks(bot: Bot): List<BotTask> {
        return listOf()
    }

}

@SpringBootApplication(scanBasePackages = ["fr.lewon.bot.runner"])
open class BotManagerApp : CommandLineRunner {

    override fun run(vararg args: String?) {
        val sbb = StocksBotBuilder()
        val bot = sbb.buildBot("viclew@gmail.com", "gGG7XtYZw-SVpsX", HashMap())
        bot.sessionManager.buildSessionHolder()
    }

}

fun main(args: Array<String>) {
    runApplication<BotManagerApp>(*args)
}
