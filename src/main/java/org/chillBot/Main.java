package org.chillBot;

import api.longpoll.bots.exceptions.VkApiException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Loader.DeployedVersionConfigLoader;
import org.chillBot.Loader.LocalVersionConfigLoader;
import org.chillBot.controller.TelegramController;
import org.chillBot.controller.VkController;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

/**
 * Entry point
 */
public class Main {
    public static void main(String[] args) throws TelegramApiException, ClientException, InterruptedException, ApiException, IOException, VkApiException {
        if (args.length != 0 && args[0].equals("deploy")) {
            DeployedVersionConfigLoader configLoader = new DeployedVersionConfigLoader();
            configLoader.loadEnvValues();
        }
        else {
            LocalVersionConfigLoader configLoader = new LocalVersionConfigLoader();
            configLoader.loadPropValues();
        }
        TelegramController telegramController = new TelegramController();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramController);
        new VkController().startPolling();
    }
}
