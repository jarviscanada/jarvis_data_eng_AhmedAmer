package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PositionService_UnitTest {
    @Mock
    QuoteService mockQuoteService;

    @Mock
    PositionDao mockPositionDao;

    PositionService positionService;
    Position testPosition;
    Quote testQuote;

    @BeforeEach
    public void setUp() {
        positionService = new PositionService(mockPositionDao, mockQuoteService);
        testPosition = new Position();
        testPosition.setTicker("FAKE1");
        testPosition.setValuePaid(1324.5);
        testPosition.setNumOfShares(345);

        testQuote = new Quote();
        testQuote.setTicker("FAKE1");
        testQuote.setOpen(12.0);
        testQuote.setHigh(15.0);
        testQuote.setLow(11.0);
        testQuote.setPrice(14.4);
        testQuote.setVolume(12121);
        testQuote.setLatestTradingDay(Date.valueOf("2024-10-13"));
        testQuote.setPreviousClose(15);
        testQuote.setChange(1.1);
        testQuote.setChangePercent("1.33");
        testQuote.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
    }

    @AfterEach
    public void tearDown() {
        testPosition = null;
        positionService = null;
    }

    @Test
    public void test_buy_invalidNumberOfShares() {
        when(mockQuoteService.fetchQuoteDataFromAPI("FAKE1")).thenReturn(Optional.of(testQuote));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("FAKE1", 12125, 10);
        }); // numOfShares over volume amount
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("FAKE1", 0 ,10);
        }); // numOfShares input 0
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("FAKE1", -2 ,10);
        }); // numOfShares negative
    }

    @Test
    public void test_buy_simulateBadTicker() {
        when(mockQuoteService.fetchQuoteDataFromAPI("!?@#!")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("!?@#!", 0 ,10);
        });
    }

    @Test
    public void test_buy_invalidPrice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("FAKE1", 19 ,0);
        }); // Price 0
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            positionService.buy("FAKE1",  10,-2);
        }); // Price negative
    }

    @Test
    public void test_buy_validArgs() {
        when(mockQuoteService.fetchQuoteDataFromAPI("FAKE1")).thenReturn(Optional.of(testQuote));
        when(mockPositionDao.findById("FAKE1")).thenReturn(Optional.of(testPosition));
        Position result = positionService.buy("FAKE1", 19 ,10);
        verify(mockPositionDao).save(any(Position.class));
        verify(mockPositionDao).findById(anyString());
        Assertions.assertEquals(result.getTicker(), testQuote.getTicker());
    }
}
