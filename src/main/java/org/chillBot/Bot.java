package org.chillBot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class Bot extends TelegramLongPollingBot implements IBot{
    /**
     * Contains how many arguments are left for the user to enter
     */
    private Integer addCommandArguments = 0;

    /**
     * Contains added place
     */
    private Place place;

    private String chatId;

    private PlaceDao placeDao;

    public Bot (PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    /**
     * Gets user input and forms place
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

    /**
     * Add place to db
     * @param place added place
     * @return true if added, false if not
     * @throws SQLException
     */
    public boolean addPlace(Place place) throws SQLException {
        return placeDao.addPlace(place);
    }

    /**
     * Sends a message to the user
     * @param message: message to send
     */
    private void sendMessageToUser(String message) throws TelegramApiException {
         execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    /**
     * Gets list of bars
     */
    public List<String> getAllPlaces() throws SQLException {
        List<Place> places = placeDao.getAllPlaces();
        List<String> formattedOutput = new LinkedList<>();
        for (Place place : places) {
            String result = String.format("%s (%s)",
                    place.getName(),
                    place.getAddress());
            formattedOutput.add(result);
        }
        return formattedOutput;
    }


    /**
     * Gets bot username
     * @return bot username
     */
    @Override
    public String getBotUsername() {
        return "@FindChillPlaceBot";
    }


    /**
     * Logic of bot commands
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
                        List<String> bars = getAllPlaces();
                        if (bars.size() == 0) {
                            sendMessageToUser("No bars added yet");
                        }
                        else {
                            sendMessageToUser("List of bars:");
                            for (String bar: bars) {
                                sendMessageToUser(bar);
                            }
                        }
                }
                else if (addCommandArguments > 0) {
                    addInputToPlace(message.getText());
                    addCommandArguments--;
                }
            }
        }
    }


    /**
     * Gets bot token
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
        Bot bot = new Bot(new DBPlaceDao());
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}