package dk.itu.map.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class XMLReaderWrapper {

    private final XMLStreamReader reader;

    public XMLReaderWrapper(InputStream inputStream) throws XMLStreamException {
        this.reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
    }

    public int next() throws XMLStreamException {
        return reader.next();
    }

    public String getLocalName() {
        return reader.getLocalName();
    }

    public String getAttributeValue(String namespaceURI, String localName) {
        return reader.getAttributeValue(namespaceURI, localName);
    }

    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }
}