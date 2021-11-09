package org.chillBot;

import lombok.Getter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Bot extends TelegramLongPollingBot {
    /**
     * A field that contains how many arguments are left for the user to enter
     */
    private Integer addCommandArguments = 0;

    private final Place place = new Place();

    /**
     * A method that returns string sql query which insert the establishment to the database
     * @param tableName: table name
     * @param type: type of establishment
     * @param name: name of establishment
     * @param address: address of establishment
     * @return string sql query
     */
    public String returnAddSqlQuery(String tableName, String type, String name, String address) {
        return String.format("INSERT INTO %s (type, name, address) VALUES('%s', '%s', '%s') ON CONFLICT DO NOTHING",
                tableName, type, name, address);
    }

    /**
     * A method that returns string sql query which returns all establishments from databases
     * @param tableName: table name
     * @return string sql query
     */
    public String returnOutputSqlQuery(String tableName) {
        return String.format("SELECT * FROM %s", tableName);
    }

    /**
     * A field that contains username postgres database
     */
    @Getter
    private final String user = "postgres";

    /**
     * A field that contains password postgres database
     */
    @Getter
    private final String password = "u_8h,B:vV+z[UzK";

    /**
     * A method that returns connection to postgres database
     * @return connection to postgres database
     */
    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", getUser(), getPassword());
    }

    /**
     * A method that writes the establishment to the database
     * @param sqlQuery sql query
     */
    @SneakyThrows
    public void savePlaceToDatabase(String sqlQuery) {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sqlQuery);
    }

    /**
     * A method that describes the logic of add command
     * @param addCommandArguments: command number
     * @param argument: user input
     * @param chatId: chat id
     */
    public void addPlace(Integer addCommandArguments, String argument, String chatId) {
        if (addCommandArguments == 3) {
            place.setType(argument);
            sendMessageToUser(chatId, "Enter the name of the establishment:");
        }
        else if (addCommandArguments == 2) {
            place.setName(argument);
            sendMessageToUser(chatId, "Enter the address of the establishment:");
        }
        else if (addCommandArguments == 1) {
            place.setAddress(argument);
            savePlaceToDatabase(returnAddSqlQuery("place", place.getType(),
                    place.getName(), place.getAddress()));
        }
    }

    /**
     * A method that sends a message to the user
     * @param chatId: chat id
     * @param message: message to send
     */
    @SneakyThrows
    public void sendMessageToUser(String chatId, String message) {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    /**
     * A method that returns list of bars from database
     * @param sqlQuery: sql query
     * @return list of bars
     */
    @SneakyThrows
    public ResultSet returnListOfBars(String sqlQuery) {
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(sqlQuery);
    }

    /**
     * A method that prints list of bars to the user
     * @param chatId: chat id
     */
    @SneakyThrows
    public void printListOfBars(String chatId) {
        ResultSet rs = returnListOfBars(returnOutputSqlQuery("place"));
        while (rs.next()) {
            String result = String.format("%s (%s)",
                    rs.getString("name"),
                    rs.getString("address"));
            sendMessageToUser(chatId, result);
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

            if (message.hasText()) {
                if (message.getText().equals("/add")) {
                    addCommandArguments = 3;
                    sendMessageToUser(message.getChatId().toString(),
                            "Select the category to which you want to add the establishment:");
                }
                else if (message.getText().equals("/bars")) {
                    sendMessageToUser(message.getChatId().toString(),
                            "List of bars:");
                    printListOfBars(message.getChatId().toString());
                }
                else if (addCommandArguments > 0) {
                    addPlace(addCommandArguments, message.getText(),
                            message.getChatId().toString());
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
    @SneakyThrows
    public static void main(String[] args) {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}