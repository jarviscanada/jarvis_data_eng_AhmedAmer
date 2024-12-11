package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


public class QuoteService_IntTest {
    QuoteService service;
    QuoteDao quoteDao;
    QuoteHttpHelper httpHelper;
    final Logger logger = LoggerFactory.getLogger("testLogger");

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
            service = new QuoteService(quoteDao, httpHelper);
        } catch (SQLException e) {
            logger.error("Could not set up test environment", e);
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void test_fetchQuoteDataFromAPI_badTick() {
        String badTick = "!!>2x";
        Optional<Quote> result = service.fetchQuoteDataFromAPI(badTick);
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void test_fetchQuoteDataFromAPI_goodTick() {
        String goodTick = "AAPL";
        Optional<Quote> serviceResult = service.fetchQuoteDataFromAPI(goodTick);
        Assertions.assertTrue(serviceResult.isPresent());
        Assertions.assertEquals(goodTick, serviceResult.get().getTicker());
        Assertions.assertTrue(quoteDao.findById(goodTick).isPresent());
    }

}
