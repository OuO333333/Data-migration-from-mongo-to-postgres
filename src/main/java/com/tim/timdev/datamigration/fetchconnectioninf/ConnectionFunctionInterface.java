package com.tim.timdev.datamigration.fetchconnectioninf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

/**
 * ConnectionFunction interface
 */
public interface ConnectionFunctionInterface {
    /**
     * fetch connection information
     *
     * @param connectionInfPath
     * @return ConnectionInf
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException
     */
    ConnectionInf fetchConnectionInf(String connectionInfPath)
            throws IOException, ParseException;
}
