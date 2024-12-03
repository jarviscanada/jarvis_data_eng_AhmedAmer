package ca.jrvs.apps.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {
    private final Connection connection;
    private final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    private final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    public QuoteDao(Connection connection) {
        this.connection = connection;
    }

    private final String SELECT_ALL = "SELECT * FROM quote";
    private final String SELECT_BY_ID = "SELECT * FROM quote WHERE symbol = ?";
    private final String INSERT = "INSERT INTO quote (symbol, open, high, low, " +
            "price, volume, latest_trading_day, previous_close, change, change_percent, " +
            "timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE = "UPDATE quote SET open = ?, high = ?, " +
            "low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, " +
            "change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
    private final String DELETE = "DELETE FROM quote WHERE symbol = ?";
    private final String DELETE_ALL = "DELETE FROM quote";



    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (this.findById(entity.getTicker()).isEmpty()) {
            try (PreparedStatement ps = this.connection.prepareStatement(INSERT);) {
                ps.setString(1, entity.getTicker());
                ps.setDouble(2, entity.getOpen());
                ps.setDouble(3, entity.getHigh());
                ps.setDouble(4, entity.getLow());
                ps.setDouble(5, entity.getPrice());
                ps.setDouble(6, entity.getVolume());
                ps.setDate(7, entity.getLatestTradingDay());
                ps.setDouble(8, entity.getPreviousClose());
                ps.setDouble(9, entity.getChange());
                ps.setString(10, entity.getChangePercent());
                ps.setTimestamp(11, entity.getTimestamp());
                ps.execute();
                infoLogger.info("Executed INSERT statement on ticker: {}", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                errorLogger.error("UPDATE Statement failure for provided entity", e);
            }
        } else {
            try (PreparedStatement ps = this.connection.prepareStatement(UPDATE);) {
                ps.setDouble(1, entity.getOpen());
                ps.setDouble(2, entity.getHigh());
                ps.setDouble(3, entity.getLow());
                ps.setDouble(4, entity.getPrice());
                ps.setDouble(5, entity.getVolume());
                ps.setDate(6, entity.getLatestTradingDay());
                ps.setDouble(7, entity.getPreviousClose());
                ps.setDouble(8, entity.getChange());
                ps.setString(9, entity.getChangePercent());
                ps.setTimestamp(10, entity.getTimestamp());
                ps.setString(11, entity.getTicker());
                ps.execute();
                infoLogger.info("Executed UPDATE statement on ticker: {}", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                errorLogger.error("UPDATE Statement failure for provided entity", e);
            }
        }
        return null;
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = new Quote();
        try (PreparedStatement ps = this.connection.prepareStatement(SELECT_BY_ID);) {
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
            }
            if (quote.getTicker() == null) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            errorLogger.error("Could not retrieve quote from ticker {}", s, e);
        } catch (IllegalArgumentException e) {
            errorLogger.error("Please provide a valid ticker symbol.", e);
        }
        return Optional.of(quote);
    }

    @Override
    public List<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        try (Statement s = this.connection.createStatement();
             ResultSet rs = s.executeQuery(SELECT_ALL)) {
            while(rs.next()) {
                Quote quote = new Quote();
                quote.setTicker(rs.getString("symbol"));
                quote.setOpen(rs.getDouble("open"));
                quote.setHigh(rs.getDouble("high"));
                quote.setLow(rs.getDouble("low"));
                quote.setPrice(rs.getDouble("price"));
                quote.setVolume(rs.getInt("volume"));
                quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
                quote.setPreviousClose(rs.getDouble("previous_close"));
                quote.setChange(rs.getDouble("change"));
                quote.setChangePercent(rs.getString("change_percent"));
                quote.setTimestamp(rs.getTimestamp("timestamp"));
                quotes.add(quote);
            }
        } catch (SQLException e) {
            errorLogger.error("Could not complete request: Find All Entities", e);
        }
        return quotes;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (findById(s).isEmpty()) {
            throw new IllegalArgumentException("No entities found with that particular ticker symbol.");
        }
        try (PreparedStatement ps = this.connection.prepareStatement(DELETE)) {
            ps.setString(1, s);
            ps.execute();
        } catch (SQLException e) {
            errorLogger.error("Could not delete quote from ticker {}", s, e);
        }
    }

    @Override
    public void deleteAll() {
        try (Statement s = this.connection.createStatement()) {
            s.executeQuery(DELETE_ALL);
        } catch (SQLException e) {
            errorLogger.error("Could not delete all entities", e);
        }
    }
}
