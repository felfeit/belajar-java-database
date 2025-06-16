package org.felfeit;

import org.felfeit.util.ConnectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class MetaDataTest {

    @Test
    void testMetaData() throws SQLException {
        Connection connection = ConnectionUtil.getDataSource().getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM contacts");
        ResultSetMetaData metaData = resultSet.getMetaData();
        Assertions.assertTrue(metaData.getColumnCount() >= 3);
    }
}
