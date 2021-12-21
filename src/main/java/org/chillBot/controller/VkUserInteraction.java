package org.chillBot.controller;

import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;
import org.chillBot.handler.CommandHandler;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import api.longpoll.bots.LongPollBot;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Interaction with user in Vkontakte
 */
public class VkUserInteraction extends LongPollBot implements Interaction {

    private VkApiClient vk;
    private GroupActor actor;
    private CommandHandler commandHandler;
    private Integer chatId;
    private static Integer groupId;
    private static String botToken;
    private Message message;

    public static void setGroupId(Integer groupId) {
        VkUserInteraction.groupId = groupId;
    }

    public static void setBotToken(String botToken) {
        VkUserInteraction.botToken = botToken;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public VkUserInteraction() throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(groupId, botToken);
        commandHandler = new CommandHandler();
    }

    @Override
    public String getAccessToken() {
        return botToken;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    /**
     * Checks whether update was made by user (query of user)
     */
    @Override
    public void onMessageNew(MessageNew messageNew) {
        message = messageNew.getMessage();
        if (message.hasText()){
            try {
                VkUserInteraction userInteraction = new VkUserInteraction();
                userInteraction.setChatId(message.getFromId());
                commandHandler.processInput(message.getText(), userInteraction);
            } catch (TelegramApiException | SQLException | ApiException | ClientException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Builds and sends message to user
     */
    @Override
    public void sendMessageToUser(String text) throws ApiException, ClientException {
        vk.messages().send(actor).message(text).peerId(Math.toIntExact(chatId)).randomId(new Random().nextInt(10000)).execute();
    }

    private List<KeyboardButton> addRateButtonsToRow() {
        List<KeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            KeyboardButton rateButton = new KeyboardButton();
            KeyboardButtonAction action = new KeyboardButtonAction();
            rateButton.setAction(action.setLabel(String.valueOf(i)).setType(TemplateActionTypeNames.TEXT));
            keyboardButtonsRow.add(rateButton);
        }
        return keyboardButtonsRow;
    }

    /**
     * Send keyboard with rating buttons to user
     */
    @Override
    public void requestRate() throws ClientException, ApiException {
        List<List<KeyboardButton>> buttons = new ArrayList<>();
        Keyboard keyboard = new Keyboard().setInline(true);
        List<KeyboardButton> keyboardButtonsRow = addRateButtonsToRow();
        buttons.add(keyboardButtonsRow);
        keyboard.setButtons(buttons);
        vk.messages().send(actor).message("Rate the establishment").peerId(Math.toIntExact(chatId)).randomId(new Random().nextInt(10000)).keyboard(keyboard).execute();
    }

    /**
     * Send button to show more bars
     */
    @Override
    public void requestMoreBars() throws ClientException, ApiException {
        List<List<KeyboardButton>> buttons = new ArrayList<>();
        List<KeyboardButton> keyboardButtonsRow = new ArrayList<>();
        Keyboard keyboard = new Keyboard().setInline(true);
        KeyboardButtonAction action = new KeyboardButtonAction();
        keyboardButtonsRow.add(new KeyboardButton().setAction(action.setLabel("/moreBars").setType(TemplateActionTypeNames.TEXT)));
        buttons.add(keyboardButtonsRow);
        keyboard.setButtons(buttons);
        vk.messages().send(actor).message("Show more bars").peerId(Math.toIntExact(chatId)).randomId(new Random().nextInt(10000)).keyboard(keyboard).execute();
    }
}