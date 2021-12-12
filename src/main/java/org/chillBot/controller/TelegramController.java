package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.handler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

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

    private List<InlineKeyboardButton> addButtonsToRow(String[] data) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            InlineKeyboardButton rateButton = new InlineKeyboardButton();
            rateButton.setText(data[i]);
            rateButton.setCallbackData(data[i]);
            keyboardButtonsRow.add(rateButton);
        }
        return keyboardButtonsRow;
    }

    public void requestRate() throws TelegramApiException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = addButtonsToRow(new String[] {"1", "2", "3", "4", "5"});
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Rate the establishment")
                .replyMarkup(inlineKeyboardMarkup)
                .build());
    }

    public void requestMoreBars() throws TelegramApiException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton moreButton = new InlineKeyboardButton();
        moreButton.setText("Show more");
        moreButton.setCallbackData("/bars");
        List<InlineKeyboardButton> keyboardButtonRow = new ArrayList<>();
        keyboardButtonRow.add(moreButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Show more bars")
                .replyMarkup(inlineKeyboardMarkup)
                .build());
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
        else if(update.hasCallbackQuery()){
            try {
                TelegramController telegramController = new TelegramController();
                telegramController.setChatId(update.getCallbackQuery().getMessage().getChatId());
                commandHandler.processInput(update.getCallbackQuery().getData(), telegramController);

                long messageId = update.getCallbackQuery().getMessage().getMessageId();
                long chat_id = update.getCallbackQuery().getMessage().getChatId();
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(chat_id));
                deleteMessage.setMessageId(toIntExact(messageId));
                execute(deleteMessage);
            } catch (TelegramApiException | ClientException | ApiException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
