package org.chillBot.handler;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.chillBot.Bot;
import org.chillBot.Command;
import org.chillBot.Place;
import org.chillBot.controller.Controller;
import org.chillBot.dao.DBPlaceDao;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public class CommandHandler {

    private Command currentCommand;

    private AddCommandArgumentsHandler addCommandArgumentsParser;

    private Bot bot;

    public CommandHandler() {
        bot = new Bot(new DBPlaceDao());
    }

    public void processInput(String message, Controller controller) throws TelegramApiException, ClientException, ApiException, SQLException {
        if (message.equals("/add") || currentCommand == Command.addBar) {
            if (message.equals("/add")) {
                currentCommand = Command.addBar;
                addCommandArgumentsParser = new AddCommandArgumentsHandler(controller);
                controller.sendMessageToUser("Select the category to which you want to add the establishment:");
            }
            else if (!addCommandArgumentsParser.isEnd()) {
                addCommandArgumentsParser.addArgument(message);
            }
            if (addCommandArgumentsParser.isEnd()) {
                Place place = addCommandArgumentsParser.getPlace();
                if (bot.addPlace(place)) {
                    controller.sendMessageToUser("Bar added to database\nYou can check list of bars. Type \\bars");
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
    }
}