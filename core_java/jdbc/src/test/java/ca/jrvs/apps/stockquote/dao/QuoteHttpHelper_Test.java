package ca.jrvs.apps.stockquote.dao;

import org.junit.jupiter.api.Test;
//Use Mockito

public class QuoteHttpHelper_Test {
    QuoteHttpHelper helper = new QuoteHttpHelper();

    @Test
    void test_fetchQuoteInfo() {
        Quote quote = helper.fetchQuoteInfo("IBM");
        System.out.println(quote);
    }

    //Write more tests -> Mock the response, test for consistency in symbol, timestamp, etc
}
