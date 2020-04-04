package fr.lewon.bot.stocks.tasks

import fr.lewon.bot.runner.Bot
import fr.lewon.bot.runner.bot.task.BotTask
import fr.lewon.bot.runner.bot.task.Delay
import fr.lewon.bot.runner.bot.task.TaskResult
import fr.lewon.bot.stocks.rest.StocksRequestProcessor
import java.util.concurrent.TimeUnit

class TestTask(bot: Bot) : BotTask("Test task", bot) {

    override fun doExecute(): TaskResult {
        val requestProcessor = StocksRequestProcessor()

        for (s in requestProcessor.getSymbols(bot)) {
            logger.info("SYMBOL  = ${s.symbol}")
        }

        logger.info("Tests done, doing them again in 1 minute")
        return TaskResult(Delay(1, TimeUnit.HOURS))
    }

}