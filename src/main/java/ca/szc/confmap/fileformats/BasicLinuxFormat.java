/* 
 * Copyright (c) 2013 Alexander Szczuczko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.szc.confmap.fileformats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicLinuxFormat
    implements FileFormat
{

    /**
     * Debug mode setting. Values > 0 mean debug is enabled. Declared via parseInt to prevent static analysis from
     * flagging unused debug output when it is 0.
     */
    private static final int DEBUG = Integer.parseInt( "0" );

    /**
     * Charset used by this class for reading and writing files
     */
    private static final Charset CHARSET = Charset.forName( "UTF-8" );

    /**
     * Regex Pattern used to parse a line of the conf file
     */
    private static final Pattern CONF_PATTERN = Pattern.compile( "^([^#][^=]*)=(.*)$" );

    @Override
    public Map<String, String> readAll( Path confFilePath )
    {
        LinkedHashMap<String, String> confMap = new LinkedHashMap<String, String>();

        try (BufferedReader reader = Files.newBufferedReader( confFilePath, CHARSET ))
        {
            String line;

            while ( ( line = reader.readLine() ) != null )
            {
                Matcher confLineMatcher = CONF_PATTERN.matcher( line );
                boolean confLineMatch = confLineMatcher.matches();

                if ( confLineMatch )
                {
                    String key = confLineMatcher.group( 1 );
                    String value = confLineMatcher.group( 2 );

                    confMap.put( key, value );
                    if ( DEBUG > 0 )
                        System.out.println( this.getClass().getName() + " Accepted  K:" + key + "  V:" + value );
                }
                else if ( DEBUG > 0 )
                {
                    System.out.println( this.getClass().getName() + " Ignored Line: '" + line + "'" );
                }
            }
        }
        catch ( IOException x )
        {
            System.err.format( this.getClass().getName() + " IOException when reading confFilePath: %s%n", x );
            if ( DEBUG > 0 )
                x.printStackTrace();
        }

        return confMap;
    }

    @Override
    public String writeOne( Path confFilePath, String key, String value )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeAll( Path confFilePath, Map<String, String> newEntries )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String removeOne( Path confFilePath, String key )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeAll( Path confFilePath )
    {
        try (BufferedWriter writer = Files.newBufferedWriter( confFilePath, CHARSET ))
        {
            String empty = "";
            writer.write( empty, 0, empty.length() );
        }
        catch ( IOException x )
        {
            System.err.format( this.getClass().getName() + " IOException when clearing confFilePath: %s%n", x );
            if ( DEBUG > 0 )
                x.printStackTrace();
        }
    }

}
