package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Place;
import org.chillBot.controller.Controller;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class AddCommandArgumentsHandler {

    private Integer addCommandArguments = 3;

    private Place place;

    private Controller controller;

    public AddCommandArgumentsHandler(Controller controller) {
        place = new Place();
        this.controller = controller;
    }

    public boolean isEnd() {
        if (addCommandArguments == 0)
            return true;
        return false;
    }

    public void addArgument(String argument) throws TelegramApiException, ClientException, ApiException {
        if (addCommandArguments == 3) {
            place.setType(argument);
            controller.sendMessageToUser("Enter the name of the establishment:");
            addCommandArguments--;
        } else if (addCommandArguments == 2) {
            place.setName(argument);
            controller.sendMessageToUser("Enter the address of the establishment:");
            addCommandArguments--;
        } else if (addCommandArguments == 1) {
            place.setAddress(argument);
            addCommandArguments--;
        }
    }

    public Place getPlace() {
        return place;
    }
}
