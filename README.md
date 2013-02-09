# Java ConfMap - Config file I/O through the Map interface
This project contains a Java 7 class that provides access to a normal [UL]nix configuration file through the standard Java Map interface.

## Usage

Usage is programmatic only. Import the class **ca.szc.confmap.ConfMap** and create an instance of it. You will only need to use the **Map** interface methods in most cases. The backing file is only kept open for the duration of a method call, so there's nothing to close.

The default mode is **read-only**. In read-only mode, calling methods like **put()**--those that write information--will cause an **UnsupportedOperationException** to be thrown. To disable read-only mode, call the two parameter constructor ConfMap(Path, boolean) with a false boolean.

*Note: Write methods are not currently implemented*

If a ConfMap's backing file is edited externally after instantiation of the ConfMap, call the **reload()** method to replace the in-memory Map with what is in the file.

### Performance
The only times that file I/O will occur are: on instantiation of an object of the class, and when calling a write method. All other operations are in memory, and are served from a LinkedHashMap (a Hash Map implementation with a defined order to the entries).

If you need to add/update many, already known, keys at once, it may be more efficient to call **putAll()**, as this will only open the backing file once.

### Examples

The following opens **./demo.conf** via a ConfMap and prints out its key/value pairs

    import java.nio.file.Path;
    import java.nio.file.Paths;
    import ca.szc.confmap.ConfMap;

    public void demo1()
    {
        Path confFile = Paths.get( "demo.conf" );
        ConfMap confMap = new ConfMap( confFile );

        System.out.println( "Format: ('KEY','VALUE')" );
        for ( Entry<String, String> kvPair : confMap.entrySet() )
        {
            System.out.println( "('" + kvPair.getKey() + "','" + kvPair.getValue() + "')" );
        }
    }

The following opens **./settings.conf** via a ConfMap and adds a value to the file.

    import java.nio.file.Path;
    import java.nio.file.Paths;
    import ca.szc.confmap.ConfMap;

    public void demo2()
    {
        Path confFile = Paths.get( "settings.conf" );
        ConfMap confMap = new ConfMap( confFile );

        confMap.put("keyName", "valueData");
    }

## Supported File Format
Unfortunately there isn't a true standard for text configuration files. The format supported here could be described as INI sans multiple namespaces.

Each line should be formatted:

    <key>=<value>

with as many lines as you choose in the file. Later lines with the same key will overwrite the value any earlier line could have established.

The **key** string cannot contain the separator character, but the **value** string can. e.g.:

    foo==bar

could possibly mean the separator is in the key or the value. We eliminate this by saying that is valid for ('foo','=bar') but invalid for ('foo=','bar'). Some other valid uses of the separator character in a value are:

    foo=bar=
    foo=b=ar

Comments are supported, but only with the comment symbol at the start of a line. i.e. these are commented lines:

    #Hello, World!
    #foo=bar

, and these are not commented lines (the comment symbol is treated as a normal character):

    foo#=bar
    foo=bar#notreallyacomment

All spaces present in the key or value are included, even trailing spaces. e.g.:

    foo=bar #notreallyacomment
     cat=dog
    vim=emacs 

would produce respectively ('foo','bar #notreallyacomment'), (' cat','dog'), and ('vim','emacs ')

## Build

Build/packaging is accomplished via maven. To build, run:

    mvn package

which will create a jar in the directory **target/**

## Development

### Code Style

Maven internal code style is used for formatting the code. An XML file with the Eclipse formatting definitions is available in the repo root. You can read more about Maven's code style at this URL: https://maven.apache.org/developers/conventions/code.html

### Import Project to Eclipse

To import the cloned repository into Eclipse, the m2e "Maven integration in Eclipse" plug-in is required.

    Help > Install New Software ... > Work with: --All Available Sites-- > filter: m2e

After Eclipse restarts, you can import the repository into Eclipse.

    File > Import > Existing Maven Projects > Root Directory: <local repo location>

This method will correctly set up the build settings and import Maven dependencies automatically.

### Import Formatting for Project

After importing the cloned repository into Eclipse, we can add the Maven code style specifically for this project.

    Project > Properties > Java Code Style > Formatter > Enable Project Specific Settings > Import ...

Select maven-eclipse-codestyle.xml from the repo root, then OK. The active formatter profile for the project should then be Maven. Press OK again to save.
