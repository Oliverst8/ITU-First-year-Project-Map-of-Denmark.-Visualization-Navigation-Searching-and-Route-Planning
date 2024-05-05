package dk.itu.map.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class XMLReaderWrapper {
    private final XMLStreamReader reader;
    private final FileProgress fileProgress;

    /**
     * Create a new XMLReaderWrapper
     * @param inputStream The input stream to be read
     * @param fileProgress The file progress to be updated
     * @throws XMLStreamException If an error occurs while reading the stream
     */
    public XMLReaderWrapper(InputStream inputStream, FileProgress fileProgress) throws XMLStreamException {
        this.reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        this.fileProgress = fileProgress;
    }

    /**
     * Get the next event
     * @return The next event
     * @throws XMLStreamException If an error occurs while reading the stream
     */
    public int next() throws XMLStreamException {
        fileProgress.updateProgress();
        return reader.next();
    }

    /**
     * Get local name
     * @return The local name
     */
    public String getLocalName() {
        return reader.getLocalName();
    }

    /**
     * Get the attribute value
     * @param namespaceURI The namespace URI
     * @param localName The local name
     * @return The attribute value
     */
    public String getAttributeValue(String namespaceURI, String localName) {
        return reader.getAttributeValue(namespaceURI, localName);
    }

    /**
     * Get the attribute value
     * @return The attribute value
     * @throws XMLStreamException If an error occurs while reading the stream
     */
    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }
}