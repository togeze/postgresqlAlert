import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.PostgreSQLAlertBot
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
import java.util.*


fun main() {
    val url = "jdbc:postgresql://localhost:5432/mestKom"
    val username = "postgres"
    val password = "123"
    var isConnected = false
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, username, password)
        isConnected = true

    } catch (e: Exception){
        isConnected = false
    }



    var bot = PostgreSQLAlertBot(isConnected, connection).createBot()
    bot.startPolling()
    alert(15) {
        checkBd(connection, bot)
    }

}
 fun alert(interval: Long, task: () -> Unit) {
    val timer = Timer()
    val timerTask = object : TimerTask() {
        override fun run() {
            task.invoke()
        }
    }
    timer.scheduleAtFixedRate(timerTask, 0, interval)
}

fun checkBd(connection: Connection?, bot: Bot) {
    if (connection != null) {
//        val query = "SELECT * FROM pg_stat_activity WHERE state = 'idle in transaction'"
//        val statement = connection.createStatement()
//        val resultSet = statement.executeQuery(query)
//
//        val listActivites = mutableListOf<String>()
//        var str: String = "Ошибок не найдено"
//        if (resultSet.next()) {
//
//            val eresultSet = resultSet.getString("query")
//            str = "Ошибка: $eresultSet"
//        }
//        listActivites.add(str)
//        resultSet.close()
//        statement.close()
//        bot.sendMessage(
//            chatId = ChatId.fromId(),
//            text = "inline buttons",
//            replyMarkup = inlineKeyboardMarkup,
//        )
    }
}