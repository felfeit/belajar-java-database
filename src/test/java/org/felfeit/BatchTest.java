package org.felfeit;

import org.felfeit.util.ConnectionUtil;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BatchTest {

    @Test
    void testBatchStatement() throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        String sql = "INSERT INTO contacts(name, phone, email) VALUES ('aryowiguna', '0987654321', 'aryowiguna@test.com')";

        for(int i = 0; i < 10; i++) {
            statement.addBatch(sql);
        }

        statement.executeBatch();

        statement.close();
        connection.close();
    }

    @Test
    void testBatchPreparedStatement() throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        String sql = "INSERT INTO contacts(name, phone, email) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for(int i = 0; i < 10; i++) {
            preparedStatement.clearParameters();
            preparedStatement.setString(1, "ayra");
            preparedStatement.setString(2, "0192837465");
            preparedStatement.setString(3, "ayra@test.com");
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();

        preparedStatement.close();
        connection.close();
    }
}
