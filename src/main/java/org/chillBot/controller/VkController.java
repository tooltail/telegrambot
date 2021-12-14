package org.chillBot.controller;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.chillBot.handler.CommandHandler;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

/**
 * Interaction with user in Vkontakte
 */
public class VkController implements Controller {

    private VkApiClient vk;
    private Integer ts;
    private GroupActor actor;
    private CommandHandler commandHandler;
    private Integer chatId;

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public VkController() throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(171475067, "433985e4d2ff9a987d05e893a1eb03940cf5026c345c525cfe0756577156ae07d6d1189d2025227b6985f");
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
        commandHandler = new CommandHandler();
    }

    @Override
    public void sendMessageToUser(String text) throws ApiException, ClientException {
        vk.messages().send(actor).message(text).userId(chatId).randomId(new Random().nextInt(10000)).execute();
    }

    @Override
    public void requestRate() {

    }

    @Override
    public void requestMoreBars() {

    }

    public void startupController() throws ClientException, ApiException, InterruptedException {
        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    try {
                        VkController vkController = new VkController();
                        vkController.setChatId(message.getFromId());
                        commandHandler.processInput(message.getText(), vkController);
                    } catch (TelegramApiException | SQLException | ApiException | ClientException e) {
                        e.printStackTrace();
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}