package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class PositionDao_Test {
    DatabaseConnectionManager dcm;
    Connection connection;
    PositionDao dao;
    Position testPosition1;
    Position testPosition2;
    Position testPosition3;

    Logger logger = LoggerFactory.getLogger(PositionDao_Test.class);

    @BeforeEach
    void setUp() throws SQLException {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        testPosition1 = new Position();
        testPosition2 = new Position();
        testPosition3 = new Position();

        testPosition1.setTicker("IBM");
        testPosition1.setNumOfShares(34);
        testPosition1.setValuePaid(400.50);
        testPosition2.setTicker("MSFT");
        testPosition2.setNumOfShares(76);
        testPosition2.setValuePaid(1400.50);
        testPosition3.setTicker("AAPL");
        testPosition3.setNumOfShares(36);
        testPosition3.setValuePaid(12300.50);

        try {
            connection = dcm.getConnection();
            dao = new PositionDao(connection);
            dao.save(testPosition1);
            dao.save(testPosition2);
        } catch (SQLException e) {
            logger.error("Could not connect to db for test setup", e);
        }

    }

    @AfterEach
    void tearDown() throws SQLException {
        dao.deleteById(testPosition1.getTicker());
        dao.deleteById(testPosition2.getTicker());
        dao.deleteById(testPosition3.getTicker());
        connection.close();
    }

    @Test
    public void test_save() {
        System.out.print(dao.save(testPosition3));
    }

//    @Test
//    public void test_deleteById() {
//        dao.deleteById("IBM");
//        System.out.println(dao.findById("IBM"));
//    }
//
//    @Test
//    public void test_deleteAll() {
//        dao.deleteAll();
//
//        Assertions.assertTrue(dao.findById("IBM").isEmpty());
//        Assertions.assertTrue(dao.findById("MSFT").isEmpty());
//    }
}
