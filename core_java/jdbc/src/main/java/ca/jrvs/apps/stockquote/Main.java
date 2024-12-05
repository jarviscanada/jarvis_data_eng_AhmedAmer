package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.service.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private static QuoteDao mainQuoteDao;
    private static PositionDao mainPositionDao;
    private static QuoteService mainQuoteService;
    private static PositionService mainPositionService;
    private static QuoteHttpHelper mainHttpHelper;
    private static StockQuoteController controller;

    final static Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final static Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    private static String server;
    private static String database;
    private static String username;
    private static String password;

    public static void main(String[] args) {
        parseProperties();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(server, database, username, password);

        try (Connection connection = dcm.getConnection()) {

            mainHttpHelper = new QuoteHttpHelper();
            mainQuoteDao = new QuoteDao(connection);
            mainPositionDao = new PositionDao(connection);
            mainQuoteService = new QuoteService(mainQuoteDao, mainHttpHelper);
            mainPositionService = new PositionService(mainPositionDao, mainQuoteService);
            controller = new StockQuoteController(mainQuoteService, mainPositionService);
            infoLogger.info("App resources initialized");
            controller.initClient();

        } catch (SQLException error) {

            System.out.println("ERROR: Could not establish connection to database.");
            System.out.println("Ensure that the database is running and that the 'properties.txt' file contains accurate properties for the connection.");
            errorLogger.error("Could not establish connection to db", error);

        }
    }

    public static void parseProperties() {
        Properties props = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/properties.txt")) {

            props.load(fileInputStream);
            server = props.getProperty("server");
            database = props.getProperty("database");
            username = props.getProperty("username");
            password = props.getProperty("password");
//            apiKey = props.getProperty("apiKey");
            infoLogger.info("Imported properties from properties.txt file.");

        } catch (IOException error) {
            errorLogger.error("Could not read properties file to initialize app resources.", error);
        }

    }
}
