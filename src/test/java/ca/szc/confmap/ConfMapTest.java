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

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfMapTest
{

    private static Path TEST_CONF_FILE = Paths.get( "test-" + new Random().nextInt() + ".conf" );

    private static Charset CHARSET = Charset.forName( "UTF-8" );

    @Before
    public void setUp()
        throws Exception
    {
        String contents =
            "#commentedname=commentedvalue\nname1=#value1\nname2==value2\nname3=value3=\n"
                + "name4=value4 \nname5 =value5\n name6=value6\nname7= value7\nname8 =value8 \n"
                + " name9= value9\nname10=#value10 \nname11=value11#asd\nname12=value12";

        try (BufferedWriter writer = Files.newBufferedWriter( TEST_CONF_FILE, CHARSET ))
        {
            writer.write( contents, 0, contents.length() );
        }
    }

    @After
    public void tearDown()
        throws Exception
    {
        Files.delete( TEST_CONF_FILE );
    }

    /**
     * Test if the class has read the file and parsed the entries as expected
     */
    @Test
    public void testConfMapPath()
    {
        Map<String, String> goldMap = new LinkedHashMap<String, String>();
        goldMap.put( "name1", "#value1" );
        goldMap.put( "name2", "=value2" );
        goldMap.put( "name3", "value3=" );
        goldMap.put( "name4", "value4 " );
        goldMap.put( "name5 ", "value5" );
        goldMap.put( " name6", "value6" );
        goldMap.put( "name7", " value7" );
        goldMap.put( "name8 ", "value8 " );
        goldMap.put( " name9", " value9" );
        goldMap.put( "name10", "#value10 " );
        goldMap.put( "name11", "value11#asd" );
        goldMap.put( "name12", "value12" );

        try
        {
            // Set DEBUG > 0 before testing the class to enable debug output
            // Yes, we're modifying a private final field at runtime... *evil laughter*
            // http://stackoverflow.com/questions/3301635
            Field field = ConfMap.class.getDeclaredField( "DEBUG" );
            // Strip private
            field.setAccessible( true );
            // Strip final
            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt( field, field.getModifiers() & ~Modifier.FINAL );
            // Set value
            field.set(null, 2);
        }
        catch ( SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException x )
        {
            fail( "Exception '" + x + "' when setting DEBUG mode on ConfMap" );
        }

        ConfMap confMap = new ConfMap( TEST_CONF_FILE );
        assertEquals( "Entries stored in ConfMap don't match the gold Map's entries.", goldMap.entrySet(),
                      confMap.entrySet() );
    }

}
