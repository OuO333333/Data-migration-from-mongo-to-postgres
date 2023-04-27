package com.tim.datamigration.FetchConnectionInf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

/**
 * <Description>
 * connection function interface
 */
public interface ConnectionFunctionInterface {
    /**
     * <Description>
     * fetch connection information to ConnectionInf
     *
     * @param connectionInfPath
     * @return ConnectionInf
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException
     */
    ConnectionInf fetchConnectionInf(String connectionInfPath)
            throws FileNotFoundException, IOException, ParseException;
}
