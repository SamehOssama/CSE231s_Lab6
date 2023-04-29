import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ArxmlParser {
    public static void main(String[] args) {
        try {
            // Check if the file is of the correct format and not empty
            String fileName = args[0];
            final String extension = ".arxml";
            if(!fileName.endsWith(extension)){
                throw new NotVaildAutosarFileException();
            }
            File file = new File(fileName);
            if (file.length() == 0) {
                throw new EmptyAutosarFileException();
            }

            // Create a document object form the file.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document arxml = builder.parse(file);
            arxml.getDocumentElement().normalize();

            // Remove whitespace only nodes.
            XPathExpression xpath = XPathFactory.newInstance().newXPath().compile("//text()[normalize-space(.) = '']");
            NodeList blankTextNodes = (NodeList) xpath.evaluate(arxml, XPathConstants.NODESET);

            for (int i = 0; i < blankTextNodes.getLength(); i++) {
                blankTextNodes.item(i).getParentNode().removeChild(blankTextNodes.item(i));
            }

            // Get the container nodes from the document object.
            NodeList containers = arxml.getElementsByTagName("CONTAINER");

            // Get the Container elements in an array to be able to sort them.
            Element[] containerArr = new Element[containers.getLength()];
            for (int i = 0; i < containers.getLength(); i++) {
                if (containers.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    containerArr[i] = (Element) containers.item(i);
                }
            }

            // Sort the container elements by "SHORT-NAME" attribute.
            Arrays.sort(containerArr, (o1, o2) -> {
                String myShortName = o1.getChildNodes().item(0).getTextContent();
                String otherShortName = o2.getChildNodes().item(0).getTextContent();
                return myShortName.compareTo(otherShortName);
            });

            // Append the elements in their correct order to the original document.
            Element root = arxml.getDocumentElement();
            for (Element container : containerArr) {
                root.appendChild(container);
            }

            // Output the rearranged document object to the modified file.
            int idx = fileName.lastIndexOf('.');
            String outputFile = fileName.substring(0, idx) + "_mod" + extension;
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource("xsl.xsl"));
            arxml.setXmlStandalone(true);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(arxml);
            StreamResult streamResult = new StreamResult(new File(outputFile));
            transformer.transform(domSource, streamResult);
            System.out.println("Done creating ARXML the File");
        } catch (ParserConfigurationException | SAXException | IOException | NotVaildAutosarFileException | XPathExpressionException | TransformerException e) {
            e.printStackTrace();
        }
    }
}