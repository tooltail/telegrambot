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

    private Integer addCommandArguments = 0;
    Place place = new Place();

    public String returnAddSqlQuery(String tableName, String type, String name, String address) {
        return String.format("INSERT INTO %s (type, name, address) VALUES('%s', '%s', '%s') ON CONFLICT DO NOTHING",
                tableName, type, name, address);
    }

    public String returnOutputSqlQuery(String tableName) {
        return String.format("SELECT * FROM %s", tableName);
    }

    @Getter
    private final String user = "postgres";

    @Getter
    private final String password = "u_8h,B:vV+z[UzK";

    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", getUser(), getPassword());
    }

    @SneakyThrows
    public void savePlaceToDatabase(String sqlQuery) {
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sqlQuery);
    }

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

    @SneakyThrows
    public void sendMessageToUser(String chatId, String message) {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    @SneakyThrows
    public ResultSet returnListOfBars(String sqlQuery) {
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(sqlQuery);
    }

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

    @Override
    public String getBotUsername() {
        return "@FindChillPlaceBot";
    }

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

    @Override
    public String getBotToken() {
        return "2029119217:AAEyuWfxXOStemuHquNZFZ8vuQxIyFihHTE";
    }
    @SneakyThrows
    public static void main(String[] args) {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}