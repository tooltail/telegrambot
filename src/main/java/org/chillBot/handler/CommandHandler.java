package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.BotFunction;
import org.chillBot.Command;
import org.chillBot.Location;
import org.chillBot.Place;
import org.chillBot.controller.Interaction;
import org.chillBot.dao.DBPlaceDao;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

/**
 * Process commands
 */
public class CommandHandler {

    /**
     * Counter for amount of arguments
     */
    private Command currentCommand;

    private CommandArgumentsHandler commandArgumentsHandler;

    private BotFunction botFunction;

    private boolean isContextSwitched = true;

    private Integer startIdx = 1;

    private Integer pageSize = 3;

    public CommandHandler() {
        botFunction = new BotFunction(new DBPlaceDao());
    }

    private void updateContext() {
        isContextSwitched = true;
        startIdx = 1;
    }

    /**
     * Handles the commands inputted by user
     */
    public void processInput(String message, Interaction controller) throws TelegramApiException, ClientException, ApiException, SQLException {
        if (message.equals("/add") || currentCommand == Command.addBar) {
            updateContext();
            if (message.equals("/add")) {
                currentCommand = Command.addBar;
                commandArgumentsHandler = new CommandArgumentsHandler(controller, 3, currentCommand);
                controller.sendMessageToUser("Select the category to which you want to add the establishment:");
            }
            else if (!commandArgumentsHandler.isEnd()) {
                commandArgumentsHandler.addArgument(message);
            }
            if (commandArgumentsHandler.isEnd()) {
                Place place = commandArgumentsHandler.getPlace();
                if (botFunction.addPlace(place)) {
                    controller.sendMessageToUser("Bar added to database\nYou can check list of bars. Type /bars");
                }
                else {
                    controller.sendMessageToUser("This place was already added");
                }
                currentCommand = null;
            }
        }
        else if (message.equals("/bars") || message.equals("/moreBars")) {
            List<String> bars = botFunction.getPlaces(startIdx, startIdx + pageSize);
            if (bars.size() != 0 || !isContextSwitched) {
                if (message.equals("/moreBars") && bars.size() == 0) {
                    controller.sendMessageToUser("No more bars");
                }
                else if (bars.size() == 0) {
                    updateContext();
                    bars = botFunction.getPlaces(startIdx, startIdx + pageSize);
                }
                if (isContextSwitched) {
                    controller.sendMessageToUser("List of bars:");
                    isContextSwitched = false;
                }
                for (String bar : bars) {
                    controller.sendMessageToUser(bar);
                }
                if (bars.size() == pageSize) {
                    controller.requestMoreBars();
                }
                startIdx += pageSize;
            } else {
                controller.sendMessageToUser("No bars added yet");
            }
        }
        else if (message.equals("/rate") || currentCommand == Command.rateBar){
            updateContext();
            if (message.equals("/rate")) {
                currentCommand = Command.rateBar;
                commandArgumentsHandler = new CommandArgumentsHandler(controller, 4, currentCommand);
                controller.sendMessageToUser("Select the category in which you want to rate the establishment:");
            }
            else if (!commandArgumentsHandler.isEnd()) {
                commandArgumentsHandler.addArgument(message);
            }
            if (commandArgumentsHandler.isEnd()) {
                Place place = commandArgumentsHandler.getPlace();
                if (botFunction.addRate(place)) {
                    controller.sendMessageToUser("Your rate was added ;)");
                }
                else {
                    controller.sendMessageToUser("Bar not found. Type /add to add the bar");
                }
                currentCommand = null;
            }
        }
        else if (message.equals("/nearestBars") || currentCommand == Command.location) {
            Location userLocation = null;
            if (message.equals("/nearestBars")) {
                controller.sendMessageToUser("Please share your geolocation or enter your current address");
                currentCommand = Command.location;
                commandArgumentsHandler = new CommandArgumentsHandler(controller, 1, currentCommand);
            }
            else if (!commandArgumentsHandler.isEnd()) {
                userLocation = commandArgumentsHandler.getLocation(message);
            }
            if (commandArgumentsHandler.isEnd()) {
                List<String> bars = botFunction.getNearestPlaces(userLocation);
                if (bars.size() == 0) {
                    controller.sendMessageToUser("There are no bars near you");
                } else {
                    controller.sendMessageToUser("Nearest bars:");
                    for (String bar : bars) {
                        controller.sendMessageToUser(bar);
                    }
                }
            }
        }
    }
}
