package ca.jrvs.apps.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String>{
    private Connection connection;
    private final Logger logger = LoggerFactory.getLogger(PositionDao.class);

    private final String SELECT_ALL = "SELECT * FROM position";

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        return Optional.empty();
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
            logger.error("Could not complete request: Find All Entities");
        }
        return positions;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {

    }

    @Override
    public void deleteAll() {

    }
}
