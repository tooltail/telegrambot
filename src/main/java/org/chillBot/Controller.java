package org.chillBot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Controller {
    void sendMessageToUser(String text) throws ApiException, ClientException, TelegramApiException;
}
