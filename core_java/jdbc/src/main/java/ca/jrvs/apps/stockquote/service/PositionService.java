package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PositionService {
    final PositionDao dao;
    final QuoteService quoteService;
    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

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
    public Position buy(String ticker, int numberOfShares, double price, int stockVolume) {
        if (price <= 0) {
            errorLogger.error("Buy method: Share price must be greater than zero.");
            throw new IllegalArgumentException("Share price must be greater than zero.");
        }

        if (stockVolume <= 0) {
            errorLogger.error("Buy method: Stock volume must be greater than zero.");
            throw new IllegalArgumentException("Stock volume must be greater than zero.");
        }

        if (numberOfShares <= 0) {
            errorLogger.error("Buy Method: Number of shares must be greater than zero.");
            throw new IllegalArgumentException("Number of shares must be greater than zero.");
        }

        if (numberOfShares > stockVolume) {
            errorLogger.error("Buy Method: Number of shares exceeds stock volume.");
            throw new IllegalArgumentException("Number of shares must be less than the volume of the stock.");
        }

        Position position = new Position();

        position.setTicker(ticker);
        position.setValuePaid(numberOfShares*price);
        position.setNumOfShares(numberOfShares);
        dao.save(position);

        Position updatedPosition = dao.findById(ticker).get();

        infoLogger.info("Successfully purchased stock: {} at price {} per share.", ticker, price);
        infoLogger.info("New numOfShares: {}. New valuePaid: {}", updatedPosition.getNumOfShares(),
                updatedPosition.getValuePaid());

        return position;
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker symbol of stock
     */
    public void sell(String ticker, double price) throws IllegalArgumentException{
        Optional<Position> ownedStockOptional = dao.findById(ticker);

        if (ownedStockOptional.isEmpty()) {
            errorLogger.error("User does not own stock for ticker: {}", ticker);
            throw new IllegalArgumentException("You do not own this stock. Please provide a " +
                    "ticker symbol for a stock you do own.");
        }

            Position ownedStock = ownedStockOptional.get();


            double newTotalPrice = price * ownedStock.getNumOfShares();
            int numberOfShares = ownedStock.getNumOfShares();
            infoLogger.info("The new price of {} is: {}", ticker, price);
            infoLogger.info("Selling all {} shares of {} at total price: {}", numberOfShares,
                    ticker, newTotalPrice);
            infoLogger.info("Net gain/loss: {}", newTotalPrice - ownedStock.getValuePaid());
            dao.deleteById(ticker);

    }

    // StockQuoteController helper functions - fetching positions from database

    /**
     * Helper function that fetches a position from the database
     * and used in conjunction with the sell menu
     * @param ticker symbol of owned stock position
     * @return optional of position from database or empty optional
     */
    public Optional<Position> fetchPosition(String ticker) {
        return dao.findById(ticker);
    }

    /**
     * Helper function that fetches all owned stock positions
     * and is used in conjunction with the display all stock menu
     * @return list of positions from database
     */
    public List<Position> fetchAllPositions() {
        return dao.findAll();
    }
}
