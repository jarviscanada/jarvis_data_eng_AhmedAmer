package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

public class PositionService {
    private PositionDao dao;

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

    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker symbol of stock
     */
    public void sell(String ticker) {
    }
}
