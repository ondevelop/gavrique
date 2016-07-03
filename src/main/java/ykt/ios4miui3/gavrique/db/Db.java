package ykt.ios4miui3.gavrique.db;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by satisfaction on 02.07.16.
 */
public class Db {

    private static SQLiteDataSource dataSource;

    public static void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:resources/db/gavrique.db");
        dataSource.getConnection().close();
        createTables();
    }


    private static void createTables() throws SQLException {
        Connection c = getConnection();
        Statement stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS gav_files " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " created           INTEGER   NOT NULL, " +
                " author            CHAR(50)   NOT NULL, " +
                " alias             CHAR(50)  UNIQUE NOT NULL, " +
                " path              CHAR(100) NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
        closeConnection(c);
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        return conn;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.err.println("coonection free error " + ex);
            }
        }
    }
}
