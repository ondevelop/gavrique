package ykt.ios4miui3.gavrique.models;

import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.db.Db;

import java.sql.*;

/**
 * Created by satisfaction on 02.07.16.
 */
public class GavFile {
    private int id = 0;
    private Date created;
    private String author;
    private String alias;
    private String path;

    public static GavFile get(int id) {
        try {
            Connection connection = Db.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select created, author, alias, path from gav_files WHERE id=" + id);
            if (rs.next()) {
                GavFile gavFile = new GavFile();
                gavFile.setCreated(new Date(rs.getLong(1)));
                gavFile.setAuthor(rs.getString(2));
                gavFile.setAlias(rs.getString(3));
                gavFile.setPath(rs.getString(4));
                return gavFile;
            }
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        }
        return null;
    }

    public void save() {
        if (id > 0) {
            //todo update
        }
        try {
            Connection connection = Db.getConnection();
            PreparedStatement st = connection.prepareStatement("insert into gav_files " +
                    "(created, author, alias, path) VALUES (?, ?, ?, ?)");
            st.setLong(1, created.getTime());
            st.setString(2, author);
            st.setString(3, alias);
            st.setString(4, path);
            st.executeUpdate();
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
