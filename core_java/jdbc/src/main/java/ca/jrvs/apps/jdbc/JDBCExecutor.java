package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import ca.jrvs.apps.stockquote.service.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExecutor {
    private final static Logger logger = LoggerFactory.getLogger(JDBCExecutor.class);

    public static void main(String... args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "hplussport", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            CustomerDAO customerDAO = new CustomerDAO(connection);
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Adams");
            customer.setEmail("jadams.wh.gov");
            customer.setAddress("1234 Main St");
            customer.setCity("Arlington");
            customer.setState("VA");
            customer.setPhone("(555) 555-9845");
            customer.setZipCode("01234");

            Customer dbCustomer = customerDAO.create(customer);
            System.out.println(dbCustomer);
            dbCustomer = customerDAO.findById(dbCustomer.getId());
            System.out.println(dbCustomer);
            dbCustomer.setEmail("john.adams@wh.gov");
            dbCustomer = customerDAO.update(dbCustomer);
            System.out.println(dbCustomer);
            customerDAO.delete(dbCustomer.getId());
        } catch (SQLException e) {
            JDBCExecutor.logger.error("Could not process main", e);
        }

    }
}
