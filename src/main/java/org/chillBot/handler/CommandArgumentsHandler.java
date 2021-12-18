package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Command;
import org.chillBot.Location;
import org.chillBot.Place;
import org.chillBot.controller.Controller;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Process command arguments
 */
public class CommandArgumentsHandler {

    private Integer countCommandArguments;

    private Integer currentCommandArgument = 0;

    private Place place;

    private Controller controller;

    private Command currentCommand;

    public CommandArgumentsHandler() {}

    public CommandArgumentsHandler(Controller controller, Integer countCommandArguments, Command currentCommand) {
        place = new Place();
        this.controller = controller;
        this.countCommandArguments = countCommandArguments;
        this.currentCommand = currentCommand;
    }

    /**
     * Check if all the arguments was inputted
     */
    public boolean isEnd() {
        if (countCommandArguments == currentCommandArgument)
            return true;
        return false;
    }

    public Location getLocation(String location) {
        String[] coordinates = location.split(" ");
        currentCommandArgument++;
        return new Location(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }

    /**
     * Forms class Place by adding all inputted arguments
     * @param argument
     * @throws TelegramApiException
     * @throws ClientException
     * @throws ApiException
     */
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
            if (currentCommand == Command.rateBar) {
                controller.requestRate();
            }
            currentCommandArgument++;
        } else if (currentCommandArgument == 3){
            controller.sendMessageToUser(String.format("%s", argument));
            Double rate = Double.parseDouble(argument);
            if (rate >= 1 && rate <= 5) {
                place.setRate(rate);
                currentCommandArgument++;
            }
            else {
                controller.sendMessageToUser("The rate must be between 1 and 5");
            }
        }
    }

    /**
     * Returns already formed class Place
     */
    public Place getPlace() {
        return place;
    }
}
