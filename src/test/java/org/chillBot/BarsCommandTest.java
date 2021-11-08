package org.chillBot;

import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class BarsCommandTest {
    String PATH = "C:\\Users\\Alex\\IdeaProjects\\telegrambot\\src\\test\\resources\\";
    ScriptRunner sr;
    Connection con;

    @SneakyThrows
    @Before
    public void setUp() {
        con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", "postgres", "u_8h,B:vV+z[UzK");
        System.out.println("Connected to the PostgreSQL server successfully.");
        sr = new ScriptRunner(con);
        Reader reader = new BufferedReader(new FileReader(PATH + "initDB.sql"));
        sr.runScript(reader);
    }

    @SneakyThrows
    @Test
    public void testOutput() {
        Bot bot = new Bot();
        Reader reader = new BufferedReader(new FileReader(PATH + "populateDB.sql"));
        sr.runScript(reader);
        String[] answers = {"Televisor Radisheva, 4", "Melodiya Pervomayskaya, 36"};
        ResultSet rs = bot.returnListOfBars(bot.returnOutputSqlQuery("placetest"));
        int cnt = 0;
        while (rs.next()) {
            String result = String.format("%s %s",
                    rs.getString("name"),
                    rs.getString("address"));
            assertEquals(answers[cnt], result);
            cnt++;
        }
        assertEquals(2, cnt);
    }

    @SneakyThrows
    @After
    public void tearDown() {
        Reader reader = new BufferedReader(new FileReader(PATH + "dropDB.sql"));
        sr.runScript(reader);
    }
}
