package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Command;
import org.chillBot.Location;
import org.chillBot.Place;
import org.chillBot.AddressLonLatFinder;
import org.chillBot.controller.Interaction;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Takes commands arguments and forms object from these arguments, send messages to user what argument to enter
 */
public class CommandArgumentsHandler {

    /**
     * Number of arguments for function
     */
    private Integer countCommandArguments;

    /**
     * Counter for amount of arguments
     */
    private Integer currentCommandArgument = 0;

    private Place place;

    private Interaction interaction;

    /**
     * Sets one of the commands from enum Command
     */
    private Command currentCommand;

    public CommandArgumentsHandler(Interaction interaction, Integer countCommandArguments, Command currentCommand) {
        place = new Place();
        this.interaction = interaction;
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
     */
    public Location getLocation(String location) {
        Location coordinates;
        if (!location.contains("coordinates:")) {
            AddressLonLatFinder lonLatFinder = new AddressLonLatFinder();
            coordinates = lonLatFinder.getAddressLonLat(location);
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
     */
    public void addArgument(String argument) throws TelegramApiException, ClientException, ApiException {
        if (currentCommandArgument == 0) {
            place.setType(argument);
            interaction.sendMessageToUser("Enter the name of the establishment:");
            currentCommandArgument++;
        } else if (currentCommandArgument == 1) {
            place.setName(argument);
            interaction.sendMessageToUser("Enter the address of the establishment:");
            currentCommandArgument++;
        } else if (currentCommandArgument == 2) {
            place.setAddress(argument);
            if (currentCommand == Command.rateBar) {
                interaction.requestRate();
            }
            currentCommandArgument++;
        } else if (currentCommandArgument == 3){
            interaction.sendMessageToUser(String.format("%s", argument));
            Double rate = Double.parseDouble(argument);
            if (rate >= 1 && rate <= 5) {
                place.setRate(rate);
                currentCommandArgument++;
            }
            else {
                interaction.sendMessageToUser("The rate must be between 1 and 5");
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
