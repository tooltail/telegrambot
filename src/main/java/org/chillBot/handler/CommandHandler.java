package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.BotFunction;
import org.chillBot.Command;
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

    private BotFunction bot;

    private boolean isContextSwitched = false;

    private Integer startIdx = 1;

    private Integer pageSize = 3;

    public CommandHandler() {
        bot = new BotFunction(new DBPlaceDao());
    }

    private void updateContext() {
        isContextSwitched = true;
        startIdx = 1;
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
            List<String> bars = bot.getPlaces(startIdx, startIdx + pageSize);
            if (bars.size() == 0 && isContextSwitched) {
                controller.sendMessageToUser("No bars added yet");
            }
            else {
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
                if (bot.addRate(place)) {
                    controller.sendMessageToUser("Your rate was added ;)");
                }
                else {
                    controller.sendMessageToUser("Bar not found. Type /add to add the bar");
                }
                currentCommand = null;
            }
        }
    }
}
