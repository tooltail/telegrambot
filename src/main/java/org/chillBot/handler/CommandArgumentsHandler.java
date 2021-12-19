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

    /**
     * Number of arguments for function
     */
    private Integer countCommandArguments;

    /**
     * Counter for amount arguments
     */
    private Integer currentCommandArgument = 0;

    private Place place;

    private Controller controller;

    /**
     * Sets one of the commands from enum Command
     */
    private Command currentCommand;

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

    /**
     * Get coordinates of the place
     * @param location
     * @return
     */
    public Location getLocation(String location) {
        Location coordinates;
        if (!location.contains("coordinates:")) {
            coordinates = new Location();
            coordinates.findPlaceLonLat(location);
        }
        else {
            coordinates = new Location(Double.parseDouble(location.split(" ")[1]),
                                       Double.parseDouble(location.split(" ")[2]));
        }
        currentCommandArgument++;
        return coordinates;
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
