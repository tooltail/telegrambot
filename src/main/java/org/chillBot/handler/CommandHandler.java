package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.BotFunctionality;
import org.chillBot.Command;
import org.chillBot.Location;
import org.chillBot.Place;
import org.chillBot.controller.Controller;
import org.chillBot.dao.DBPlaceDao;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

/**
 * Process commands
 */
public class CommandHandler {

    private Command currentCommand;

    private CommandArgumentsHandler commandArgumentsHandler;

    private BotFunctionality bot;

    public CommandHandler() {
        bot = new BotFunctionality(new DBPlaceDao());
    }

    /**
     * Handles the commands inputted by user
     * @param message
     * @param controller
     * @throws TelegramApiException
     * @throws ClientException
     * @throws ApiException
     * @throws SQLException
     */
    public void processInput(String message, Controller controller) throws TelegramApiException, ClientException, ApiException, SQLException {
        if (message.equals("/add") || currentCommand == Command.addBar) {
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
                if (bot.addPlace(place)) {
                    controller.sendMessageToUser("Bar added to database\nYou can check list of bars. Type /bars");
                }
                else {
                    controller.sendMessageToUser("This place was already added");
                }
                currentCommand = null;
            }
        }
        else if (message.equals("/bars")) {
            List<String> bars = bot.getAllPlaces();
            if (bars.size() == 0) {
                controller.sendMessageToUser("No bars added yet");
            } else {
                controller.sendMessageToUser("List of bars:");
                for (String bar : bars) {
                    controller.sendMessageToUser(bar);
                }
            }
        }
        else if (message.equals("/rate") || currentCommand == Command.rateBar){
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
                if(bot.addRate(place)) {
                    controller.sendMessageToUser("Your rate was added ;)");
                }
                else {
                    controller.sendMessageToUser("Bar not found. Type /add to add the bar");
                }
                currentCommand = null;
            }
        }
        else if (message.equals("/search") || currentCommand == Command.location) {
            Location userLocation = null;
            if (message.equals("/search")) {
                controller.requestLocation();
                currentCommand = Command.location;
                commandArgumentsHandler = new CommandArgumentsHandler(controller, 1, currentCommand);
            }
            else if (!commandArgumentsHandler.isEnd()) {
                userLocation = commandArgumentsHandler.getLocation(message);
                //controller.sendMessageToUser(String.format("%s %s", userLocation.getLatitude(), userLocation.getLongitude()));
            }
            if (commandArgumentsHandler.isEnd()) {
                List<String> bars = bot.getNearestPlace(userLocation);
                if (bars.size() == 0) {
                    controller.sendMessageToUser("No bars added yet");
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
