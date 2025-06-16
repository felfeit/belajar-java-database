package org.felfeit;

import org.felfeit.util.ConnectionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLInjectionTest {
    @Test
    void testSqlInjectionPrevention() throws SQLException {
        String unsafeInput = "' OR '1'='1";

        String sql = "SELECT * FROM contacts WHERE name = ?";
        try (Connection conn = ConnectionUtil.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, unsafeInput);
            ResultSet rs = stmt.executeQuery();

            Assertions.assertFalse(rs.next());
        }
    }
}
