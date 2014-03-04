package br.cin.ufpe.wsn2cpn.layer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author avld
 */
public class LayerConfigurationOpen
{
    private LayerConfiguration layer;
    private String filename;

    public LayerConfigurationOpen(String filename)
    {
        this.filename = filename;
    }

    public LayerConfiguration open() throws Exception
    {
        layer = new LayerConfiguration();

        Node root = getNodeRoot();
        threat( root );
        
        return layer;
    }

    private Node getNodeRoot() throws Exception
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse( filename );
        doc.getDocumentElement().normalize();

        return doc.getDocumentElement();
    }

    private void threat( Node node )
    {
        NodeList list = node.getChildNodes();
        
        for( int i = 0 ; i < list.getLength() ; i++ )
        {
            Node children = list.item( i );

            if( "name".equalsIgnoreCase( children.getNodeName() ) )
            {
                layer.setName( getText( children ) );
            }
            else if( "layer".equalsIgnoreCase( children.getNodeName() ) )
            {
                layer.setLayer(getText( children ) );
            }
            else if( "developer".equalsIgnoreCase( children.getNodeName() ) )
            {
                layer.setDeveloper(getText( children ) );
            }
            else if( "description".equalsIgnoreCase( children.getNodeName() ) )
            {
                layer.setDescription(getText( children ) );
            }
            else if( "variable_list".equalsIgnoreCase( children.getNodeName() ) )
            {
                threatVariables( children );
            }
            else if( "node_propreties".equalsIgnoreCase( children.getNodeName() ) )
            {
                threatPropreties( children );
            }
        }
    }

    private void threatVariables( Node node )
    {
        NodeList list = node.getChildNodes();
        for( int i = 0 ; i < list.getLength() ; i++ )
        {
            Node children = list.item( i );

            if( "property".equalsIgnoreCase( children.getNodeName() ) )
            {
                LayerProperty prop = threatProperty( children );
                layer.getVariableList().add( prop );
            }
        }
    }
    
    private void threatPropreties( Node node )
    {
        NodeList list = node.getChildNodes();
        for( int i = 0 ; i < list.getLength() ; i++ )
        {
            Node children = list.item( i );

            if( "property".equalsIgnoreCase( children.getNodeName() ) )
            {
                LayerProperty prop = threatProperty( children );
                layer.getNodeProperties().add( prop );
            }
        }
    }

    private LayerProperty threatProperty( Node node )
    {
        LayerProperty prop = new LayerProperty();
        prop.setHidden(false );

        NodeList list = node.getChildNodes();
        for( int i = 0 ; i < list.getLength() ; i++ )
        {
            Node children = list.item( i );

            if( "name".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setName( getText( children ) );
            }
            else if( "type".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setType( getText( children ) );
            }
            else if( "nick".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setNick( getText( children ) );
            }
            else if( "default".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setDefaultValue( getText( children ) );
            }
            else if( "hidden".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setHidden(true );
            }
            else if( "description".equalsIgnoreCase( children.getNodeName() ) )
            {
                prop.setDescription( getText( children ) );
            }
        }
        
        return prop;
    }

    public String getText(Node node)
    {
        //"<text> ... </text>
        if( node.getChildNodes().getLength() == 0 )
        {
            return "";
        }
        else
        {
            Node n = node.getChildNodes().item( 0 );
            String value = n.getNodeValue();

            value = value.replace( "&lt;" , "<" );

            return value;
        }
    }
    
}
