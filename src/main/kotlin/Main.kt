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

}