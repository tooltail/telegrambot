package org.example;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class TestBot extends TelegramLongPollingBot {

    Integer addCommandArguments = 0;
    Place place = new Place();

    public void savePlaceToDatabase() {
        EntityManager em = Persistence.createEntityManagerFactory("ru.easyjava.data.jpa.hibernate")
                .createEntityManager();
        em.getTransaction().begin();
        em.persist(place);
        em.getTransaction().commit();
        em.close();
    }

    public void addCommand(Integer addCommandArgumets, String argument, String chatId) {
        if (addCommandArgumets == 3) {
            place.setType(argument);
            sendMessageToUser(chatId, "Enter the address of the establishment:");
        }
        else if (addCommandArgumets == 2) {
            place.setAddress(argument);
            sendMessageToUser(chatId, "Enter the name of the establishment:");
        }
        else if (addCommandArgumets == 1) {
            place.setName(argument);
            savePlaceToDatabase();
        }
    }

    @SneakyThrows
    public void sendMessageToUser(String chatId, String message) {
        execute(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
    }

    public void printListOfBars(String chatId) {
        EntityManager em = Persistence.createEntityManagerFactory("ru.easyjava.data.jpa.hibernate")
                .createEntityManager();
        em.getTransaction().begin();
        em.createQuery("SELECT p FROM Place p", Place.class)
                .getResultList()
                .forEach(p -> sendMessageToUser(chatId, String.format("%s (%s)", p.getName(), p.getAddress())));
        em.getTransaction().commit();
        em.close();
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
                    addCommand(addCommandArguments, message.getText(),
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
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
}
