package org.chillBot.Loader;

import org.chillBot.Location;
import org.chillBot.controller.TelegramController;
import org.chillBot.controller.VkController;
import org.chillBot.dao.DBPlaceDao;

/**
 * Downloads necessary data from heroku config
 */
public class DeployedVersionConfigLoader {

    public void loadEnvValues() {
        TelegramController.setBotToken(System.getenv("TELEGRAM_BOT_TOKEN"));
        TelegramController.setBotUsername(System.getenv("TELEGRAM_BOT_USERNAME"));
        VkController.setGroupId(Integer.parseInt(System.getenv("VK_BOT_GROUP_ID")));
        VkController.setBotToken(System.getenv("VK_BOT_TOKEN"));
        DBPlaceDao.setTableName(System.getenv("DATABASE_TABLE_NAME"));
        DBPlaceDao.setUser(System.getenv("DATABASE_USER"));
        DBPlaceDao.setPassword(System.getenv("DATABASE_PASSWORD"));
        DBPlaceDao.setUrl(System.getenv("DATABASE_URL"));
        Location.setApiKey(System.getenv("API_KEY"));
        Location.setSecretKey(System.getenv("SECRET_KEY"));
        Location.setDataClient();
    }
}
