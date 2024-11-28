package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class PositionService_IntTest {
    PositionService positionService;
    PositionDao positionDao;
    QuoteService quoteService;
    QuoteDao quoteDao;
    QuoteHttpHelper httpHelper;
    final Logger logger = LoggerFactory.getLogger(PositionService_IntTest.class);

    Connection connection;
    DatabaseConnectionManager dcm;


    @BeforeEach
    public void setUp() {
        httpHelper = new QuoteHttpHelper();
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        try {
            connection = dcm.getConnection();
            quoteDao = new QuoteDao(connection);
            positionDao = new PositionDao(connection);
            quoteService = new QuoteService(quoteDao, httpHelper);
            positionService = new PositionService(positionDao, quoteService);
        } catch (SQLException e) {
            logger.error("Could not set up test environment", e);
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void test_buy_invalidNumberOfShares() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("AAPL", 1212511223, 10);
        }); // numOfShares over volume amount
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("AAPL", 0 ,10);
        }); // numOfShares input 0
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("AAPL", -2 ,10);
        }); // numOfShares negative
    }

    @Test
    public void test_buy_simulateBadTicker() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("!?@#!", 0 ,10);
        });
    }

    @Test
    public void test_buy_invalidPrice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("AAPL", 19 ,0);
        }); // Price 0
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("AAPL",  10,-2);
        }); // Price negative
    }

    @Test
    public void test_buy_validArgs() {
        Position result = positionService.buy("AAPL", 19 ,10);
        Assertions.assertEquals(result.getTicker(), "AAPL");
        Assertions.assertTrue(positionDao.findById("AAPL").isPresent());
    }
}
