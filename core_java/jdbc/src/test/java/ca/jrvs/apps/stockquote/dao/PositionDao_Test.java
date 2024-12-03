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

public class PositionDao_Test {
    DatabaseConnectionManager dcm;
    Connection connection;
    PositionDao positionDao;
    QuoteDao quoteDao;
    Position testPosition1;
    Position testPosition2;
    Position testPosition3;
    Quote testQuote1;
    Quote testQuote2;
    Quote testQuote3;

    Logger logger = LoggerFactory.getLogger("testLogger");

    @BeforeEach
    void setUp() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        testPosition1 = new Position();
        testPosition2 = new Position();
        testPosition3 = new Position();
        testQuote1 = new Quote();
        testQuote2 = new Quote();
        testQuote3 = new Quote();

        testPosition1.setTicker("FAKE1");
        testPosition1.setNumOfShares(34);
        testPosition1.setValuePaid(400.50);
        testPosition2.setTicker("FAKE2");
        testPosition2.setNumOfShares(76);
        testPosition2.setValuePaid(1400.50);
        testPosition3.setTicker("FAKE3");
        testPosition3.setNumOfShares(36);
        testPosition3.setValuePaid(12300.50);

        testQuote1.setTicker("FAKE1");
        testQuote2.setTicker("FAKE2");
        testQuote3.setTicker("FAKE3");

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

        try {
            connection = dcm.getConnection();
            positionDao = new PositionDao(connection);
            quoteDao = new QuoteDao(connection);
            quoteDao.save(testQuote1);
            quoteDao.save(testQuote2);
            quoteDao.save(testQuote3);
            positionDao.save(testPosition1);
            positionDao.save(testPosition2);
            logger.info("TEST-PositionDao: Setup complete, DAOs initialized and data saved.");
        } catch (SQLException e) {
            logger.error("TEST-PositionDao: Could not complete setup", e);
        }

    }

    @AfterEach
    void tearDown() throws SQLException {
        positionDao.deleteById(testPosition1.getTicker());
        positionDao.deleteById(testPosition2.getTicker());
        quoteDao.deleteById(testQuote1.getTicker());
        quoteDao.deleteById(testQuote2.getTicker());
        quoteDao.deleteById(testQuote3.getTicker());
        connection.close();
        logger.info("TEST-PositionDao: Teardown Complete.");
    }

    @Test
    public void test_saveInsert() {
        positionDao.save(testPosition3);
        Optional<Position> position = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(position.isPresent());
        positionDao.deleteById(testPosition3.getTicker());
    }

    @Test
    public void test_saveUpdate() {
        positionDao.save(testPosition3);
        Optional<Position> positionBeforeUpdate = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(positionBeforeUpdate.isPresent());
        testPosition3.setNumOfShares(100);
        logger.info("TEST-PositionDao: Changed numOfShares for testPosition3: {}", testPosition3.getNumOfShares());
        positionDao.save(testPosition3);
        Optional<Position> positionAfterUpdate = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(positionAfterUpdate.isPresent());
        Assertions.assertNotEquals(positionBeforeUpdate.get().getNumOfShares(),
                positionAfterUpdate.get().getNumOfShares());
        logger.info("TEST-PositionDao: Updated numOfShares after UPDATE statement: {}",
                positionAfterUpdate.get().getNumOfShares());
        positionDao.deleteById(testPosition3.getTicker());
    }

    @Test
    public void test_findById() {
        Optional<Position> position = positionDao.findById(testPosition1.getTicker());
        Assertions.assertTrue(position.isPresent());
        Assertions.assertEquals(position.get().getTicker(),
                testPosition1.getTicker());
        Assertions.assertEquals(position.get().getValuePaid(),
                testPosition1.getValuePaid());
        Assertions.assertEquals(position.get().getNumOfShares(),
                testPosition1.getNumOfShares());
    }

    @Test
    public void test_deleteById() {
        positionDao.save(testPosition3);
        Assertions.assertTrue(positionDao.findById("FAKE3").isPresent());
        positionDao.deleteById("FAKE3");
        Assertions.assertFalse(positionDao.findById("FAKE3").isPresent());
    }

    @Test
    public void test_findAll() {
        positionDao.save(testPosition3);
        List<Position> positions = positionDao.findAll();
        Assertions.assertFalse(positions.isEmpty());
        System.out.println(positions);
        positionDao.deleteById(testPosition3.getTicker());
    }
}
