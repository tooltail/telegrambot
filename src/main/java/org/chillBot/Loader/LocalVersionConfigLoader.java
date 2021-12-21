package org.chillBot.Loader;

import org.chillBot.AddressLonLatFinder;
import org.chillBot.controller.TelegramUserInteraction;
import org.chillBot.controller.VkUserInteraction;
import org.chillBot.dao.DBPlaceDao;

import java.io.*;
import java.util.Properties;

/**
 * Downloads necessary data from local file
 */
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
                        TelegramUserInteraction.setBotToken(prop.getProperty("telegramBot.BotToken"));
                        TelegramUserInteraction.setBotUsername(prop.getProperty("telegramBot.BotUsername"));
                        VkUserInteraction.setGroupId(Integer.parseInt(prop.getProperty("vkBot.GroupId")));
                        VkUserInteraction.setBotToken(prop.getProperty("vkBot.BotToken"));
                        DBPlaceDao.setTableName(prop.getProperty("database.tableName"));
                        DBPlaceDao.setUser(prop.getProperty("database.user"));
                        DBPlaceDao.setPassword(prop.getProperty("database.password"));
                        DBPlaceDao.setUrl(prop.getProperty("database.url"));
                        AddressLonLatFinder.setApiKey(prop.getProperty("daData.ApiKey"));
                        AddressLonLatFinder.setSecretKey(prop.getProperty("daData.SecretKey"));
                        AddressLonLatFinder.setDataClient();
                } catch (Exception e) {
                        throw e;
                } finally {
                        inputStream.close();
                }
        }
}
