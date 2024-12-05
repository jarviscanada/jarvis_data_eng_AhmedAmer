package ca.jrvs.apps.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String>{
    private final Connection connection;
    private final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    private final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    public PositionDao(Connection connection) {
        this.connection = connection;
    }

    private final String SELECT_ALL = "SELECT * FROM position";
    private final String SELECT_BY_ID = "SELECT symbol, number_of_shares, " +
            "value_paid FROM position WHERE symbol = ?";
    private final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid)" +
            "VALUES (?, ?, ?)";
    private final String UPDATE = "UPDATE position SET number_of_shares = ?, " +
            "value_paid = ? WHERE symbol = ?";
    private final String DELETE = "DELETE FROM position WHERE symbol = ?";
    private final String DELETE_ALL = "DELETE FROM position";

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        Optional<Position> oldPosition = this.findById(entity.getTicker());

        if (oldPosition.isEmpty()) {

            try (PreparedStatement ps = this.connection.prepareStatement(INSERT)) {
                ps.setString(1, entity.getTicker());
                ps.setInt(2, entity.getNumOfShares());
                ps.setDouble(3, entity.getValuePaid());
                ps.execute();
                infoLogger.info("INSERT statement running for {} position", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                errorLogger.error("Could not INSERT position", e);
            }

        } else {

            try (PreparedStatement ps = this.connection.prepareStatement(UPDATE)) {
                int newNumOfShares = oldPosition.get().getNumOfShares() + entity.getNumOfShares();
                double newValuePaid = oldPosition.get().getValuePaid() + entity.getValuePaid();
                ps.setInt(1, newNumOfShares);
                ps.setDouble(2, newValuePaid);
                ps.setString(3, entity.getTicker());
                ps.execute();
                infoLogger.info("UPDATE statement running for {} position", entity.getTicker());
                return this.findById(entity.getTicker()).get();
            } catch (SQLException e) {
                errorLogger.error("Could not UPDATE position", e);
            }

        }

        return null;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        Position position = new Position();

        try (PreparedStatement ps = this.connection.prepareStatement(SELECT_BY_ID)) {
            ps.setString(1, s);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                position.setTicker(rs.getString("symbol"));
                position.setNumOfShares(rs.getInt("number_of_shares"));
                position.setValuePaid(rs.getDouble("value_paid"));

            }

            if (position.getTicker() == null) {
                return Optional.empty();
            }

        } catch (SQLException e) {
            errorLogger.error("Could not retrieve position with id: {}", s, e);

        } catch (IllegalArgumentException e) {
            errorLogger.error("Please provide a valid ticker symbol.", e);

        } catch (NoSuchElementException e) {
            errorLogger.error("Could not retrieve position with id: {} because it doesn't exist", s, e);

        }

        return Optional.of(position);
    }

    @Override
    public List<Position> findAll() {
        List<Position> positions = new ArrayList<>();

        try (Statement s = connection.createStatement();
             ResultSet rs = s.executeQuery(SELECT_ALL)) {

            while(rs.next()) {

                Position p = new Position();
                p.setTicker(rs.getString("symbol"));
                p.setNumOfShares(rs.getInt("number_of_shares"));
                p.setValuePaid(rs.getDouble("value_paid"));
                positions.add(p);

            }

        } catch (SQLException e) {
            errorLogger.error("Could not complete request: Find All Entities");
        }

        return positions;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {

        if (this.findById(s).isEmpty()) {
            throw new IllegalArgumentException("Could not find entity with id: " + s);
        }

        try (PreparedStatement ps = this.connection.prepareStatement(DELETE)) {
            ps.setString(1, s);
            ps.execute();
        } catch (SQLException e) {
            errorLogger.error("Could not delete position with id: {}", s, e);
        }

    }

    @Override
    public void deleteAll() {

        try (PreparedStatement ps = this.connection.prepareStatement(DELETE_ALL)) {
            ps.execute();
            infoLogger.info("DELETE ALL statement executed.");
        } catch (SQLException e) {
            errorLogger.error("Could not delete all entities", e);
        }
        
    }
}
