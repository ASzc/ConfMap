package ca.szc.confmap;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
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
        String contents = "#commentedname=commentedvalue\nname1=value1\nname2=value2\nname3=value3";
        
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

    @Test
    public void testConfMapPath()
    {
        Map<String, String> goldMap = new LinkedHashMap<String, String>();
        goldMap.put( "name1", "value1" );
        goldMap.put( "name2", "value2" );
        goldMap.put( "name3", "value3" );

        ConfMap confMap = new ConfMap( TEST_CONF_FILE );
        assertEquals( "Entries stored in ConfMap don't match the gold Map's entries.", goldMap.entrySet(),
                      confMap.entrySet() );
    }

}
