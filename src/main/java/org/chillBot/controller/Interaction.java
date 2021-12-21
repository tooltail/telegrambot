package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Interaction with user with necessary to be implemented methods
 */
public interface Interaction {

    /**
     * Builds and sends message to user
     */
    void sendMessageToUser(String text) throws ApiException, ClientException, TelegramApiException;

    /**
     * Send keyboard with rating buttons to user
     */
    void requestRate() throws TelegramApiException, ClientException, ApiException;

    /**
     * Send button to show more bars
     */
    void requestMoreBars() throws TelegramApiException, ClientException, ApiException;
}
