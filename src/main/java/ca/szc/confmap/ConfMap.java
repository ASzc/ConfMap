package ca.szc.confmap;

import java.io.BufferedReader;
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
 * Access a single-namespace conf file though the Map interface TODO: Spec of accepted conf file format
 * 
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class ConfMap
    implements Map<String, String>
{

    private static final short DEBUG = 1;

    private static final Charset CHARSET = Charset.forName( "UTF-8" );

    private final Map<String, String> confMap;

    private final Path confFilePath;

    private final boolean readOnly;

    /**
     * Instantiate a ConfMap for confFilePath in read-only mode. Read-only mode will prevent write methods from changing
     * the file at confFilePath.
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
        LinkedHashMap<String, String> confMap = new LinkedHashMap<String, String>();

        try (BufferedReader reader = Files.newBufferedReader( confFilePath, CHARSET ))
        {
            Pattern confLinePattern = Pattern.compile( "^([^#][^=]*)=(.*)$" );
            String line;

            while ( ( line = reader.readLine() ) != null )
            {
                Matcher confLineMatcher = confLinePattern.matcher( line );
                boolean confLineMatch = confLineMatcher.matches();

                if ( confLineMatch )
                {
                    String key = confLineMatcher.group( 1 );
                    String value = confLineMatcher.group( 2 );

                    confMap.put( key, value );
                    if ( DEBUG > 0 )
                        System.out.println( "Accepted  K:" + key + "  V:" + value );
                }
                else if ( DEBUG > 0 )
                {
                    System.out.println( "Ignored Line: '" + line + "'" );
                }
            }
        }
        catch ( IOException x )
        {
            System.err.format( this.getClass().getName() + " IOException when reading confFilePath: %s%n", x );
            if ( DEBUG > 0 )
                x.printStackTrace();
        }

        this.readOnly = readOnly;
        this.confMap = confMap;
        this.confFilePath = confFilePath;
    }

    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsKey( Object key )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsValue( Object value )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String get( Object key )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String put( String key, String value )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String remove( Object key )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putAll( Map<? extends String, ? extends String> m )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub

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
