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

import java.nio.file.Path;
import java.util.Map;

/**
 * Defines the interface needed from a file to convert the data to and from a Map
 * 
 * @param <K> Key type
 * @param <V> Value type
 */
public interface FileFormat
{

    /**
     * Read all entries from the file to the returned Map
     * 
     * @param confFilePath Path to the compatible file
     * @return The Map of all the file entries
     */
    public Map<String, String> readAll( Path filePath );

    /**
     * Replace/add one of the file entries from the given entry data
     * 
     * @param confFilePath Path to the compatible file
     * @param key
     * @param value Path to the compatible file
     * @return The previous value associated with the key, if any
     */
    public String writeOne( Path filePath, String key, String value );

    /**
     * Replace/add more than one of the file entries from the given Map
     * 
     * @param confFilePath Path to the compatible file
     * @param map The entries to insert/update in the file
     */
    public void writeAll( Path filePath, Map<String, String> map );

    /**
     * Remove one of the file entries from the given entry key
     * 
     * @param confFilePath Path to the compatible file
     * @param key The key whose mapping is to be removed from the map
     * @return The key whose mapping is to be removed from the map
     */
    public String removeOne( Path filePath, String key );

    /**
     * Remove of the file entries
     * 
     * @param confFilePath Path to the compatible file
     */
    public void removeAll( Path filePath );

}
