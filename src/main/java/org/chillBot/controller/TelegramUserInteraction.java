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
public class TelegramUserInteraction extends TelegramLongPollingBot implements Interaction {

    private Long chatId;
    private CommandHandler commandHandler;
    private Message message;

    private static String botUsername;

    private static String botToken;

    public static void setBotToken(String botToken) {
        TelegramUserInteraction.botToken = botToken;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public static void setBotUsername(String botUsername) {
        TelegramUserInteraction.botUsername = botUsername;
    }

    public TelegramUserInteraction() {
        commandHandler = new CommandHandler();
    }

    /**
     * Create Inline keyboard with buttons 1-5
     */
    private List<InlineKeyboardButton> addRateButtonsToRow() {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            InlineKeyboardButton rateButton = new InlineKeyboardButton();
            rateButton.setText(String.valueOf(i));
            rateButton.setCallbackData(String.valueOf(i));
            keyboardButtonsRow.add(rateButton);
        }
        return keyboardButtonsRow;
    }

    /**
     * Send keyboard with rating buttons to user
     */
    public void requestRate() throws TelegramApiException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow = addRateButtonsToRow();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Rate the establishment")
                .replyMarkup(inlineKeyboardMarkup)
                .build());
    }

    /**
     * Send button to show more bars
     */
    public void requestMoreBars() throws TelegramApiException {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton moreButton = new InlineKeyboardButton();
        moreButton.setText("Show more");
        moreButton.setCallbackData("/moreBars");
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

    /**
     * Builds and sends message to user
     */
    public void sendMessageToUser(String text) throws TelegramApiException {
        execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build());
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Checks whether update was made by user (query of user)
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            message = update.getMessage();
            try {
                TelegramUserInteraction telegramInteraction = new TelegramUserInteraction();
                telegramInteraction.setChatId(message.getChatId());
                if (message.hasLocation()) {
                    String msg = String.format("coordinates: %s %s", message.getLocation().getLongitude(), message.getLocation().getLatitude());
                    commandHandler.processInput(msg, telegramInteraction);
                }
                else {
                    commandHandler.processInput(message.getText(), telegramInteraction);
                }
            } catch (ClientException | ApiException | SQLException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if(update.hasCallbackQuery()){
            try {
                TelegramUserInteraction telegramInteraction = new TelegramUserInteraction();
                telegramInteraction.setChatId(update.getCallbackQuery().getMessage().getChatId());
                commandHandler.processInput(update.getCallbackQuery().getData(), telegramInteraction);

                long callbackMessageId = update.getCallbackQuery().getMessage().getMessageId();
                long callbackChatId = update.getCallbackQuery().getMessage().getChatId();
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(callbackChatId));
                deleteMessage.setMessageId(toIntExact(callbackMessageId));
                execute(deleteMessage);
            } catch (TelegramApiException | ClientException | ApiException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
