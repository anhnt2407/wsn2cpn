package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.util.Map;

/**
 *
 * @author avld
 */
public abstract class NodeDeploy implements Cloneable
{
    protected int x;
    protected int y;
    
    protected int width;
    protected int heigth;
    
    protected int nodeSize;
    
    protected String name;
    protected String description;
    
    private Node nodeDefault;
    
    public NodeDeploy()
    {
        nodeDefault = new Node();
    }
    
    public void setLocation( int x , int y )
    {
        this.x = x;
        this.y = y;
    }
    
    public void setSize( int width , int heigth )
    {
        this.width = width;
        this.heigth = heigth;
    }
    
    public void setNodeSize( int nodeSize )
    {
        this.nodeSize = nodeSize;
    }

    public int getNodeSize()
    {
        return nodeSize;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getDescription()
    {
        return this.description;
    }

    public Node getNodeDefault() throws CloneNotSupportedException
    {
        return nodeDefault.clone();
    }

    public void setNodeDefault( Node nodeDefault )
    {
        this.nodeDefault = nodeDefault;
    }
    
    public Map<String,String> getParameterMap()
    {
        return null;
    }
    
    public abstract Topology create( Topology topology ) throws Exception;
    
    public Topology create( Topology topology , Map<String,String> param ) throws Exception
    {
        return create( topology );
    }
    
    public Node getNode( Topology topology , int nodeId ) throws CloneNotSupportedException
    {
        if( !topology.getNodeMap().containsKey( nodeId ) )
        {
            Node node = getNodeDefault();
            node.setId( nodeId );
            
            return node;
        }
        else
        {
            return topology.getNodeMap().get( nodeId );
        }
    }
    
    @Override
    public NodeDeploy clone() throws CloneNotSupportedException
    {
        return (NodeDeploy) super.clone();
    }
}
