package ykt.ios4miui3.gavrique.models;

import ykt.ios4miui3.gavrique.Core.Logger;
import ykt.ios4miui3.gavrique.Main;
import ykt.ios4miui3.gavrique.db.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satisfaction on 02.07.16.
 */
public class GavFile {
    private int id = 0;
    private Date created;
    private String author;
    private String alias;
    private String path;

    public GavFile(String author, String alias, String path) {
        this.created = new Date(new java.util.Date().getTime());
        this.author = author;
        this.alias = alias;
        this.path = path;
    }

    public static GavFile get(int id) {
        Connection connection = null;
        try {
            connection = Db.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select id, created, author, alias, path from gav_files WHERE id=" + id);
            if (rs.next()) {
                GavFile gavFile = new GavFile(rs.getString(3), rs.getString(4), rs.getString(5));
                gavFile.setId(rs.getInt(1));
                gavFile.setCreated(new Date(rs.getLong(2)));
                return gavFile;
            }
            rs.close();
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        } finally {
            Db.closeConnection(connection);
        }
        return null;
    }

    public static GavFile getByAlias(String alias) {
        Connection connection = null;
        try {
            connection = Db.getConnection();
            PreparedStatement st = connection.prepareStatement("select id, created, author, path from gav_files WHERE alias=?");
            st.setString(1, alias);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                GavFile gavFile = new GavFile(rs.getString(3), alias, rs.getString(4));
                gavFile.setId(rs.getInt(1));
                gavFile.setCreated(new Date(rs.getLong(2)));
                return gavFile;
            }
            rs.close();
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        } finally {
            Db.closeConnection(connection);
        }
        return null;
    }

    public static List<GavFile> list() {
        Connection connection = null;
        List<GavFile> list = new ArrayList<>();
        try {
            connection = Db.getConnection();
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select id, created, author, alias, path from gav_files");

            while (rs.next()) {
                GavFile gavFile = new GavFile(rs.getString(3), rs.getString(4), rs.getString(5));
                gavFile.setId(rs.getInt(1));
                gavFile.setCreated(new Date(rs.getLong(2)));
                list.add(gavFile);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        } finally {
            Db.closeConnection(connection);
        }
        return list;
    }

    public void save() {
        Connection connection = null;
        try {
            connection = Db.getConnection();
            if (id > 0) {
                PreparedStatement st = connection.prepareStatement("update gav_files " +
                        " set created=?, author=?, alias=?, path=? where id=?");
                st.setLong(1, created.getTime());
                st.setString(2, author);
                st.setString(3, alias);
                st.setString(4, path);
                st.setInt(5, id);
                st.executeUpdate();
                st.close();
            } else {
                PreparedStatement st = connection.prepareStatement("insert into gav_files " +
                        "(created, author, alias, path) VALUES (?, ?, ?, ?)");
                st.setLong(1, created.getTime());
                st.setString(2, author);
                st.setString(3, alias);
                st.setString(4, path);
                st.executeUpdate();
                st.close();
            }
        } catch (SQLException e) {
            Logger.get().error("save error", e);
        } finally {
            Db.closeConnection(connection);
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

    public String getFullPath() {
        return Main.FILES_PATH + Main.PATH_SEPARATOR + path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
