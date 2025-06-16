package org.felfeit;

import org.felfeit.util.ConnectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementTest {

    @Test
    void testExecuteStatement() throws SQLException {
        try (Connection conn = ConnectionUtil.getDataSource().getConnection();
             Statement stmt = conn.createStatement()) {

            boolean result = stmt.execute("SELECT * FROM contacts");
            Assertions.assertTrue(result);
        }
    }
}
