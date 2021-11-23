package org.chillBot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;


public class Bot extends TelegramLongPollingBot implements IBot{
    /**
     * A field that contains how many arguments are left for the user to enter
     */
    private Integer addCommandArguments = 0;

    private Place place;

    private String chatId;

    private DBPlaceDao dbDao = new DBPlaceDao();

    /**
     * A method that describes the logic of add command
     * @param argument: user input
     */

    private void addInputToPlace(String argument) throws SQLException, TelegramApiException {
        if (addCommandArguments == 3) {
            place.setType(argument.toLowerCase());
            sendMessageToUser("Enter the name of the establishment:");
        }
        else if (addCommandArguments == 2) {
            place.setName(argument.toLowerCase());
            sendMessageToUser("Enter the address of the establishment:");
        }
        else if (addCommandArguments == 1) {
            place.setAddress(argument.toLowerCase());
            if (addPlace(place))
                sendMessageToUser("Bar added to database\nYou can check list of bars. Type \\bars");
            else
                sendMessageToUser("This place was already added");
        }
    }

    public boolean addPlace(Place place) throws SQLException {
        return dbDao.addPlace(place);
    }

    /**
     * A method that sends a message to the user
     * @param message: message to send
     */
    public void sendMessageToUser(String message) throws TelegramApiException {
         execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    /**
     * A method that prints list of bars to the user
     */
    public boolean printAllPlaces() throws SQLException, TelegramApiException {
        //PlaceDao dbDao = new DBPlaceDao();
        List<Place> places = dbDao.getAllPlaces();
        if (places.size() == 0) {
            sendMessageToUser("No bars added yet");
            return false;
        }
        else {
            for (Place place : places) {
                String result = String.format("%s (%s)",
                        place.getName(),
                        place.getAddress());
                sendMessageToUser(result);
            }
            return true;
        }
    }


    /**
     * A method that returns bot username
     * @return bot username
     */
    @Override
    public String getBotUsername() {
        return "@FindChillPlaceBot";
    }


    /**
     * A method that the logic of bot commands
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            chatId = message.getChatId().toString();
            if (message.hasText()) {
                if (message.getText().equals("/add")) {
                    place = new Place();
                    addCommandArguments = 3;
                    sendMessageToUser("Select the category to which you want to add the establishment:");
                }
                else if (message.getText().equals("/bars")) {
                        sendMessageToUser("List of bars:");
                        printAllPlaces();
                }
                else if (addCommandArguments > 0) {
                    addInputToPlace(message.getText());
                    addCommandArguments--;
                }
            }
        }
    }


    /**
     * A method that returns bot token
     * @return bot token
     */
    @Override
    public String getBotToken() {
        return "2029119217:AAEyuWfxXOStemuHquNZFZ8vuQxIyFihHTE";
    }


    /**
     * Program entry point
     */
    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}