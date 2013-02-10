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

package ca.szc.confmap;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import ca.szc.confmap.fileformats.FileFormat;

/**
 * Access a conf file though the Map interface
 */
public class ConfMap
    implements Map<String, String>
{

    /**
     * Path to the conf file which stores the persistent copy of the Map
     */
    private final Path filePath;

    /**
     * If true, any methods that cause writes will fail with UnsupportedOperationException
     */
    private final boolean readOnly;

    /**
     * In memory Map
     */
    private Map<String, String> confMap;

    /**
     * Format I/O object of the backing file
     */
    private FileFormat fileIO;

    /**
     * Instantiate a ConfMap for confFilePath in read-only mode. Read-only mode will prevent write methods having any
     * effect.
     * 
     * @param confFilePath
     */
    public ConfMap( Path confFilePath, FileFormat format )
    {
        this( confFilePath, true, format );
    }

    /**
     * Instantiate a ConfMap for confFilePath with a read-only mode based on readOnly
     * 
     * @param confFilePath The location of the persistent conf file.
     * @param readOnly true will allow write methods to change the file at confFilePath.
     */
    public ConfMap( Path confFilePath, boolean readOnly, FileFormat format )
    {
        this.filePath = confFilePath;
        this.readOnly = readOnly;
        this.fileIO = format;
        reload();
    }

    /**
     * Replace in memory Map with contents from confFilePath given during instantiation
     * 
     * @return The old in memory Map that was replaced
     */
    public Map<String, String> reload()
    {
        Map<String, String> oldMap = confMap;
        confMap = fileIO.readAll( filePath );
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
            confMap.put( key, value );
            return fileIO.writeOne( filePath, key, value );
        }
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @Override
    public String remove( Object key )
    {
        if ( !( key instanceof String ) )
        {
            throw new IllegalArgumentException( "key must be of type String" );
        }

        if ( !readOnly )
        {
            confMap.remove( key );
            return fileIO.removeOne( filePath, (String) key );
        }
        else
        {
            throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
        }
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void putAll( Map<? extends String, ? extends String> map )
    {
        if ( map.size() > 0 )
        {
            if ( !( map.keySet().toArray()[0] instanceof String ) )
            {
                throw new IllegalArgumentException( "Map keys must be of type String" );
            }
            else if ( !( map.values().toArray()[0] instanceof String ) )
            {
                throw new IllegalArgumentException( "Map values must be of type String" );
            }

            if ( !readOnly )
            {
                confMap.putAll( map );
                fileIO.writeAll( filePath, (Map<String, String>) map );
            }
            else
            {
                throw new UnsupportedOperationException( this.getClass().getName() + " read-only mode is active" );
            }
        }
    }

    @Override
    public void clear()
    {
        if ( !readOnly )
        {
            // Set in-memory and persistent both to no entries
            confMap.clear();
            fileIO.removeAll( filePath );
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
