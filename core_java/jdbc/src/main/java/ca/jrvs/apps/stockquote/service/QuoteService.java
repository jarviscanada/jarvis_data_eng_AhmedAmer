package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class QuoteService {
    private final QuoteDao quoteDao;
    private final QuoteHttpHelper quoteHttpHelper;
    private final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    public QuoteService(QuoteDao quoteDao, QuoteHttpHelper quoteHttpHelper) {
        this.quoteDao = quoteDao;
        this.quoteHttpHelper = quoteHttpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker symbol for stock
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        logger.info("Fetching quote data from API");
        Quote quote = quoteHttpHelper.fetchQuoteInfo(ticker);
        if (quote.getTicker() == null) {
            logger.info("Stock ticker symbol not found! Returning empty Optional.");
            return Optional.empty();
        } else if(!quote.getTicker().equals(ticker)) {
            throw new RuntimeException("API ERROR: Stock returned and provided ticker symbol differ.");
        } else {
            logger.info("Stock ticker: {} accepted. Updating stock info to DB.", quote.getTicker());
            try {
                quoteDao.save(quote);
            } catch (Exception e) {
                logger.error("Error updating stock info", e);
            }
        }
        return Optional.of(quote);
    }
}
