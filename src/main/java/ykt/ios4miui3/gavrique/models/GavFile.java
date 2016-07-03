package ykt.ios4miui3.gavrique.models;

import java.sql.Date;

/**
 * Created by satisfaction on 02.07.16.
 */
public class GavFile {
    private int id;
    private Date created;
    private String author;
    private String alias;
    private String path;

    public void save() {

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
