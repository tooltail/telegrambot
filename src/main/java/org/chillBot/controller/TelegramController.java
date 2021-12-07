package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.handler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

/**
 * Interaction with user in Telegram
 */
public class TelegramController extends TelegramLongPollingBot implements Controller {

    private Long chatId;
    private CommandHandler commandHandler;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public TelegramController () {
        commandHandler = new CommandHandler();
    }

    public void sendMessageToUser(String message) throws TelegramApiException {
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(message)
                .build());
    }

    @Override
    public String getBotUsername() {
        return "@FindChillPlaceBot";
    }

    @Override
    public String getBotToken() {
        return "2029119217:AAEyuWfxXOStemuHquNZFZ8vuQxIyFihHTE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            try {
                TelegramController telegramController = new TelegramController();
                telegramController.setChatId(message.getChatId());
                commandHandler.processInput(message.getText(), telegramController);
            } catch (ClientException | ApiException | SQLException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
