package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Interface for controller, interaction with user with necessary to be implemented methods
 */
public interface Controller {

    /**
     * Builds and sends message to user
     * @param text
     * @throws ApiException
     * @throws ClientException
     * @throws TelegramApiException
     */
    void sendMessageToUser(String text) throws ApiException, ClientException, TelegramApiException;

    /**
     * Send keyboard with rating buttons to user
     * @throws TelegramApiException
     */
    void requestRate() throws TelegramApiException;

    /**
     * Send button to show more bars
     * @throws TelegramApiException
     */
    void requestMoreBars() throws TelegramApiException;
}
