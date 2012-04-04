package com.soletta.seek.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Provides detection of OOXML files, and very fast text extraction from them.
 * 
 * @author rjudson
 *
 */
public class OOXMLParser {
    private final SAXParserFactory spf;
    
    public OOXMLParser() {
        spf = SAXParserFactory.newInstance();
        try {
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
        spf.setNamespaceAware(true);
    }

    /** Check the given buffer to see if it holds an OOXML document.
     * 
     * @param buffer
     * @return
     * @throws IOException
     */
    public boolean isOOXML(byte [] buffer) throws IOException {
        try {
            ZipInputStream zi = new ZipInputStream(new ByteArrayInputStream(buffer));
            try {
                ZipEntry entry = zi.getNextEntry();
                while (entry != null) {
                    if (entry.getName().equals("[Content_Types].xml")) 
                        return true;
                    entry = zi.getNextEntry();
                }
                return false;
            } finally {
                zi.close();
            }
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean isOOXML(File file) throws ZipException, IOException {
        try {
            ZipFile zf = new ZipFile(file);
    
            try {
                ZipEntry content = zf.getEntry("[Content_Types].xml");
                return content != null;
            } finally {
                zf.close();
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /** Break out the information in the given OOXML file, writing the text content to the provided Writer, 
     * and the properties into the given map. If out is null, the text won't be extracted.
     * @param file
     * @param out A writer that will get the text (null is no extraction of text).
     * @param properties
     * @throws ZipException
     * @throws IOException
     */
    public void extract(File file, Writer out, Map<String, String> properties) throws ZipException, IOException {
        List<String> textEntries = new ArrayList<String>();
        List<String> propertyEntries = new ArrayList<String>();
        
        ZipFile zf = new ZipFile(file);

        try {
            ZipEntry content = zf.getEntry("[Content_Types].xml");
            if (content != null) {
                InputStream contentStream = zf.getInputStream(content);
                try {
                    SAXParser parser = spf.newSAXParser();
                    parser.parse(contentStream, new ContentTypesHandler(textEntries, propertyEntries), "OO");
                    if (out != null)
                        parseEntries(zf, null, parser, new TextHandler(out), textEntries);
                    parseEntries(zf, null, parser, new DocPropsHandler(properties), propertyEntries);
                } catch (SAXException se) {
                    if (se.getCause() != null && se.getCause() instanceof TextLimitReached) {
                        // ignore...we have extracted to our text limit.
                    } else {
                        se.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    contentStream.close();
                }
            }
        } finally {
            zf.close();
        }
        // System.out.format("%,d fragments\n", fragments);
    }

    protected void parseEntries(ZipFile zf, InputStream entryStream, SAXParser parser, DefaultHandler th, List<String> list) throws IOException,
            SAXException {
        for (String te : list) {
            ZipEntry entry = zf.getEntry(te);
            if (zf != null)
                entryStream = zf.getInputStream(entry);
            try {
                parser.parse(entryStream, th, "OO");
            } finally {
                if (zf != null)
                    entryStream.close();
            }
        }
    }

    static class DocPropsHandler extends DefaultHandler {

        private static final String COREPROPS_NS = "http://schemas.openxmlformats.org/package/2006/metadata/core-properties";

        private static final String EXTENDED_NS = "http://schemas.openxmlformats.org/officeDocument/2006/extended-properties";

        private final Map<String, String> properties;

        private StringBuilder builder;
        private String propertyName;
        private boolean inProperties, inCoreProperties;

        private HashMap<String, String> propsAttrMap;
        private HashMap<String, String> coreAttrMap;
        
        DocPropsHandler(Map<String, String> properties) {
            this.properties = properties;
            
            // insert a dash before each upper case character but the first.
            propsAttrMap = new HashMap<String, String>();
            propsAttrMap.put("Characters", "Character Count");
            propsAttrMap.put("CharactersWithSpaces", "Character-Count-With-Spaces");
            propsAttrMap.put("Application", "Application-Name");
            propsAttrMap.put("AppVersion", "Application-Version");
            propsAttrMap.put("Lines", "Line-Count");
            propsAttrMap.put("Paragraphs", "Paragraph-Count");
            propsAttrMap.put("Pages", "Page-Count");
            propsAttrMap.put("Slides", "Slide-Count");
            
            coreAttrMap = new HashMap<String, String>();
            coreAttrMap.put("title", "title");
            coreAttrMap.put("subject", "subject");
            coreAttrMap.put("creator", "creator");
            coreAttrMap.put("keywords", "Keywords");
            coreAttrMap.put("description", "description");
            coreAttrMap.put("lastModifiedBy", "Last-Author");
            coreAttrMap.put("revision", "Revision-Number");
            coreAttrMap.put("created", "Creation-Date");
            coreAttrMap.put("modified", "Last-Modified");
            coreAttrMap.put("version", "Version");
            coreAttrMap.put("publisher", "publisher");
            
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if (inProperties) {
                propertyName = propsAttrMap.get(localName);
                if (propertyName == null) {
                    propertyName = tidyLocalName(localName);
                }
                builder = new StringBuilder();
            } else if (inCoreProperties) {
                
                propertyName = coreAttrMap.get(localName);
                if (propertyName == null) {
                    propertyName = tidyLocalName(localName);
                }
                builder = new StringBuilder();
            } else {
                if (uri.equals(EXTENDED_NS) && localName.equals("Properties")) {
                    inProperties = true;
                } else if (uri.equals(COREPROPS_NS) && localName.equals("coreProperties")) {
                    inCoreProperties = true;
                }
            }
//                propertyName = attrMap.get(localName);
//                if (propertyName == null)
//                    propertyName = localName;
//                builder = new StringBuilder();
//            } else  if (qName.startsWith("dc:") || qName.startsWith("dcterms:") || qName.startsWith("cp:")) {
//                
//                propertyName = localName;
//                builder = new StringBuilder();
//                
//            }
            
            /*
             
        addProperty(metadata, Metadata.CATEGORY, propsHolder.getCategoryProperty());
        addProperty(metadata, Metadata.CONTENT_STATUS, propsHolder.getContentStatusProperty());
        addProperty(metadata, Metadata.DATE, propsHolder.getCreatedProperty());
        addProperty(metadata, Metadata.CREATION_DATE, propsHolder.getCreatedProperty());
        addProperty(metadata, Metadata.CREATOR, propsHolder.getCreatorProperty());
        addProperty(metadata, Metadata.AUTHOR, propsHolder.getCreatorProperty());
        addProperty(metadata, Metadata.DESCRIPTION, propsHolder.getDescriptionProperty());
        addProperty(metadata, Metadata.IDENTIFIER, propsHolder.getIdentifierProperty());
        addProperty(metadata, Metadata.KEYWORDS, propsHolder.getKeywordsProperty());
        addProperty(metadata, Metadata.LANGUAGE, propsHolder.getLanguageProperty());
        addProperty(metadata, Metadata.LAST_AUTHOR, propsHolder.getLastModifiedByProperty());
        addProperty(metadata, Metadata.LAST_PRINTED, propsHolder.getLastPrintedPropertyString());
        addProperty(metadata, Metadata.LAST_MODIFIED, propsHolder.getModifiedProperty());
        addProperty(metadata, Metadata.REVISION_NUMBER, propsHolder.getRevisionProperty());
        addProperty(metadata, Metadata.SUBJECT, propsHolder.getSubjectProperty());
        addProperty(metadata, Metadata.TITLE, propsHolder.getTitleProperty());
        addProperty(metadata, Metadata.VERSION, propsHolder.getVersionProperty());
        
        
        CTProperties propsHolder = properties.getUnderlyingProperties();

        addProperty(metadata, Metadata.APPLICATION_NAME, propsHolder
                .getApplication());
        addProperty(metadata, Metadata.APPLICATION_VERSION, propsHolder
                .getAppVersion());
        addProperty(metadata, Metadata.CHARACTER_COUNT, propsHolder
                .getCharacters());
        addProperty(metadata, Metadata.CHARACTER_COUNT_WITH_SPACES, propsHolder
                .getCharactersWithSpaces());
        addProperty(metadata, Metadata.PUBLISHER, propsHolder.getCompany());
        addProperty(metadata, Metadata.LINE_COUNT, propsHolder.getLines());
        addProperty(metadata, Metadata.MANAGER, propsHolder.getManager());
        addProperty(metadata, Metadata.NOTES, propsHolder.getNotes());
        addProperty(metadata, Metadata.PAGE_COUNT, propsHolder.getPages());
        if (propsHolder.getPages() > 0) {
            metadata.set(PagedText.N_PAGES, propsHolder.getPages());
        } else if (propsHolder.getSlides() > 0) {
            metadata.set(PagedText.N_PAGES, propsHolder.getSlides());
        }
        addProperty(metadata, Metadata.PARAGRAPH_COUNT, propsHolder.getParagraphs());
        addProperty(metadata, Metadata.PRESENTATION_FORMAT, propsHolder
                .getPresentationFormat());
        addProperty(metadata, Metadata.SLIDE_COUNT, propsHolder.getSlides());
        addProperty(metadata, Metadata.TEMPLATE, propsHolder.getTemplate());
        addProperty(metadata, Metadata.TOTAL_TIME, propsHolder.getTotalTime());
        addProperty(metadata, Metadata.WORD_COUNT, propsHolder.getWords());
        
             */
        }

        private String tidyLocalName(String localName) {
            if (localName.indexOf('-') < 0) {
                localName = localName.replaceAll("([A-Z])", "-$1");
                if (localName.startsWith("-"))
                    localName = localName.substring(1);
            }
            if (Character.isLowerCase(localName.charAt(0))) {
                localName = Character.toUpperCase(localName.charAt(0)) + localName.substring(1);
            }
            return localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (builder != null) {
                builder.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (builder != null) {
                String v = builder.toString();
                if (propertyName.length() > 0 && v.length() > 0) {
                    properties.put(propertyName, builder.toString());
//                    System.out.println(propertyName + ": " + v);
                }                
                builder = null;
            }
            if (uri.equals(EXTENDED_NS) && localName.equals("Properties")) {
                inProperties = false;
            } else if (uri.equals(COREPROPS_NS) && localName.equals("coreProperties")) {
                inCoreProperties = false;
            }
        }

    }

    static class TextHandler extends DefaultHandler {

        final Writer out;
        int indent;

        TextHandler(Writer out) {
            this.out = out;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            // try {
            // out.append(Strings.repeat("  ", indent));
            // out.append(qName);
            // out.append('\n');
            // out.flush();
            // } catch (IOException e) {
            // throw new SAXException(e);
            // }
            indent++;
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            try {
                boolean lf;
                if (uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main") && localName.equals("t")) {
                    lf = true;
                } else if (uri.equals("http://schemas.openxmlformats.org/wordprocessingml/2006/main") && (localName.equals("p") || localName.equals("br") || localName.equals("tab"))) {
                    lf = true;
                } else if (uri.equals("http://schemas.openxmlformats.org/drawingml/2006/main") && (localName.equals("p") || localName.equals("br") || localName.equals("tab"))) {
                    lf = true;
                } else {
                    lf = false;
                }
                
                if (lf) {
                    out.append('\n');
//                    out.flush();
                }
            } catch (IOException e) {
                throw new SAXException(e);
            }
            
            indent--;
        }

        @Override
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            try {
                out.write(ch, start, length);
            } catch (IOException e) {
                throw new SAXException(e);
            }
        }

    }

    static class ContentTypesHandler extends DefaultHandler {

        private List<String> textEntries;
        private List<String> propertyEntries;

        ContentTypesHandler(List<String> textEntries, List<String> propertyEntries) {
            this.textEntries = textEntries;
            this.propertyEntries = propertyEntries;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("Override")) {
                String ctValue = attributes.getValue("ContentType");
                String partName = attributes.getValue("PartName").substring(1);
                
                if (endsWith(ctValue, ".slide+xml", "document.main+xml", ".endnotes+xml", ".footnotes+xml", ".sharedStrings+xml" )) {

                    textEntries.add(partName);
                    
//                if (endsWith(ctValue, ".customXmlProperties+xml", ".numbering+xml", ".styles+xml",
//                        ".stylesWithEffects.xml", ".settings+xml", ".webSettings+xml", ".fontTable+xml", ".theme+xml",
//                        ".sheet.main+xml", ".worksheet+xml", ".calcChain+xml", ".presProps+xml",
//                        ".")) {
//                    // ignore it.
                } else if (endsWith(ctValue, ".core-properties+xml", ".extended-properties+xml",
                        ".custom-properties+xml")) {
                    propertyEntries.add(partName);
                }
            }
        }
        
        boolean endsWith(String s, String... endings) {
            for (String e : endings)
                if (s.endsWith(e))
                    return true;
            return false;
        }


    }

    @SuppressWarnings("serial")
    public static class TextLimitReached extends IOException {
        
    }
    
}
