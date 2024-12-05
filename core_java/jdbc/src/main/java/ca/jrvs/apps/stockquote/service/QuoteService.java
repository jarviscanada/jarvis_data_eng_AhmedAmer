package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class QuoteService {
    private final QuoteDao quoteDao;
    private final QuoteHttpHelper quoteHttpHelper;
    private final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    private final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

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
        infoLogger.info("Fetching quote data from API for ticker {}", ticker);
        Quote quote = quoteHttpHelper.fetchQuoteInfo(ticker);

        if (quote == null) {
            infoLogger.info("Quote from QuoteHttpHelper was returned null, returning empty optional.");
            throw new NoSuchElementException("Quote from QuoteHttpHelper was returned null.");
        }

        if (quote.getTicker() == null) {
            infoLogger.info("Stock ticker symbol not found! Returning empty Optional.");
            return Optional.empty();
        } else {
            infoLogger.info("Stock ticker: {} accepted. Updating stock info to DB.", quote.getTicker());

            try {
                quoteDao.save(quote);
            } catch (IllegalArgumentException e) {
                errorLogger.error("Error updating stock info with save method", e);
            }
            return Optional.of(quote);
        }

    }

    //StockQuoteController helper functions - get quote from database instead

    /**
     * Fetches quote info from database if available when API fails
     * @param ticker symbol of stock quote
     * @return optional of quote in db or empty optional
     */
    public Optional<Quote> fetchQuoteDataFromDB(String ticker) {
        infoLogger.info("Fetching quote data from DB");
        return quoteDao.findById(ticker);
    }
}
