package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class PositionService {
    private final PositionDao dao;
    private QuoteService quoteService;
    final Logger logger = LoggerFactory.getLogger(PositionService.class);

    public PositionService(PositionDao dao) {
        this.dao = dao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker symbol of stock
     * @param numberOfShares number of shares to buy
     * @param price at what price to buy each share
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position position = new Position();
        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(ticker);
        if (quote.isEmpty()) {
            throw new IllegalArgumentException("Please use a valid ticker symbol.");
        }
        int stockVolume = quote.get().getVolume();
        if (numberOfShares <= 0) {
            throw new IllegalArgumentException("Number of shares must be greater than zero.");
        }
        // If a user purchases a set amount of shares of a stock it is assumed that the API is updated fast enough
        // to reflect the new amount of shares available currently, thus no other checks are needed other than below
        if (numberOfShares > stockVolume) {
            throw new IllegalArgumentException("Number of shares must be less than the volume of the stock.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Share price must be greater than zero.");
        }

        dao.save(position);
        Position updatedPosition = dao.findById(position.getTicker()).get();
        logger.info("Successfully purchased stock: {} at price {} per share.", ticker, price);
        logger.info("New numOfShares, valuePaid: {}, {}", updatedPosition.getNumOfShares(),
                updatedPosition.getValuePaid());

        return position;
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker symbol of stock
     */
    public void sell(String ticker) {
        Optional<Position> ownedStockOpt = dao.findById(ticker);
        Optional<Quote> quoteOfOwnedStockOpt = quoteService.fetchQuoteDataFromAPI(ticker);
        if (quoteOfOwnedStockOpt.isEmpty()) {
            logger.error("There was a problem fetching latest stock quote from the API!");
            throw new IllegalArgumentException("Try using a valid ticker symbol for a stock that exists.");
        }
        if (ownedStockOpt.isEmpty()) {
            throw new IllegalArgumentException("You do not own this stock. Please provide a " +
                    "ticker symbol for a stock you do own.");
        } else {
            Position ownedStock = ownedStockOpt.get();
            Quote quoteOfOwnedStock = quoteOfOwnedStockOpt.get();
            double stockPrice = quoteOfOwnedStock.getPrice();
            double newTotalPrice = stockPrice * ownedStock.getNumOfShares();
            int numberOfShares = ownedStock.getNumOfShares();
            logger.info("The new price of {} is: {}", ticker, stockPrice);
            logger.info("Selling all {} shares of {} at total price: {}", numberOfShares,
                    ticker, newTotalPrice);
            logger.info("Net gain/loss: {}", newTotalPrice - ownedStock.getValuePaid());
            dao.deleteById(ticker);
        }

    }
}
