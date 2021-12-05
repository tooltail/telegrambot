package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Place;
import org.chillBot.controller.Controller;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class CommandArgumentsHandler {

    private Integer countCommandArguments;

    private Integer currentCommandArgument = 0;

    private Place place;

    private Controller controller;

    public CommandArgumentsHandler(Controller controller, Integer countCommandArguments) {
        place = new Place();
        this.controller = controller;
        this.countCommandArguments = countCommandArguments;
    }

    public boolean isEnd() {
        if (countCommandArguments == currentCommandArgument)
            return true;
        return false;
    }

    public void addArgument(String argument) throws TelegramApiException, ClientException, ApiException {
        if (currentCommandArgument == 0) {
            place.setType(argument);
            controller.sendMessageToUser("Enter the name of the establishment:");
            currentCommandArgument++;
        } else if (currentCommandArgument == 1) {
            place.setName(argument);
            controller.sendMessageToUser("Enter the address of the establishment:");
            currentCommandArgument++;
        } else if (currentCommandArgument == 2) {
            place.setAddress(argument);
            currentCommandArgument++;
        }
        else if (currentCommandArgument == 3){
            controller.sendMessageToUser("Rate the establishment (between 1 and 5):");
            place.setRate(Double.parseDouble(argument));
            currentCommandArgument++;
        }
    }

    public Place getPlace() {
        return place;
    }
}
