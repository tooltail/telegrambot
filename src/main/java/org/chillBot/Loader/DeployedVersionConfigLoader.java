package org.chillBot.Loader;

import org.chillBot.AddressLonLatFinder;
import org.chillBot.controller.TelegramUserInteraction;
import org.chillBot.controller.VkUserInteraction;
import org.chillBot.dao.DBPlaceDao;

/**
 * Downloads necessary data from heroku config
 */
public class DeployedVersionConfigLoader {

    public void loadEnvValues() {
        TelegramUserInteraction.setBotToken(System.getenv("TELEGRAM_BOT_TOKEN"));
        TelegramUserInteraction.setBotUsername(System.getenv("TELEGRAM_BOT_USERNAME"));
        VkUserInteraction.setGroupId(Integer.parseInt(System.getenv("VK_BOT_GROUP_ID")));
        VkUserInteraction.setBotToken(System.getenv("VK_BOT_TOKEN"));
        DBPlaceDao.setTableName(System.getenv("DATABASE_TABLE_NAME"));
        DBPlaceDao.setUser(System.getenv("DATABASE_USER"));
        DBPlaceDao.setPassword(System.getenv("DATABASE_PASSWORD"));
        DBPlaceDao.setUrl(System.getenv("DATABASE_URL"));
        AddressLonLatFinder.setApiKey(System.getenv("API_KEY"));
        AddressLonLatFinder.setSecretKey(System.getenv("SECRET_KEY"));
        AddressLonLatFinder.setDataClient();
    }
}
