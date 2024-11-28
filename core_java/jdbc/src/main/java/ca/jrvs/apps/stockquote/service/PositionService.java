package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PositionService {
    final PositionDao dao;
    final QuoteService quoteService;
    final Logger logger = LoggerFactory.getLogger(PositionService.class);

    public PositionService(PositionDao dao, QuoteService quoteService) {
        this.dao = dao;
        this.quoteService = quoteService;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker symbol of stock
     * @param numberOfShares number of shares to buy
     * @param price at what price to buy each share
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Share price must be greater than zero.");
        }
        if (numberOfShares <= 0) {
            throw new IllegalArgumentException("Number of shares must be greater than zero.");
        }
        Optional<Quote> optionalQuote = quoteService.fetchQuoteDataFromAPI(ticker);
        if (optionalQuote.isEmpty()) {
            throw new IllegalArgumentException("Please use a valid ticker symbol.");
        }
        int stockVolume = optionalQuote.get().getVolume();
        if (numberOfShares > stockVolume) {
            throw new IllegalArgumentException("Number of shares must be less than the volume of the stock.");
        }
        Position position = new Position();
        position.setTicker(optionalQuote.get().getTicker());
        position.setValuePaid(numberOfShares*price);
        position.setNumOfShares(numberOfShares);
        dao.save(position);
        Position updatedPosition = dao.findById(ticker).get();
        logger.info("Successfully purchased stock: {} at price {} per share.", ticker, price);
        logger.info("New numOfShares: {}. New valuePaid: {}", updatedPosition.getNumOfShares(),
                updatedPosition.getValuePaid());

        return position;
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker symbol of stock
     */
    public void sell(String ticker) {
        Optional<Position> ownedStockOptional = dao.findById(ticker);
        if (ownedStockOptional.isEmpty()) {
            throw new IllegalArgumentException("You do not own this stock. Please provide a " +
                    "ticker symbol for a stock you do own.");
        }
        Optional<Quote> quoteOfOwnedStockOptional = quoteService.fetchQuoteDataFromAPI(ticker);
        if (quoteOfOwnedStockOptional.isEmpty()) {
            logger.error("There was a problem fetching latest stock quote from the API!");
            throw new IllegalArgumentException("Try using a valid ticker symbol for a stock that you own.");
        }
            Position ownedStock = ownedStockOptional.get();
            Quote quoteOfOwnedStock = quoteOfOwnedStockOptional.get();
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
