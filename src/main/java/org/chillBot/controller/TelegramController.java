package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.handler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Interaction with user in Telegram
 */
public class TelegramController extends TelegramLongPollingBot implements Controller {

    private Long chatId;
    private CommandHandler commandHandler;
    private Message message;

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public TelegramController () {
        commandHandler = new CommandHandler();
    }

    public void sendMessageToUser(String text) throws TelegramApiException {
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build());
    }

    public void requestLocation() throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setRequestLocation(true);
        keyboardButton.setText("Share location");
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(keyboardButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Share your location, pls :)")
                .replyMarkup(keyboardMarkup)
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
            message = update.getMessage();
            try {
                TelegramController telegramController = new TelegramController();
                telegramController.setChatId(message.getChatId());
                if (message.hasLocation()) {
                    String msg = String.format("%s %s", message.getLocation().getLongitude(), message.getLocation().getLatitude());
                    commandHandler.processInput(msg, telegramController);
                }
                else {
                    commandHandler.processInput(message.getText(), telegramController);
                }
            } catch (ClientException | ApiException | SQLException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
