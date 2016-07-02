package ykt.ios4miui3.gavrique.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by satisfaction on 02.07.16.
 */
public class Db {

    private static Connection connection;

    public static void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:resources/db/gavrique.db");
        createTables();
    }

    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS gav_files " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " created           INTEGER   NOT NULL, " +
                " author            INTEGER   NOT NULL, " +
                " alias             CHAR(50)  NOT NULL, " +
                " path              CHAR(100) NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
    }
}
