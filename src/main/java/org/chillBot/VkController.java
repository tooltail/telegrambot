package org.chillBot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class VkController implements Controller{

    private VkApiClient vk;
    private Integer ts;
    private GroupActor actor;
    private Command currentCommand;
    private AddCommandArgumentsParser addCommandArgumentsParser;
    private Bot bot;
    private Integer id;

    public VkController(Integer id) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        vk = new VkApiClient(transportClient);
        actor = new GroupActor(171475067, "433985e4d2ff9a987d05e893a1eb03940cf5026c345c525cfe0756577156ae07d6d1189d2025227b6985f");
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
        bot = new Bot(new DBPlaceDao(), "0");
        this.id = id;
    }

    @Override
    public void sendMessageToUser(String text) throws ApiException, ClientException {
        vk.messages().send(actor).message(text).userId(id).randomId(new Random().nextInt(10000)).execute();
    }

    public void init() throws ClientException, ApiException, InterruptedException {
        while (true) {
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    try {
                        id = message.getFromId();
                        if (message.getText().equals("/add") || currentCommand == Command.addBar) {
                            if (message.getText().equals("/add")) {
                                currentCommand = Command.addBar;
                                addCommandArgumentsParser = new AddCommandArgumentsParser(new VkController(id));
                                sendMessageToUser("Select the category to which you want to add the establishment:");
                            }
                            else if (!addCommandArgumentsParser.isEnd()) {
                                addCommandArgumentsParser.addArgument(message.getText());
                            }
                            if (addCommandArgumentsParser.isEnd()) {
                                Place place = addCommandArgumentsParser.getPlace();
                                if (bot.addPlace(place)) {
                                    sendMessageToUser("Bar added to database\nYou can check list of bars. Type \\bars");
                                }
                                else {
                                    sendMessageToUser("This place was already added");
                                }
                                currentCommand = null;
                            }
                        }
                        else if (message.getText().equals("/bars")) {
                            List<String> bars = bot.getAllPlaces();
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
                    catch (TelegramApiException | SQLException | ClientException | ApiException e) {
                        e.printStackTrace();
                    }
                });
            }
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
            Thread.sleep(500);
        }
    }
}