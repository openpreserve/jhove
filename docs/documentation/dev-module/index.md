---
title: Writing a module
layout: page
---

# Writing a JHOVE Module (*draft*, 2005-02-07)

{: #module-interface .section-heading}
## 1 The Module Interface

All JHOVE modules implement the `module` interface. (Details of all interfaces and classes are available [here](/javadoc/).)

```java
package edu.harvard.hul.ois.jhove;
import java.io.*;
import java.util.*;

public interface Module
{
    public void init  (String init)  throws Exception;
    public void param (String param) throws Exception;
    public void setApp (App app);
    public void setBase (JhoveBase je);
    public void setVerbosity (int verbosity);

    public String getName ();
    public String getRelease ();
    public Date   getDate ();
    public String [] getFormat ();
    public String getCoverage ();
    public String [] getMimeType ();
    public List   getSpecification ();
    public List   getSignature ();
    public String getWellFormedNote ();
    public String getValidityNote ();
    public String getRepInfoNote ();
    public Agent  getVendor ();
    public String getNote ();
    public String getRights ();
    public boolean isRandomAccess ();
    public boolean hasFeature (String feature);
    public List getFeatures ();

    public void checkSignatures (File file, InputStream stream,   RepInfo info) throws IOException;
    public void checkSignatures (File file, RandomAccessFile raf, RepInfo info) throws IOException;

    public int parse (InputStream stream,  RepInfo info, int parseIndex) throws IOException;
    public int parse (RandomAccessFile raf, RepInfo info) throws IOException;

    public void show (OutputHandler handler);
}
```

### 1.1 Initialization Methods

```java
public void init  (String init)  throws Exception;
public void param (String param) throws Exception;
```

> The `init()` method is invoked once at the time the module class is instantiated, passing the argument optionally specified in the configuration file for this module, or `null`.

```xml
...
<module>
  <class>fully-package-qualified-module-class-name</class>
  [ <init>optional-module-init-argument</init> ]
  [ <param>optional-module-parameter</param> ]
  ...
</module>
...
```

> The `param()` method is invoked once every time the module object is invoked, passing an argument specified by the `-p param` command line option, or `null`.

### 1.2 Mutator Methods

```java
public void setApp (App app);
```

> The `setApp()` method passes the application state object to the module.

```java
public void setBase (JhoveBase je);
```

> The `setBase()` method passes the application state object to the module. The `JhoveBase` object provides the module with state information about the surrounding context from which the module is invoked.

```java
public void setVerbosity (int verbosity);
```

> The `setVerbosity()` method specifies the level of verbosity of object representation information that the module should report via the `RepInfo` object returned by the `parse()` method. Each module can decide what representation information should be displayed for each level.

| verbosity                     | Value | Level                  |
|-------------------------------|-------|------------------------|
| `Module.MAXIMUM_VERBOSITY`    | 1     | Maximum verbosity      |
| `Module.MINIMUM_VERBOSITY`    | 2     | Minimum verbosity (default) |

### 1.3 Accessor Methods

```java
public String getName ();
public String getRelease ();
public String getCoverage ();
public String getWellFormedNote ();
public String getValidityNote ();
public String getRepInfoNote ();
public String getNote ();
public String getRights ();
public List getFeatures ();
```

> These methods return scalar `String`-valued module descriptive information: module name, release identifier, format coverage, methodological notes on well-formedness, validity, and representation information, general informative note, and intellectual property rights statement.

```java
public Date getDate ();
```

> The `getDate()` method returns the module release date.

```java
public Agent getVendor ();
```

> The `getVendor()` method returns an `Agent` object describing the module vendor.

```java
public String [] getFormat ();
public String [] getMimeType ();
```

> These methods return arrays of `String`-valued module descriptive information: variant format names and MIME types associated with the format.

```java
public List getSpecification ();
public List getSignature ();
```

> These methods return `List` containers of `Document` and `Signature` objects respectively. The documents are specification documents for the format used to construct the module. The signatures are the internal and external format signatures recognized by the module.

```java
public boolean isRandomAccess ();
```

> The `isRandomAccess()` method must return `true` if parsing of formatted-objects requires random access to the object content stream. The method should return `false` if the parsing can occur on a stream access basis.

```java
public List getFeatures ();
```

> This method returns a List of Strings identifying the features of the Module. See the discussion of Module features further on.

### 1.4 Parse Methods

```java
public void checkSignatures (InputStream stream,    RepInfo info) throws IOException;
public void checkSignatures (RandomAccessFile raf, RepInfo info) throws IOException;
```

> The `checkSignatures()` methods attempt to identify the object (represented as either a stream or random access file) using only internal signatures, i.e., magic numbers. Representation information about the object is returned through the `RepInfo` object.

```java
public int parse (InputStream stream,    RepInfo info, int parseIndex) throws IOException;
public int parse (RandomAccessFile file, RepInfo info) throws IOException;
```

> The parse() methods parse the object (represented by either a stream or random access file). Representation information about the object is returned through the RepInfo object. The stream version of parsemay be invoked multiple times, if it is necessary to do multiple passes on the data. On the first invocation of this method parseIndex is set to 0. If the method returns a non-zero value then it is invoked again, with parseIndex set to the return value.
>
> > ```java
> > ...
> > RepInfo info;
> > int parseIndex = 0;
> > while ((parseIndex = parse (..., info, parseIndex)) != 0);
> > ...
> > ```
> >
> > The parse method for a RandomAccessFile does not have this feature, and does not have a parseIndex parameter, since it is always possible to move back to a previously examined position in the file.

### 1.5 Descriptive Methods

```java
public void show (Output handler);
```

> The `show()` method uses the specified output handler to display descriptive information about the module itself, including module name, release identifier, build date, format names, MIME types, coverage statement, specifications, signatures, methodology statements, vendor, rights statement, and notes.

{: #modulebase-class .section-heading}
## 2 ModuleBase Class

The `Module` interface is implemented by the abstract `ModuleBase` class from which all JHOVE modules are extended. The class provides concrete implementations of the initialization, mutator, and accessor methods, and the `show()` method.

A new module *must* override the stub methods `checkSignature()` and `parse()`.

> ```java
> package edu.harvard.hul.ois.jhove;
> import java.io.*;
> import java.security.*;
> import java.util.*;
> import java.util.zip.*;
>
> public abstract class ModuleBase
>     implements Module
> {
>     protected ModuleBase (String name, String release, int [] date, String [] format,
>                           String coverage, String [] mimeType, String wellFormedNote,
>                           String validityNote, String repInfoNote, String note,
>                           String rights, boolean isRandomAccess)
>     {
>         ...
>     }
>     public void checkSignature (File file, ..., RepInfo info) throws IOException
>     {                /* Do nothing */
>     }
>     public int  parse (..., RepInfo info, int parseIndex) thows IOException
>     {
>         return 0;    /* Do nothing */
>     }
>     protected void initParse () { ... }
>
>     public static DataInputStream getBufferedDataStream (InputStream stream, int size) { ... }
>
>     public static int readUnsignedByte (DataInputStream stream, ModuleBase counted) { ... }
>     public static int readUnsignedByte (RandomAccessFile file) { ... }
>     public static void readByteBuf (DataInputStream stream, byte [] buf, ModuleBase counted) { ...}
>     public static int readSignedByte (DataInputStream stream, ModuleBase counted) { ...}
>     public static int readSignedByte (RandomAccessFile file) { ... }
>     public static int readUnsignedShort (DataInputStream stream, boolean bigEndian, ModuleBase counted) { ... }
>     public static int readUnsignedShort (RandomAccessFile file,  boolean bigEndian) { ... }
>     public static int readSignedShort (DataInputStream stream, boolean endian, ModuleBase counted) { ... }
>     public static int readSignedShort (RandomAccessFile file,  boolean endian) { ...}
>     public static long readUnsignedInt (DataInputStream stream, boolean bigEndian, ModuleBase counted) { ... }
>     public static long readUnsignedInt (RandomAccessFile file,  boolean bigEndian) { ... }
>     public static int readSignedInt (DataInputStream stream, boolean endian, ModuleBase counted) { ... }
>     public static int readSignedInt (RandomAccessFile file,  boolean endian) { ...}
>     public static long readSignedLong (DataInputStream stream, boolean bigEndian, ModuleBase counted) { ... }
>     public static long readSignedLong (RandomAccessFile file, boolean bigEndian) { ... }
>     public static float readFloat (DataInputStream stream, boolean endian, ModuleBase counted) { ... }
>     public static float readFloat (RandomAccessFile file,  boolean endian) { ... }
>     public static double readDouble (DataInputStream stream, boolean endian, ModuleBase counted) { ... }
>     public static double readDouble (RandomAccessFile file,  boolean endian) { ... }
>     public static Rational readUnsignedRational (DataInputStream stream, boolean endian, ModuleBase counted) { ... }
>     public static Rational readUnsignedRational (RandomAccessFile file,  boolean endian) { ... }
>     public static Rational readSignedRational (RandomAccessFile file, boolean endian)
> }
> ```
>

 The ModuleBase class defines a number of static convenience methods for type-specific reading of random access files and input streams.

```java
public static DataInputStream getBufferedDataStream (InputStream stream, int size)
```

> This is a convenience method for converting a generic InputStream into a DataInputStream as required by the convenience reading methods. The new stream is buffered for optimized performance. If the value of 0 is specified for the size argument then the default JRE buffer size is used.

{: #module-construction .section-heading}
## 3 New Module Construction

{: #module-name .section-heading}
### 3.1 Module name

Module names should consist of two parts, an uppercase format name and a lowercase vendor name, separated by a hyphen:

> ```text
> FORMAT-vendor
> ```

The format and vendor names should be abbreviated, if necessary. For example:

> ```text
> ASCII-hul
> ```

is the name for the ASCII module created by the Harvard University Library.

### 3.2 Module class name

A JHOVE module is encapsulated in one or more classes. The main module class name should be based on the format that the module supports:

> ```java
> public class FormatModule { ... }
> ```

For example:

> ```java
> public class AsciiModule { ... }
> ```

is the class name for the ASCII module created by the Harvard University Library.

### 3.3 Installing a module

Module classes must be in the classpath used by JHOVE. In addition, they must be specified in the configuration file. A configuration file will include several `<module>` elements; you simply have to add an appropriate element for the module class you have created, using the following pattern.

> ```xml
> ...
> <module>
>   <class>fully-package-qualified-class-name</class>
>   <init>optional-initialization-argument</init>
> </module>
> ...
> ```

where the initialization parameter is optional. If defined, it will be passed to the module's init() method once at the time the module class object is instantiated.

The position of the module's definition in the configuration file is significant; modules will be applied in the order in which they appear in the configuration file. Since a document will be matched by the first module it satisfies, modules for specific format files should appear before more general ones. For example, if your module verifies XHTML documents, the element declaring it should appear before the element declaring the XML module, since all valid XHTML documents are also XML documents.

If you install a new module, you must restart JHOVE for the new module to be usable.

### 3.4 Making a module class

All format modules *must* extend the `ModuleBase` class.

The constructor for a module takes no parameters. It *must* first invoke its the superclass constructor for passing in arguments defining the static descriptive information about the module. The optional `WELLFORMED`, `VALIDITY`, `REPINFO`, methodology notes and the informative `NOTE` may be set to `null` if appropriate.

> ```java
> import edu.harvard.hul.ois.jhove.*;
> import java.io.*;
> import java.util.*;
> public class FormatModule
>     extends ModuleBase
> {
>     private static final String    NAME       =  "FORMAT-vendor";
>     private static final String    RELEASE    =  "major.minor";
>     private static final int    [] DATE       = {yyyy, mm, dd};
>     private static final String [] FORMAT     = {"format", ...};
>     private static final String [] MIMETYPE   = {"mime", ...};
>     private static final String    WELLFORMED =  "note";
>     private static final String    VALIDITY   =  "note";
>     private static final String    REPINFO    =  "note";
>     private static final String    NOTE       =  "note";
>     private static final String    RIGHTS     =  "statement";
>     private static final boolean   RANDOM     =   flag;
>
>     public FormatModule ()
>     {
>         super (NAME, RELEASE, DATE, FORMAT, COVERAGE, MIMETYPE, WELLFORMED,
>                VALIDITY, REPINFO, NOTE, RIGHTS, RANDOM);
>         ...
>     }
>
>     public void checkSignature (File file, ..., RepInfo info) { ... }
>     public int  parse (..., RepInfo info, int parseIndex) { ... }
>     ...
> }
> ```

### 3.4.1 Module constructor arguments

`private static final String NAME`

> The module name as described [above](#module-name).

`private static final String RELEASE`

> The module release identifier, typically formatted as a major and minor release number: `major.minor`, e.g. `"10.3"` for release 10.3.

`private static final int [] DATE`

> An array of three integers specifying the year, month, and day module release, e.g. `{2004, 4, 12}` for a April 12, 2004, release date.

`private static final String [] FORMAT`

> An array of names for the formats supported by the module. The first entry should be be most generally appropriate format name, e.g. `{"TIFF, "Tagged Image File Format", "TIFF/EP", "TIFF/IT", ...}`.

`private static final String [] MIMETYPE`

> An array of MIME types applicable for the formats supported by the module. The first entry should be be most generally appropriate MIME type, e.g. `{image/tiff}`.

`private static final String COVERAGE`

> A comma-separated list of format profiles supported by the module, e.g. `"TIFF, TIFF/IT (ISO 12639:2003), TIFF/EP (ISO 12234-2:2001), Exif 2.2 (JEITA CP-3451), ..."`.

`private static final String VALIDITY`

> Optional statement of validity methodology used by the module, or `null`.

`private static final String REPINFO`

> Optional description of special properties of the representation information returned by this module, or `null`.

`private static final String NOTE`

> Optional informative note about the module, or `null`.

`private static final String RIGHTS`

> Intellectual property rights statement for the module. Typically this will include a copyright notice and summary of the license terms under which the module is available.

`private static final boolean RANDOM`

> Random access flag: `true` for modules that require random access to objects, in which case the method `parse(RandomAccessFile file, ...)` *must* be defined; `false` for modules that accept stream access to objects, in which case the method `parse(InputStream stream, ...)` *must* be defined.

The `ModuleBase` constructor defines `_specification` as an initially empty List of `Document` objects which give information about the specification of the format as treated by the `Module`. The module constructor may define `Document` objects for this purpose and add these objects to `_specification`.

The `ModuleBase` constructor defines `_signature` as an initially empty List of `Signature` objects which allow quick identification of documents that claim to conform to the module's format. The module constructor may define `Signature` objects and add these objects to `_signature`.

A Jhove module may be either stream-based or random-access. The choice depends on the expectations contained in the file format. A file format which is designed to be read from beginning to end, and which does not contain pointers to specific file offsets, is best handled by a stream module. A file format which contains pointers to file locations, or which otherwise cannot be read in sequence, is best handled by a random-access module.

One of the first actions of the `parse()` method should be to call `initParse()`. The module's `initParse` method must begin by calling its superclass constructor:

- `protected void initParse ();`

The superclass constructor in `ModuleBase` will initialize checksum calculations and the byte count (`_nbyte`). The module's `initParse` method should initialize all variables that must start from a known state when parsing a document.

Information obtained during a parse is stored in the variable `_info`, which is a `RepInfo` object.

If the module does validation, the `parse()` method must process the document so as to determine if it is well-formed and valid. If the document is both well-formed and valid, it is unnecessary to call `RepInfo`'s setter methods. If it is not well-formed or not valid, the parser must call `_info.setWellFormed(RepInfo.FALSE)` or `_info.setValid (RepInfo.FALSE)`. `RepInfo.setWellFormed(RepInfo.FALSE)` automatically calls `setValid(RepInfo.FALSE)`, so it is unnecessary to declare a module explicitly ill-formed if it is not valid.

If a document is not valid or not well-formed, then one or more error messages should be placed in `_info` explaining the source of the problem, using `RepInfo.setMessage (Message message)`.

Although this is a "set" method, it actually adds the message to the message list. Messages which indicate invalidity or ill-formedness should be of type `ErrorMessage`, which is a subclass of `Message`.

A non-validating module must call `RepInfo.setWellFormed(RepInfo.UNDETERMINED)`. This will automatically call `setValid(RepInfo.UNDETERMINED)` for you.

Other information which may be stored in `_info` is discussed in the `RepInfo` section.

### 3.5 Reading a Stream-based document

When reading a Stream-based document, buffering and tracking the byte count are supported for the module, provided that the data is read properly. It is assumed that the `parse()` function will use the `InputStream` passed to it to set up a `BufferedDataStream` through code such as the following:

- `BufferedDataStream _dstream = getBufferedDataStream (stream, _app != null ?   _app.getBufferSize () : 0);`

A `ChecksumInputStream` is designed to calculate [checksums](#checksum) automatically as the stream is read. The `BufferedDataStream` is used for the reading of data from the document. `getBufferedDataStream` is defined by `ModuleBase`. Only the functions indicated here (defined in `ModuleBase`) should be used to read the `BufferedDataStream`; if this is done, then the value of `_nByte` is kept up to date as the current offset into the file. The `ModuleBase` argument must be the value of the calling module (normally `this`), or null if the function is being called in a context where `_nByte` should not be updated.

- `public static int readUnsignedByte (DataInputStream stream, ModuleBase counted);`
- `public static void readByteBuf (DataInputStream stream,   byte[] buf,   ModuleBase counted);`
- `public static int readUnsignedShort (DataInputStream stream,   boolean bigEndian,   ModuleBase counted);`
- `public static long readUnsignedInt (DataInputStream stream,   boolean bigEndian,   ModuleBase counted);`
- `public static Rational readUnsignedRational (DataInputStream stream,   boolean endian,   ModuleBase counted);`
- `public static int readSignedByte (DataInputStream stream,   ModuleBase counted);`
- `public static int readSignedShort (DataInputStream stream, boolean endian,   ModuleBase counted);`
- `public static int readSignedInt (DataInputStream stream, boolean endian,   ModuleBase counted);`
- `public static float readFloat (DataInputStream stream, boolean endian,   ModuleBase counted);`
- `public static double readDouble (DataInputStream stream, boolean endian,   ModuleBase counted);`
- `public void skipBytes (DataInputStream stream, int bytesToSkip,   ModuleBase counted);`

{: #randomaccess .section-heading}
### 3.6 Reading a random-access document

There is less built-in support for random-access modules than for stream-based modules, since random access is more varied. If you have a choice for a given file format, it is usually simpler to write a stream-based module than a random-access module. However, if a format uses file pointers or offsets, it will probably be necessary to use random access.

If checksum calculation is requested (`_app.getDoChecksum()` returns `true`), and if the checksum has not already been calculated (`_info.getChecksum().size()` is zero), it is necessary to calculate the checksum explicitly. The function `ModuleBase.calcRAChecksum()` is provided for this purpose. The following code in your `parse` function will do the job:

```java
    Checksummer ckSummer = null;
    if (_app != null && _app.getDoChecksum () &&
        info.getChecksum ().size () == 0) {
        ckSummer = new Checksummer ();
        calcRAChecksum (ckSummer, raf);
        setChecksums (ckSummer, info);
    }
```

For your module to be reasonably efficient, it is necessary to read data in the largest chunks that are feasible; doing single-byte reads everywhere and making frequent calls to `RandomAccessFile.seek()` will slow operations down painfully. A useful trick when reading a structure of known size is to read it into a byte array, then create a `ByteArrayInputStream` on it, and a `DataInputStream` on the `ByteArrayInputStream`. You can then use any of the stream-based data reading functions provided by `ModuleBase` (listed above). Be sure to pass `null` where a `ModuleBase` parameter is expected, since updating `_nByte` is meaningless and possibly harmful in this context.

{: #features .section-heading}
### 3.7 Module features

To allow greater flexibility in incorporating third-party modules with different degrees of functionality, JHOVE modules can be queried for their "features." Names for features should follow the same conventions as Java packages. Currently, all HUL modules report the following features:

|     |     |
| --- | --- |
| `edu.harvard.hul.ois.jhove.canCharacterize` | Gives descriptive information |
| `edu.harvard.hul.ois.jhove.canValidate` | Reports document validity |

If a Module supports a different set of features, it must override `ModuleBase.initFeatures`. The features list should never be empty or void.

If a Module's features indicate that it cannot validate, JHOVE will call it only when it is explicitly selected. The reason for this is that such a module may act unpredictably when given a document of the wrong format. However, even modules which do not validate should throw an exception or return gracefully when they encounter a document that they can't deal with.

Features of a Module can be queried with `hasFeature` for a particular feature, or `getFeatures` to retrieve the complete list.

{: #checksum .section-heading}
### 3.8 Checksum calculations

One of the tasks of the module's parse() function is to calculate checksums on the module if requested. The module should call `_app.getDoChecksum()` to determine if it has been requested to calculate checksums. In addition, it should examine the value of `info.getChecksum()`; if it is a non-empty list, then the application has already calculated the checksum and no further action is needed. The classes `Checksummer` and `ChecksumInputStream` aid in doing the calculations.

Calculating the checksums can be a time-consuming operation if the document is large, so the module should perform the calculation only when it is required.

`Checksummer` provides the capability for calculating CRC32, MD5, and SHA1 checksums or message digests. The availability of the MD5 and SHA1 message digests depends on the version of the Java library which is available; in most cases, though, they should be available.

- `public Checksummer ();`

To calculate the checksum, it is simply necessary to call `Checksummer.update()` with each byte of the document in sequence. The caller can then call `getCRC32()`, `getMD5()`, and `getSHA1()` to obtain the calculated values.

`ChecksumInputStream` further automates the generation of these values by incorporating them into the reading of the stream.

- `public ChecksumInputStream(InputStream stream, Checksummer cksummer);`

If the module uses a `ChecksumInputStream` as the argument to `ModuleBase.getBufferedDataStream`, then the checksum calculations will be done in the course of reading the `BufferedDataStream`. This technique cannot be used with random-access modules.

{: #document .section-heading}
### 3.9 The Document object

The `Document` object is used to define sources of documentation for a module. `Document` objects are added to the module's `_specification` list.

- `public Document (String title, DocumentType type);`

The `Document`'s title is any suitable descriptive string; the actual title of the document is recommended. The type must be one of the predefined instances of `DocumentType`:

- `DocumentType.ARTICLE`
- `DocumentType.BOOK`
- `DocumentType.REPORT`
- `DocumentType.RFC`
- `DocumentType.STANDARD`
- `DocumentType.OTHER`

Other information may be added to a `Document` using its setter methods:

- `public void setAuthor (Agent author);`
- `public void setDate (String date);`
- `public void setEdition (String edition);`
- `public void setEnumeration (String enum);`
- `public void setIdentifier (Identifier identifier);`
- `public void setNote (String note);`
- `public void setPages (String pages);`
- `public void setPublisher (Agent publisher);`

The author and publisher of a `Document` are defined using [`Agent`](#agent) objects. The identifier is defined using an [`Identifier`](#identifier) object.

{: #signature .section-heading}
### 3.10 The Signature object

`Signature` objects are used to specify quick checks for whether a document conforms to a format. When checking for signatures, the document is not checked in any details, but only examined for characteristic data, such as a header or filename extension. `Signature` is an abstract class; JHOVE defines subclasses `ExternalSignature` and `InternalSignature`. `InternalSignature` is used for signatures based on the document content; `ExternalSignature` is used for signatures based on the file name, metadata, or other information located other than in the document content.

- `public ExternalSignature (String value, SignatureType type,   SignatureUseType use);`  
    Used when the signature is represented as a character string.
- `public ExternalSignature (int[] value, SignatureType type, SignatureUseType use);`  
    Used when the signature is represented as an array of bytes.
- `public InternalSignature (String value, SignatureType type,   SignatureUseType use);`  
    Used when the signature is represented as a character string and the offset in the file is indeterminate.
- `public InternalSignature (int[] value, SignatureType type,   SignatureUseType use);`  
    Used when the signature is represented as an array of bytes and the offset in the file is indeterminate.
- `public InternalSignature (String value, SignatureType type,   SignatureUseType use, int offset);`  
    Used when the signature is represented as a character string and must occur at a specific offset in the file.
- `public InternalSignature (int[] value, SignatureType type,   SignatureUseType use, int offset);`  
    Used when the signature is represented as an array of bytes and must occur at a specific offset in the file.
- `public InternalSignature (String value, SignatureType type, SignatureUseType use, String note);`  
    Used when the signature is represented as a character string and the offset in the file is indeterminate, and a note is specified.
- `public InternalSignature (int[] value, SignatureType type, SignatureUseType use, String note);   Used when the signature is represented as an array of bytes and the offset in the file is indeterminate, and a note is specified.`
- `public InternalSignature (String value, SignatureType type,   SignatureUseType use, int offset, String note);`       Used when the signature is represented as a character string and must occur at a specific offset in the file, and a note is specified. -   `public InternalSignature (int[] value, SignatureType type,   SignatureUseType use, int offset, String note);`       Used when the signature is represented as an array of bytes and must occur at a specific offset in the file, and a note is specified.

The type parameter must be one of the predefined instances of `SignatureType`. For an `ExternalSignature`, the value may be `EXTENSION` or `FILETYPE`. `EXTENSION` indicates a file extension (more properly, the end of a file name, whether the file system supports extensions or not), such as ".pdf". `FILETYPE` is applicable only to the Macintosh OS, and indicates a four-character file type stored in the file's metadata, such as "TIFF". For an `InternalSignature`, the value must be `MAGIC`, signifying a "magic number" stored in the file.  At this time, the code checks only internal signatures. A document which does not satisfy internal signature specifications is reported as not consistent.

{: #repinfo .section-heading}
### 3.11 The RepInfo object

The module's `parse` method may place information into the variable `_info`, which is a `RepInfo` object. The setting of the `valid` and `wellFormed` fields has already been discussed. In addition, the module may call the following methods to add information to `RepInfo`:

- `public void setFormat (String format);`  
    Sets a `String` identifying the format of the document. This should be an element of the module's `FORMAT` array.
- `public void setMessage (Message message);`  
    Adds a `Message` to the list of informational and error messages. If a document is not valid, there should be at least one `ErrorMessage` explaining the problem.
- `public void setMimeType (String mimeType);`  
    Sets the MIME type which the document satisfies.
- `public void setProfile (String profile);`  
    Sets the name of a profile which the document satisfies. A profile denotes a set of document characteristics which conform to a recognized subclass of the document format, such as TIFF Class P and Class R. More than one profile may be set for a document.
- `public void setProperty (Property property);`  
    Adds a [`Property`](#property) of the document to the list of Properties. Any number of Properties may be set. Each module should have a consistent set of Properties which are reported for documents that are valid under it. A module which does not do characterization (does not have the `edu.harvard.hul.ois.jhove.canCharacterize` feature) should not add any Properties.
    In creating Properties, a module should pay attention to the value of `_verbosity` (inherited from `ModuleBase`). If \_verbosity has a value of `MIMIMUM_VERBOSITY`, the module should omit information which is voluminous and of relatively little use. If `_verbosity` has a value of `MAXIMUM_VERBOSITY`, then the maximum amount of available data should be reported.
- `public void setSize (long size);`  
    Sets the size of the document in bytes.
- `public void setNote (String note);`  
    Sets a note as may be appropriate.
- `public void setSigMatch (String modname);`  
    Adds a String to a list of module names, indicating that the document's signature satisfies the module. By convention, this should be called with `_name` as its argument. If a module recognizes an internal signature or "magic number" as an initial step in identifying the file, it should call `setSigMatch(_name)` as soon as the signature has been verified. `JhoveBase` treats this list specially, so that values set by successive modules for the same document will be accumulated. The notation that the signature was satisfied will be retained even if the module reports the document as not well-formed. Modules which do not check internal signatures should not call this. A file extension or type should **not** be used as a basis for calling this.

{: #app .section-heading}
### 3.12 The App object

There is a single object of type `App`, which holds information describing the application state. `ModuleBase` makes this available as the field `_app`. With the architectural changes in Beta 3, there is little or no need to make use of this object. References previously made to the `App` object should now refer to the `JhoveBase` object.

{: #jhovebase .section-heading}
### 3.13 The JhoveBase object

There may be one or more than one `JhoveBase` objects, depending on the application architecture. It holds information relevant to a particular invocation of JHOVE. `ModuleBase` makes this available as the field `_je` ("JHOVE engine"). The following functions are of interest:

- `public int getBufferSize ();`  
    Returns the user's preferred buffer size, as specified in the command line. This is subject to interpretation, but in general buffers allocated by the application should be that large. If the value returned is negative, the application has not specified a buffer size.
- `public boolean getShowRawFlag ();`  
    Returns the "raw output" flag. If this function returns `true`, then properties with numeric values that have specific interpretations should contain only the numeric values; if it returns `false`, the module may substitute interpretive text strings for the numeric values.
- `public boolean getChecksumFlag ();`  
    Returns `true` if the application has been asked to do checksum calculations.

{: #agent .section-heading}
### 3.14 The Agent object

The `Agent` object defines a party that has a role in the creation, publication, or distribution of a `Document`.

- `public Agent (String name, AgentType type);`

The `Agent`'s name is any suitable descriptive string. The type must be one of the predefined instances of `AgentType`:

- `AgentType.COMMERCIAL`
- `AgentType.GOVERNMENT`
- `AgentType.EDUCATIONAL`
- `AgentType.NONPROFIT`
- `AgentType.STANDARD`
- `AgentType.OTHER`

Other information may be added to an `Agent` using its setter methods:

- `public void setAddress (String address);`
- `public void setEmail (String email);`
- `public void setFax (String fax);`
- `public void setNote (String note);`
- `public void setTelephone (String telephone);`
- `public void setWeb (String web);`

{: #identifier .section-heading}
### 3.15 The Identifier object

The `Identifier` object provides various ways of assigning an identifier to a `Document`.

- `public Identifier (String value, IdentifierType type, String note);`

The `Identifier`'s value should be appropriate to the `IdentifierType`. The type must be one of the predefined values of `IdentifierType`:

- `IdentifierType.ANSI` (American National Standards Institute)
- `IdentifierType.DDC` (Dewey Decimal Classification)
- `IdentifierType.DOI` (Digital Object Identifier)
- `IdentifierType.ECMA`
- `IdentifierType.HANDLE` (CNRI Handle)
- `IdentifierType.ISO` (International Standards Organization)
- `IdentifierType.ISBN` (International Standard Book Number)
- `IdentifierType.LC` (Library of Congress classification)
- `IdentifierType.LCCN` (Library of Congress catalogue number)
- `IdentifierType.NISO` (NISO standard number)
- `IdentifierType.PII` (Publisher Item Identifier)
- `IdentifierType.RFC` (IETF Request for Comment)
- `IdentifierType.SICI` (Serial Item and Contribution Identifier)
- `IdentifierType.URI` (Uniform Resource Identifier)
- `IdentifierType.URL` (Uniform Resource Locator)
- `IdentifierType.URN` (Uniform Resource Name)
- `IdentifierType.CCITT`
- `IdentifierType.ITU` (International Telecommunication Union)
- `IdentifierType.JEITA` (Japan Electronics and Information Technology Industries Association)
- `IdentifierType.OTHER`

The note parameter may be null.

{: #property .section-heading}
### 3.16 The Property object

Properties are used to report information about a document. Output handlers and the viewer application present Properties in an appropriate output format. JHOVE provides a rich set of options for defining properties. Properties can be single objects or ordered or unordered sets. The constituent members of a `Property` can themselves be Properties, allowing a hierarchical structure. All constituent members of a given `Property` must have the same type.

- `public Property (String name, PropertyType type, Object value);`
- `public Property (String name, PropertyType type, PropertyArity arity, Object value);`

The first constructor creates a `Property` with an arity of `PropertyArity.SCALAR`.

The property name should be a valid XML name; in particular, it should not contain white space.

The arity (type of organization) of the property must be one of the predefined instances of `PropertyArity`. The type of value must be in agreement with the value of `arity`, as specified by the following table.

|     |     |
| --- | --- |
| `PropertyArity.ARRAY` | Java array |
| `PropertyArity.LIST` | `java.util.List` |
| `PropertyArity.MAP` | `java.util.Map` |
| `PropertyArity.SCALAR` | Type indicated by `type` |
| `PropertyArity.SET` | `java.util.Set` |

The type must be one of the predefined instances of `PropertyType`. The type of the constituents of value must be in agreement with the value of `type`, as specified by the following table. If the arity is `SCALAR`, the type of value itself must be in agreement with the value of `type`. With arity `ARRAY`, members of the array are primitive Java types rather than Objects where applicable, so the type in the last column must be used. The object type must be used with all other arities.

|     |     |     |
| --- | --- | --- |
| `PropertyType.AESAUDIOMETADATA` | `edu.harvard.hul.ois.jhove.AESAudioMetadata` | |
| `PropertyType.BOOLEAN` | `java.lang.Boolean` | `boolean` |
| `PropertyType.BYTE` | `java.lang.Byte` | `byte` |
| `PropertyType.CHARACTER` | `java.lang.Character` | `char` |
| `PropertyType.DATE` | `java.util.Date` | |
| `PropertyType.DOUBLE` | `java.lang.Double` | `double` |
| `PropertyType.FLOAT` | `java.lang.Float` | `float` |
| `PropertyType.INTEGER` | `java.lang.Integer` | `int` |
| `PropertyType.LONG` | `java.lang.Long` | `long` |
| `PropertyType.NISOIMAGEMETADATA` | `edu.harvard.hul.ois.jhove.NisoImageMetadata` | |
| `PropertyType.OBJECT` | `java.lang.Object` | |
| `PropertyType.PROPERTY` | `edu.harvard.hul.ois.jhove.Property` | |
| `PropertyType.RATIONAL` | `edu.harvard.hul.ois.jhove.Rational` | |
| `PropertyType.SHORT` | `java.lang.Short` | `short` |
| `PropertyType.STRING` | `java.lang.String` | |

{: #message .section-heading}
### 3.17 The Message object

A `Message` object is used to report information about the document. `Message` is an abstract class with two subclasses, `InfoMessage` and `ErrorMessage`. The only difference between the classes is the significance of the message; an `ErrorMessage` should be used for a situation that makes a document invalid or ill-formed, and an `InfoMessage` for other cases.

- `public InfoMessage (String message, long offset);`
- `public InfoMessage (String message);`
- `public ErrorMessage (String message, long offset);`
- `public ErrorMessage (String message);`

If the circumstance which gives rise to the message occurs at a known offset into the document, the constructor with an offset should be used; otherwise the single-argument constructor should be used.

{: #nisoimagemetadata .section-heading}
### 3.18 The NisoImageMetadata object

`NisoImageMetadata` provides a standard way to report many common document properties. The output handlers include dedicated methods for displaying `NisoImageMetadata` properties.

- `public NisoImageMetadata ();`

Setter methods are provided for the properties which `NisoImageMetadata` supports.The source code, the JavaDoc for the class and the NISO documentation should be consulted for detailed information on setter functions and parameter values.

{: #rational .section-heading}
### 3.19 The Rational object

A `Rational` object provides a way to represent the ratio of two integers. A `Rational` is stored as its numerator and denominator values. No protection against zero division is provided by the class.

- `public Rational (long numerator, long denominator);`

See the [JavaDoc](/javadoc/) for further details.
