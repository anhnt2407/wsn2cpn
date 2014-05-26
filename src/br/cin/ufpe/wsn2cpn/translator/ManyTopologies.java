package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class ManyTopologies
{
    private Map<String,String> relactionMap;
    private Topology topology;
    private int topologySize;
    
    public ManyTopologies( Topology top )
    {
        relactionMap = new HashMap<String, String>();
        relactionMap.put( "pos.x" , "X" );
        relactionMap.put( "pos.y" , "Y" );
        
        this.topology = top;
    }
    
    public String getCreateNodes()
    {
        if( topology.getConfigurationMap().containsKey( "size" ) )
        {
            topologySize = Integer.parseInt( topology.getConfigurationMap().get( "size" ) );
            return getCreateNodesWithManyTopologies();
        }
        else
        {
            return getCreateNodesWithOneTopology();
        }
    }
    
    private String getCreateNodesWithOneTopology()
    {   
        StringBuilder builder = new StringBuilder();
        builder.append( "fun createNodes() =\n" );
        builder.append( "(\n" );
        builder.append( "  nodeList    := nil;\n\n" );
        
        for( int i = 0 ; i < topology.getNodeMap().size() ; i++ )
        {
            Node node = topology.getNodeMap().get( i + 1 );
            
            builder.append( "  addNode( " );
            builder.append( getNode( node ) );
            builder.append( ");\n" );
        }
        
        builder.append( "  \n" );
        builder.append( "  !nodeList\n" );
        builder.append( ");" );
        
        return builder.toString();
    }
    
    private String getCreateNodesWithManyTopologies()
    {   
        StringBuilder builder = new StringBuilder();
        builder.append( "fun createNodes() =\n" );
        builder.append( "let\n" );
        builder.append("  val rand  = uniform( 1.0 , " ).append( topologySize ).append( ".0 );\n");
        builder.append("  val value = IntInf.toInt( RealToIntInf 0 rand );\n");
        builder.append( "in\n" );
        
        builder.append( "  if( value = 1 ) then createTopology1()\n" );
        
        for( int i = 2; i <= topologySize ; i++ )
        {
            builder.append ( "  else if ( value = " ).append( i ).append( " ) then" );
            builder.append ( " createTopology").append( i ).append( "()\n");
        }
        
        builder.append( "  else createTopology1()\n" );
        builder.append( "end;" );
        
        return builder.toString();
    }
    
    public List<Ml> getCreateTopologyList() throws CloneNotSupportedException
    {
        List<Ml> mlList = new ArrayList<Ml>();
        List<Topology> topologyList = createTopologyList();
        
        for( int i = 0 ; i < topologyList.size() ; i++ )
        {
            Topology top = topologyList.get( i );
            String layout = getCreateTopology( i + 1 , top );
            
            mlList.add( new Ml( layout ) );
        }
        
        return mlList;
    }
    
    private String getCreateTopology( int n , Topology top )
    {   
        StringBuilder builder = new StringBuilder();
        builder.append( "fun createTopology" ).append( n ).append( "() =\n" );
        builder.append( "(\n" );
        builder.append( "  nodeList    := nil;\n\n" );
        
        for( int i = 0 ; i < top.getNodeMap().size() ; i++ )
        {
            Node node = top.getNodeMap().get( i + 1 );
            
            builder.append( "  addNode( " );
            builder.append( getNode( node ) );
            builder.append( ");\n" );
        }
        
        builder.append( "  \n" );
        builder.append( "  !nodeList\n" );
        builder.append( ");" );
        
        return builder.toString();
    }
    
    private String getNode(Node node)
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "{" );

        builder.append( "id = " );
        builder.append( node.getId() );
        builder.append( ", " );

        for( Map.Entry<String,String> entry : node.getProperties().entrySet() )
        {
            String key = entry.getKey();

            if( relactionMap.containsKey( key ) )
            {
                key = relactionMap.get( key );
            }

            if( "application".equals( key )
                    || "type".equals( key )
                    || "application_layer".equals( key )
                    || "network_layer".equals( key )
                    || "max_layer".equals( key )
                    || "battery".equals( key )
                    || "energy".equals( key )
                    || "X_list".equals( key )
                    || "Y_list".equals( key )
                    || "energy_list".equals( key )
                    || "range_list".equals( key )
                    || "battery_level".equals( key )
                    )
            {
                continue ;
            }

            builder.append( key );
            builder.append( " = " );
            builder.append( entry.getValue() );
            builder.append( ", " );
        }

//        builder.append( "radio_status = true, " );
//        builder.append( "sent = 0, " );
//        builder.append( "received = 0, " );
//        builder.append( "routed = 0, " );
//        builder.append( "ignored = 0, " );
//        builder.append( "packet_id = 0, " );
//        builder.append( "retransmitted = 0, " );
        builder.append( "isDead = false " );
        
        builder.append( "}" );
        
        return builder.toString();
    }
    
    private List<Topology> createTopologyList() throws CloneNotSupportedException
    {
        List<Topology> topologyList = new ArrayList<>();
        List<List<Integer>> xList = new LinkedList<>();
        List<List<Integer>> yList = new LinkedList<>();
        List<List<Integer>> rList = new LinkedList<>();
        
        for( int id = 1 ; id <= topology.getNodeMap().size() ; id++ )
        {
            Node n = topology.getNodeMap().get( id );
            
            if( !n.getProperties().containsKey( "X_list" ) )
            {
                String X = n.getProperties().get( "X" );
                n.getProperties().put( "X_list" , "[" + X + "]" );
            }
            
            if( !n.getProperties().containsKey( "Y_list" ) )
            {
                String Y = n.getProperties().get( "Y" );
                n.getProperties().put( "Y_list" , "[" + Y + "]" );
            }
            
            if( !n.getProperties().containsKey( "range_list" ) )
            {
                String range = n.getProperties().get( "range" );
                n.getProperties().put( "range_list" , "[" + range + "]" );
            }
            
            xList.add( convertToList( n.getProperties().get( "X_list" ) ) );
            yList.add( convertToList( n.getProperties().get( "Y_list" ) ) );
            rList.add( convertToList( n.getProperties().get( "range_list" ) ) );
        }
        
        if( !topology.getConfigurationMap().containsKey( "size" ) )
        {
            topology.getConfigurationMap().put( "size" , "1" );
        }
        
        topologySize = Integer.parseInt( topology.getConfigurationMap().get( "size" ) );
        
        for( int topologyId = 0 ; topologyId < topologySize ; topologyId++ )
        {
            Topology top = new Topology();
            
            for( int nodeId = 1 ; nodeId <= topology.getNodeMap().size() ; nodeId++ )
            {
                Node n = topology.getNodeMap().get( nodeId ).clone();
                
                n.getProperties().put( "X" , xList.get( nodeId - 1 ).get( topologyId ) + "" );
                n.getProperties().put( "Y" , yList.get( nodeId - 1 ).get( topologyId ) + "" );
                n.getProperties().put( "range" , rList.get( nodeId - 1 ).get( topologyId ) + "" );
                
                top.getNodeMap().put( nodeId , n );
            }
            
            topologyList.add( top );
        }
        
        return topologyList;
    }
    
    private List<Integer> convertToList( String values )
    {
        String arr = values.substring( 1 , values.length() - 1 );
        String[] parts = arr.split( "," );
        
        List<Integer> l = new ArrayList<Integer>();
        for( String p : parts )
        {
            l.add( Integer.parseInt(p.trim() ) );
        }
        
        return l;
    }
    
}
