package com.github.kotlintelegrambot.dispatcher

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.logging.LogLevel
import com.github.kotlintelegrambot.network.fold
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.sql.Connection

private const val TOKEN = "6700628435:AAHH3o5EciDRrurO7s_jARQROJJqGHUx0nA"

class PostgreSQLAlertBot(val isConnection: Boolean, val connect: Connection?) {
    private var chatId: ChatId.Id? = null
    private val isConnected = isConnection
    private val connection: Connection? = connect

    fun createBot(): Bot {
        return bot {
            token = TOKEN
            timeout = 30
            logLevel = LogLevel.Network.Body

            dispatch {
                runBlocking {
                    launch {
                        startCommand()
                        setUpCallbacks()
                    }

                }
            }
        }
    }

    private fun Dispatcher.setUpCallbacks() {
        callbackQuery("state") {
            val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
            if(connection != null){
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
                bot.sendMessage(ChatId.fromId(chatId), listActivites.toString())
            } else{
                bot.sendMessage(ChatId.fromId(chatId), "Соединение отсутсвует")
            }

        }
    }

    private fun Dispatcher.startCommand() {
        command("start") {
            val result = bot.sendMessage(
                chatId = ChatId.fromId(update.message!!.chat.id),
                text = "Привет, я бот способный отслеживать состояние баз данных и помогая устранять проблемы "
            )
            result.fold(
                {
                    // do something here with the response
                    val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                        listOf(
                            InlineKeyboardButton.CallbackData(
                                text = "Показать состояния баз данных",
                                callbackData = "state"
                            )
                        )
                    )
                    bot.sendMessage(
                        chatId = ChatId.fromId(message.chat.id),
                        text = "inline buttons",
                        replyMarkup = inlineKeyboardMarkup,
                    )
                },
                {
                    // do something with the error
                }
            )
        }
    }

}

