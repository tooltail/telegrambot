package org.chillBot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;

public class AddCommandArgumentsParser {

    private Integer addCommandArguments = 3;

    private Place place;

    private Controller controller;

    public AddCommandArgumentsParser(Controller controller) {
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
