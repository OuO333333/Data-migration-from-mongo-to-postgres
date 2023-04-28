package com.tim.timdev.datamigration.fetchconnectioninf;

/**
 * connection information
 */
public class ConnectionInf {
    private String host;
    private String port;
    private String username;
    private String dbName;
    private String password;

    /**
     * set host
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * set port
     *
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * set username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * set dbName
     *
     * @param dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * set password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get host
     *
     * @return String
     */
    public String getHost() {
        return host;
    }

    /**
     * get port
     *
     * @return String
     */
    public String getPort() {
        return port;
    }

    /**
     * get username
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * get dbName
     *
     * @return String
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * get password
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }
}
