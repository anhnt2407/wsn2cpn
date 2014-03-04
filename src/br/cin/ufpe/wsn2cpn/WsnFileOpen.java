package br.cin.ufpe.wsn2cpn;

import org.w3c.dom.Node;
import java.io.File;
import java.util.Map;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;

/**
 *
 * @author avld
 */
public class WsnFileOpen
{
    private Topology topology;

    public WsnFileOpen()
    {
        topology = new Topology();
    }

    public void open(String filename) throws Exception
    {
        File fXmlFile = new File( filename );
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( fXmlFile );
        doc.getDocumentElement().normalize();

        NodeList rootXml = doc.getDocumentElement().getChildNodes();

        topology = new Topology();
        
        for( int i = 0; i < rootXml.getLength(); i++ )
        {
            Node nodeXml = rootXml.item( i );

            if( "configurations".equalsIgnoreCase( nodeXml.getNodeName() ) )
            {
                processConfigurations( nodeXml );
            }
            else if( "variables".equalsIgnoreCase( nodeXml.getNodeName() ) )
            {
                processVariables( nodeXml );
            }
            else if( "nodes".equalsIgnoreCase(nodeXml.getNodeName() ) )
            {
                processNodes( nodeXml );
            }
        }
        
    }

    private void processConfigurations(Node nodeXml)
    {
        NodeList parameterXml = nodeXml.getChildNodes();
        for( int i = 0; i < parameterXml.getLength(); i++ )
        {
            Node parameterNodeXml = parameterXml.item( i );

            if( "property".equalsIgnoreCase( parameterNodeXml.getNodeName() ) )
            {
                process( topology.getConfigurationMap() , parameterNodeXml );
            }
        }
    }

    private void processVariables(Node nodeXml)
    {
        NodeList parameterXml = nodeXml.getChildNodes();
        for( int i = 0; i < parameterXml.getLength(); i++ )
        {
            Node parameterNodeXml = parameterXml.item( i );

            if( "property".equalsIgnoreCase( parameterNodeXml.getNodeName() ) )
            {
                process( topology.getVariableMap() , parameterNodeXml );
            }
        }
    }

    private void processNodes(Node nodesXml)
    {
        NodeList childrenXml = nodesXml.getChildNodes();
        for( int i = 0; i < childrenXml.getLength(); i++ )
        {
            Node nodeXml = childrenXml.item( i );

            if( "node".equalsIgnoreCase( nodeXml.getNodeName() ) )
            {
                processNode( nodeXml );
            }
        }
    }

    private void processNode(Node nodeXml)
    {
        Node idNode = nodeXml.getAttributes().getNamedItem( "id" );
        String idStr = idNode.getNodeValue();
        int id = Integer.parseInt( idStr );

        br.cin.ufpe.wsn2cpn.Node node = new br.cin.ufpe.wsn2cpn.Node();
        node.setId( id );

        NodeList parameterXml = nodeXml.getChildNodes();
        for( int i = 0; i < parameterXml.getLength(); i++ )
        {
            Node parameterNodeXml = parameterXml.item( i );

            if( "property".equalsIgnoreCase( parameterNodeXml.getNodeName() ) )
            {
                process( node.getProperties() , parameterNodeXml );
            }
        }

        topology.getNodeMap().put( id , node );
    }

    private void process(Map<String,String> propertiesMap, Node parameterXml)
    {
        Node nameXml = parameterXml.getAttributes().getNamedItem( "name" );
        Node valueXml = parameterXml.getAttributes().getNamedItem( "value" );

        propertiesMap.put( nameXml.getNodeValue() , valueXml.getNodeValue() );
    }

    public Topology getTopology()
    {
        return topology;
    }
}
