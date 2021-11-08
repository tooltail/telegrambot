package org.chillBot;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;

import static org.junit.Assert.assertEquals;

public class AddCommandTest {

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
    public void testExistenceOfRecordsInDB() {
        Bot bot = new Bot();
        String answer = "Bar Televisor Radisheva, 4";
        bot.savePlaceToDatabase(bot.returnAddSqlQuery("placetest",
                "Bar",
                "Televisor",
                "Radisheva, 4"));
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM placetest");
        while (rs.next()) {
            String result = String.format("%s %s %s",
                    rs.getString("type"),
                    rs.getString("name"),
                    rs.getString("address"));
            assertEquals(answer, result);
        }
    }

    @SneakyThrows
    @After
    public void tearDown() {
        Reader reader = new BufferedReader(new FileReader(PATH + "dropDB.sql"));
        sr.runScript(reader);
    }
}
