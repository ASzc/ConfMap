package ca.szc.confmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Access a conf file though the Map interface TODO: Spec of accepted conf file format
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class ConfMap
    implements Map<String, String>
{

    /**
     * Debug mode setting. Values > 0 mean debug is enabled.
     */
    private static final short DEBUG = 1;

    /**
     * Charset used by this class for reading and writing files
     */
    private static final Charset CHARSET = Charset.forName( "UTF-8" );

    /**
     * Regex Pattern used to parse a line of the conf file
     */
    private static final Pattern CONF_PATTERN = Pattern.compile( "^([^#][^=]*)=(.*)$" );

    /**
     * Path to the conf file which stores the persistent copy of the Map
     */
    private final Path confFilePath;

    /**
     * If true, any methods that cause writes will fail with UnsupportedOperationException
     */
    private final boolean readOnly;

    /**
     * In memory Map
     */
    private Map<String, String> confMap;

    /**
     * Instantiate a ConfMap for confFilePath in read-only mode. Read-only mode will prevent write methods having any
     * effect.
     * 
     * @param confFilePath
     */
    public ConfMap( Path confFilePath )
    {
        this( confFilePath, true );
    }

    /**
     * Instantiate a ConfMap for confFilePath with a read-only mode based on readOnly
     * 
     * @param confFilePath The location of the persistent conf file.
     * @param readOnly true will allow write methods to change the file at confFilePath.
     */
    public ConfMap( Path confFilePath, boolean readOnly )
    {
        this.confFilePath = confFilePath;
        this.readOnly = readOnly;
        reload();
    }

    /**
     * Replace in memory Map with contents from confFilePath given during instantiation
     * 
     * @return The old in memory Map that was replaced
     */
    public Map<String, String> reload()
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

        Map<String, String> oldMap = this.confMap;
        this.confMap = confMap;
        return oldMap;
    }

    @Override
    public int size()
    {
        return confMap.size();
    }

    @Override
    public boolean isEmpty()
    {
        return confMap.isEmpty();
    }

    @Override
    public boolean containsKey( Object key )
    {
        return confMap.containsValue( key );
    }

    @Override
    public boolean containsValue( Object value )
    {
        return confMap.containsValue( value );
    }

    @Override
    public String get( Object key )
    {
        return confMap.get( key );
    }

    @Override
    public String put( String key, String value )
    {
        if ( !readOnly )
        {
            // TODO
            return null;
        }
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @Override
    public String remove( Object key )
    {
        if ( !readOnly )
        {
            // TODO
            return null;
        }
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @Override
    public void putAll( Map<? extends String, ? extends String> m )
    {
        if ( !readOnly )
        {
            // TODO
        }
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @Override
    public void clear()
    {
        if ( !readOnly )
        {
            // Set in-memory and persistent both to no entries
            confMap.clear();
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
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @Override
    public Set<String> keySet()
    {
        return confMap.keySet();
    }

    @Override
    public Collection<String> values()
    {
        return confMap.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet()
    {
        return confMap.entrySet();
    }

}
