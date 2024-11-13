package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ca.jrvs.apps.jdbc.util.DataAccessObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class CustomerDAO extends DataAccessObject<Customer> {
    public CustomerDAO(Connection connection) {
        super(connection);
    }
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    private static final String INSERT = "INSERT INTO customer (first_name, last_name, email, " +
            "phone, address, city, state, zipCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT customer_id, first_name, last_name, email, " +
            "phone, address, city, state, zipCode FROM customer WHERE customer_id = ?";
    private static final String UPDATE = "UPDATE customer SET first_name = ?, last_name = ?, " +
            "email = ?, phone = ?, address = ?, city = ?, state = ?, zipCode = ? WHERE customer_id = ?";
    private static String DELETE = "DELETE FROM customer WHERE customer_id = ?";

    @Override
    public Customer findById(long id) {
        Customer customer = new Customer();
        try(PreparedStatement ps = this.connection.prepareStatement(GET_ONE);) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                customer.setId(rs.getLong("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setZipCode(rs.getString("zipCode"));
            }
        } catch (SQLException e) {
            CustomerDAO.logger.error("Could not complete find!", e);
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Customer update(Customer dto) {
        Customer customer = null;
        try(PreparedStatement ps = this.connection.prepareStatement(UPDATE);) {
            ps.setString(1, dto.getFirstName());
            ps.setString(2, dto.getLastName());
            ps.setString(3, dto.getEmail());
            ps.setString(4, dto.getPhone());
            ps.setString(5, dto.getAddress());
            ps.setString(6, dto.getCity());
            ps.setString(7, dto.getState());
            ps.setString(8, dto.getZipCode());
            ps.setLong(9, dto.getId());
            ps.execute();
            customer = this.findById(dto.getId());
        } catch (SQLException e) {
            CustomerDAO.logger.error("Could not complete update!", e);
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public Customer create(Customer dto) {
        try(PreparedStatement ps = this.connection.prepareStatement(INSERT);){
            ps.setString(1, dto.getFirstName());
            ps.setString(2, dto.getLastName());
            ps.setString(3, dto.getEmail());
            ps.setString(4, dto.getPhone());
            ps.setString(5, dto.getAddress());
            ps.setString(6, dto.getCity());
            ps.setString(7, dto.getState());
            ps.setString(8, dto.getZipCode());
            ps.execute();
            int id = this.getLastVal(CUSTOMER_SEQUENCE);
            return this.findById(id);
        } catch (SQLException e) {
            CustomerDAO.logger.error("Failed to create customer", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement ps = this.connection.prepareStatement(DELETE);){
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            CustomerDAO.logger.error("Failed to delete customer", e);
            throw new RuntimeException(e);
        }
    }
}
