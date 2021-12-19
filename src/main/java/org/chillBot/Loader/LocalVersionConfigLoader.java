package org.chillBot.Loader;

import org.chillBot.Location;
import org.chillBot.controller.TelegramController;
import org.chillBot.controller.VkController;
import org.chillBot.dao.DBPlaceDao;

import java.io.*;
import java.util.Properties;

public class LocalVersionConfigLoader {
        InputStream inputStream;

        public void loadPropValues() throws IOException {
                try {
                        Properties prop = new Properties();
                        String propFileName = "config.properties";

                        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
                        if (inputStream != null) {
                                prop.load(inputStream);
                        } else {
                                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                        }
                        TelegramController.setBotToken(prop.getProperty("telegramBot.BotToken"));
                        TelegramController.setBotUsername(prop.getProperty("telegramBot.BotUsername"));
                        VkController.setGroupId(Integer.parseInt(prop.getProperty("vkBot.GroupId")));
                        VkController.setBotToken(prop.getProperty("vkBot.BotToken"));
                        DBPlaceDao.setTableName(prop.getProperty("database.tableName"));
                        DBPlaceDao.setUser(prop.getProperty("database.user"));
                        DBPlaceDao.setPassword(prop.getProperty("database.password"));
                        DBPlaceDao.setUrl(prop.getProperty("database.url"));
                        Location.setApiKey(prop.getProperty("daData.ApiKey"));
                        Location.setSecretKey(prop.getProperty("daData.SecretKey"));
                        Location.setDataClient();
                } catch (Exception e) {
                        throw e;
                } finally {
                        inputStream.close();
                }
        }
}
