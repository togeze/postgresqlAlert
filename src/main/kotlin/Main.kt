import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.photos
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.dice.DiceEmoji
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.network.fold
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Connection
import java.sql.DriverManager


fun main() {
    val url = "jdbc:postgresql://localhost:5432/mestKom"
    val username = "postgres"
    val password = "123"
    val connection: Connection = DriverManager.getConnection(url, username, password)

    val query = "SELECT pid, state FROM pg_stat_activity"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    val listActivites = mutableListOf<String>()

    while (resultSet.next()) {
        val processId: Int = resultSet.getInt("pid")
        val state: String? = resultSet.getString("state")
        val str: String = "Process ID: $processId, State: $state"
        listActivites.add(str)
    }

    resultSet.close()
    statement.close()
    connection.close()

    val bot = bot {
        token = "6700628435:AAHH3o5EciDRrurO7s_jARQROJJqGHUx0nA"
        timeout = 30
        logLevel = LogLevel.Network.Body

        dispatch {
            command("start") {
                val result = bot.sendMessage(chatId = ChatId.fromId(update.message!!.chat.id), text = "Bot started")

                result.fold(
                    {
                        //bot.sendDice(chatId = ChatId.fromId(update.message!!.chat.id), DiceEmoji.Basketball)
                        bot.sendMessage(
                            chatId = ChatId.fromId(update.message!!.chat.id),
                            "Для получения состояния и активности базы данных местком введите комманду /pg_stat_activity"
                        )
                    },
                    {
                    },
                )
            }

            command("pg_stat_activity") {

                runBlocking {
                    launch {
                        val result =
                            bot.sendMessage(
                                chatId = ChatId.fromId(update.message!!.chat.id),
                                text = "База данных местком онлайн"
                            )

                        result.fold(
                            {
                                var string: String = ""
                                for (str in listActivites) {
                                    bot.sendMessage(
                                        chatId = ChatId.fromId(update.message!!.chat.id),
                                        str
                                    )
                                }


                            },
                            {
                            },
                        )
                    }
                }


            }
        }

    }
    bot.startPolling()
}


private suspend fun pgStatActivity(bot: Bot) {

}
