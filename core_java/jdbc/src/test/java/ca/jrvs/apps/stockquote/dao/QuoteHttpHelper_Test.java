package ca.jrvs.apps.stockquote.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

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
        System.out.println(quote);
    }

    @Test
    void test_fetchQuoteInfoBadTicker() {
        assertThrows(IllegalArgumentException.class, () -> helper.fetchQuoteInfo("292883"));
    }

    //Write more tests -> Mock the response, test for consistency in symbol, timestamp, etc
}
