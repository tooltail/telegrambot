package org.chillBot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.controller.TelegramController;
import org.chillBot.controller.VkController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Entry point
 */
public class Main {
    public static void main(String[] args) throws TelegramApiException, ClientException, InterruptedException, ApiException {
        TelegramController telegramController = new TelegramController();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramController);
        VkController vkController = new VkController();
        vkController.startupController();
    }
}
