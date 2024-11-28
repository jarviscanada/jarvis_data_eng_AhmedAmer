package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.service.DatabaseConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class QuoteDao_Test {
    DatabaseConnectionManager dcm;
    Connection connection;
    QuoteDao quoteDao;
    Quote testQuote1;
    Quote testQuote2;
    Quote testQuote3;
    Quote testQuote4;

    Logger logger = LoggerFactory.getLogger(QuoteDao_Test.class);

    @BeforeEach
    void setUp() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        testQuote1 = new Quote();
        testQuote2 = new Quote();
        testQuote3 = new Quote();
        testQuote4 = new Quote();

        testQuote1.setTicker("FAKE1");
        testQuote2.setTicker("FAKE2");
        testQuote3.setTicker("FAKE3");
        testQuote4.setTicker("FAKE4");

        testQuote1.setOpen(12.0);
        testQuote1.setHigh(15.0);
        testQuote1.setLow(11.0);
        testQuote1.setPrice(14.4);
        testQuote1.setVolume(12121);
        testQuote1.setLatestTradingDay(Date.valueOf("2024-10-13"));
        testQuote1.setPreviousClose(15);
        testQuote1.setChange(1.1);
        testQuote1.setChangePercent("1.33");
        testQuote1.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        testQuote2.setOpen(12.0);
        testQuote2.setHigh(15.0);
        testQuote2.setLow(11.0);
        testQuote2.setPrice(14.4);
        testQuote2.setVolume(12121);
        testQuote2.setLatestTradingDay(Date.valueOf("2024-10-13"));
        testQuote2.setPreviousClose(15);
        testQuote2.setChange(1.1);
        testQuote2.setChangePercent("1.33");
        testQuote2.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        testQuote3.setOpen(12.0);
        testQuote3.setHigh(15.0);
        testQuote3.setLow(11.0);
        testQuote3.setPrice(14.4);
        testQuote3.setVolume(12121);
        testQuote3.setLatestTradingDay(Date.valueOf("2024-10-13"));
        testQuote3.setPreviousClose(15);
        testQuote3.setChange(1.1);
        testQuote3.setChangePercent("1.33");
        testQuote3.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        testQuote4.setOpen(12.0);
        testQuote4.setHigh(15.0);
        testQuote4.setLow(11.0);
        testQuote4.setPrice(14.4);
        testQuote4.setVolume(12121);
        testQuote4.setLatestTradingDay(Date.valueOf("2024-10-13"));
        testQuote4.setPreviousClose(15);
        testQuote4.setChange(1.1);
        testQuote4.setChangePercent("1.37");
        testQuote4.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));

        try {
            connection = dcm.getConnection();
            quoteDao = new QuoteDao(connection);
            quoteDao.save(testQuote1);
            quoteDao.save(testQuote2);
            quoteDao.save(testQuote3);
            logger.info("Setup Complete. Testing quotes inserted.");
        } catch (SQLException e) {
            logger.error("Could not complete setup", e);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        quoteDao.deleteById(testQuote1.getTicker());
        quoteDao.deleteById(testQuote2.getTicker());
        quoteDao.deleteById(testQuote3.getTicker());
        connection.close();
        logger.info("Teardown Complete.");
    }

    @Test
    public void test_saveInsert() {
        quoteDao.save(testQuote4);
        Optional<Quote> quote = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(quote.isPresent());
        quoteDao.deleteById(testQuote4.getTicker());
    }

    @Test
    public void test_saveUpdate() {
        quoteDao.save(testQuote4);
        Optional<Quote> quoteBeforeUpdate = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(quoteBeforeUpdate.isPresent());
        testQuote4.setHigh(17.7);
        testQuote4.setLow(10.3);
        testQuote4.setPrice(15.8);
        logger.info("Changed testQuote4's High, Low, Price attributes -> " +
                "{} {} {}", testQuote4.getHigh(), testQuote4.getLow(), testQuote4.getPrice());
        quoteDao.save(testQuote4);
        Optional<Quote> quoteAfterUpdate = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(quoteAfterUpdate.isPresent());
        Assertions.assertNotEquals(quoteBeforeUpdate.get().getHigh(),
                quoteAfterUpdate.get().getHigh());
        Assertions.assertNotEquals(quoteBeforeUpdate.get().getLow(),
                quoteAfterUpdate.get().getLow());
        Assertions.assertNotEquals(quoteBeforeUpdate.get().getPrice(),
                quoteAfterUpdate.get().getPrice());
        quoteDao.deleteById(testQuote4.getTicker());
    }

    @Test
    public void test_findById() {
        Optional<Quote> quote = quoteDao.findById(testQuote2.getTicker());
        Assertions.assertTrue(quote.isPresent());
        Assertions.assertEquals(quote.get().getTicker(),
                testQuote2.getTicker());
        Assertions.assertEquals(quote.get().getHigh(),
                testQuote2.getHigh());
        Assertions.assertEquals(quote.get().getLow(),
                testQuote2.getLow());
        Assertions.assertEquals(quote.get().getLatestTradingDay(),
                testQuote2.getLatestTradingDay());
        Assertions.assertEquals(quote.get().getPrice(),
                testQuote2.getPrice());
    }

    @Test
    public void deleteById() {
        quoteDao.save(testQuote4);
        Assertions.assertTrue(quoteDao.findById("FAKE4").isPresent());
        quoteDao.deleteById("FAKE4");
        Assertions.assertFalse(quoteDao.findById("FAKE4").isPresent());

    }

    @Test
    public void test_findAll() {
        quoteDao.save(testQuote4);
        List<Quote> quotes = quoteDao.findAll();
        Assertions.assertFalse(quotes.isEmpty());
        System.out.println(quotes);
        quoteDao.deleteById(testQuote4.getTicker());
    }
}
