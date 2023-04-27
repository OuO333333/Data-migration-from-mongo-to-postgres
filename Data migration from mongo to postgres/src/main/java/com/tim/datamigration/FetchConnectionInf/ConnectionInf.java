package com.tim.datamigration.FetchConnectionInf;

/**
 * <Description>
 * connection information
 */
public class ConnectionInf {
    private String host;
    private String port;
    private String username;
    private String dbName;
    private String password;

    /**
     * <Description>
     * set host
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * <Description>
     * set port
     *
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * <Description>
     * set username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <Description>
     * set dbName
     *
     * @param dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * <Description>
     * set password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * <Description>
     * get host
     *
     * @return String
     */
    public String getHost() {
        return host;
    }

    /**
     * <Description>
     * get port
     *
     * @return String
     */
    public String getPort() {
        return port;
    }

    /**
     * <Description>
     * get username
     *
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * <Description>
     * get dbName
     *
     * @return String
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * <Description>
     * get password
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }
}
