package com.tim.datamigration.FetchConnectionInf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.*;

public interface ConnectionFunctionInterface {
    ConnectionInf fetchConnectionInf(String connectionInfPath)
            throws FileNotFoundException, IOException, ParseException;
}
