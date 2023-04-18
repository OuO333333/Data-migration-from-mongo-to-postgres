package com.tim.datamigration.FetchConnectionInf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ConnectionFunctionImplement implements ConnectionFunctionInterface {
    // connection information to ConnectionInf
    public ConnectionInf fetchConnectionInf(String connectionInfPath)
            throws FileNotFoundException, IOException, ParseException {
        Object ob = new JSONParser().parse(new FileReader(connectionInfPath));
        // typecasting ob to JSONObject
        JSONObject js = (JSONObject) ob;
        String host = (String) js.get("host");
        String port = String.valueOf(js.get("port"));
        String username = (String) js.get("username");
        String dbName = (String) js.get("db_name");
        String password = (String) js.get("password");
        ConnectionInf connectionInf = new ConnectionInf();
        connectionInf.setHost(host);
        connectionInf.setPort(port);
        connectionInf.setUsername(username);
        connectionInf.setDbName(dbName);
        connectionInf.setPassword(password);
        return connectionInf;
    }

}
