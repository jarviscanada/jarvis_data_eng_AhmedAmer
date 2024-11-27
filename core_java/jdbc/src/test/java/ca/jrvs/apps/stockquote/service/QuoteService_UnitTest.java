package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuoteService_UnitTest {
    QuoteService service;
    Quote testQuote;
    Quote badTickQuote;

    @Mock
    QuoteDao mockQuoteDao;

    @Mock
    QuoteHttpHelper mockQuoteHttpHelper;

    @BeforeEach
    public void setUp() {
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

        badTickQuote = new Quote();
        badTickQuote.setTicker(null);

        service = new QuoteService(mockQuoteDao, mockQuoteHttpHelper);
    }

    @AfterEach
    public void tearDown() {
        testQuote = null;
        badTickQuote = null;
        service = null;
    }

    @Test
    public void test_fetchQuoteDataFromAPI_badTick() {
        String badTick = "!!>2x";
        when(mockQuoteHttpHelper.fetchQuoteInfo(badTick)).thenReturn(badTickQuote);
        Optional<Quote> result = service.fetchQuoteDataFromAPI(badTick);
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void test_fetchQuoteDataFromAPI_goodTick() {
        String goodTick = "FAKE1";
        when(mockQuoteHttpHelper.fetchQuoteInfo(goodTick)).thenReturn(testQuote);
        Optional<Quote> serviceResult = service.fetchQuoteDataFromAPI(goodTick);
        Assertions.assertTrue(serviceResult.isPresent());
        verify(mockQuoteDao).save(testQuote);
        Assertions.assertEquals(testQuote.getTicker(), serviceResult.get().getTicker());
        Assertions.assertEquals(testQuote, serviceResult.get());
    }

    @Test
    public void test_fetchQuoteDataFromAPI_wrongTick() {
        String wrongTick = "FAKE2";
        when(mockQuoteHttpHelper.fetchQuoteInfo(wrongTick)).thenReturn(testQuote);
        Assertions.assertThrows(RuntimeException.class, () ->
                service.fetchQuoteDataFromAPI(wrongTick));
    }
}
