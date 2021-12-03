package org.chillBot;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.checkerframework.checker.units.qual.C;
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


public class Bot extends TelegramLongPollingBot implements IBot, Controller{
    /**
     * Contains how many arguments are left for the user to enter
     */
    //private Integer addCommandArguments = 0;

    /**
     * Contains added place
     */
    //private Place place;

    public String chatId;

    private PlaceDao placeDao;

    private Command currentCommand;

    private AddCommandArgumentsParser addCommandArgumentsParser;

    public Bot (PlaceDao placeDao, String chatId) {
        this.placeDao = placeDao;
        this.chatId = chatId;
    }

    /**
     * Gets user input and forms place
     * @param argument: user input
     * @throws SQLException
     * @throws TelegramApiException
     */

    /*
    public void addInputToPlace(String argument) throws SQLException, TelegramApiException {
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
     */

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
     * @throws TelegramApiException
     */
    public void sendMessageToUser(String message) throws TelegramApiException {
         execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    /**
     * Gets list of bars
     * @throws SQLException
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
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                chatId = message.getChatId().toString();
                if (message.getText().equals("/add") || currentCommand == Command.addBar) {
                    if (message.getText().equals("/add")) {
                        currentCommand = Command.addBar;
                        addCommandArgumentsParser = new AddCommandArgumentsParser(new Bot(new DBPlaceDao(), chatId));
                        sendMessageToUser("Select the category to which you want to add the establishment:");
                    }
                    else if (!addCommandArgumentsParser.isEnd()) {
                        addCommandArgumentsParser.addArgument(message.getText());
                    }
                    if (addCommandArgumentsParser.isEnd()) {
                        Place place = addCommandArgumentsParser.getPlace();
                        if (addPlace(place)) {
                            sendMessageToUser("Bar added to database\nYou can check list of bars. Type \\bars");
                        }
                        else {
                            sendMessageToUser("This place was already added");
                        }
                        currentCommand = null;
                    }
                }
                else if (message.getText().equals("/bars")) {
                    List<String> bars = getAllPlaces();
                    if (bars.size() == 0) {
                        sendMessageToUser("No bars added yet");
                    } else {
                        sendMessageToUser("List of bars:");
                        for (String bar : bars) {
                            sendMessageToUser(bar);
                        }
                    }
                }
            }
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
        catch (SQLException | ClientException | ApiException e) {
            e.printStackTrace();
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


    public static void main(String[] args) throws TelegramApiException, ClientException, InterruptedException, ApiException {
        Bot bot = new Bot(new DBPlaceDao(), "0");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
        VkController vkController = new VkController(0);
        vkController.init();
    }
}