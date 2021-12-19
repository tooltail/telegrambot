package org.chillBot.controller;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Interface for controller, interaction with user with necessary to be implemented methods
 */
public interface Controller {
    void sendMessageToUser(String text) throws ApiException, ClientException, TelegramApiException;
    void requestRate() throws TelegramApiException;
    void requestMoreBars() throws TelegramApiException;
}
