package org.felfeit;

import org.felfeit.util.ConnectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementTest {

    @Test
    void testPreparedStatement() throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        String sql = "INSERT INTO contacts(name, phone, email) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, "aryax");
        preparedStatement.setString(2, "555");
        preparedStatement.setString(3, "aryax@test.com");

        int rows = preparedStatement.executeUpdate();

        Assertions.assertEquals(1, rows);
    }
}
