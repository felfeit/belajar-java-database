package org.felfeit.repository;

import org.felfeit.model.Contact;
import org.felfeit.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactRepository implements IContactRepository {

    public ContactRepository() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS contacts (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100)
            )""";

    private static final String SELECT_ALL_SQL = "SELECT * FROM contacts";
    private static final String INSERT_SQL = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM contacts WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM contacts WHERE id = ?";

    @Override
    public Contact save(Contact contact) {
        try (Connection conn = ConnectionUtil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getPhone());
            stmt.setString(3, contact.getEmail());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating contact failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating contact failed, no ID obtained.");
                }
            }

            return contact;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Contact> findById(int id) {
        try(Connection connection = ConnectionUtil.getDataSource().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToContact(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find contact by id: " + id, e);
        }
    }

    @Override
    public List<Contact> findAll() {
        List<Contact> contacts = new ArrayList<>();

        try (Connection connection = ConnectionUtil.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            while (resultSet.next()) {
                contacts.add(mapResultSetToContact(resultSet));
            }

            return contacts;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all contacts", e);

        }
    }

    @Override
    public Contact update(Contact contact) {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhone());
            statement.setString(3, contact.getEmail());
            statement.setLong(4, contact.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating contact failed, no rows affected.");
            }

            return contact;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update contact", e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection connection = ConnectionUtil.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting contact failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete contact with id: " + id, e);
        }
    }

    private Contact mapResultSetToContact(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getInt("id"));
        contact.setName(resultSet.getString("name"));
        contact.setPhone(resultSet.getString("phone"));
        contact.setEmail(resultSet.getString("email"));
        return contact;
    }
}
