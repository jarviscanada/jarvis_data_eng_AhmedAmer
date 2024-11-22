package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
//Use Mockito

class QuoteHttpHelper_Test {

    QuoteHttpHelper helper;

    @BeforeEach
    void setUp() {
        helper = new QuoteHttpHelper();
    }

    @Test
    void test_fetchQuoteInfoGoodTicker() {
        assertNotNull(helper);
        Quote quote = helper.fetchQuoteInfo("IBM");
        Assertions.assertNotNull(quote.getTicker());
        System.out.println(quote);
    }

    @Test
    void test_fetchQuoteInfoBadTicker() {
        Assertions.assertNull(helper.fetchQuoteInfo("9828929").getTicker());
    }

    //Write more tests -> Mock the response, test for consistency in symbol, timestamp, etc
}
