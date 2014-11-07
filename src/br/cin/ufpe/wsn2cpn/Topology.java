package br.cin.ufpe.wsn2cpn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class Topology implements Serializable
{
    private Map<String,String>  configurationMap;
    private Map<String,String>  variableMap;
    private Map<Integer,Region> regionMap;
    private Map<Integer,Node>   nodeMap;
    
    public Topology()
    {
        configurationMap = new HashMap<>();
        configurationMap.put( "reliability_link"        , "1.0" );
        configurationMap.put( "reliability_application" , "1.0" );
        configurationMap.put( "reliability_hardware"    , "1.0" );
        configurationMap.put( "reliability_tinyos"      , "1.0" );
        configurationMap.put( "reliability_batteryMax"  , "100" );
        configurationMap.put( "reliability_batteryMin"  , "0.0" );
        
        variableMap = new HashMap<>();
        regionMap   = new HashMap<>();
        nodeMap     = new HashMap<>();
    }

    public Map<String, String> getConfigurationMap()
    {
        return configurationMap;
    }

    public void setConfigurationMap( Map<String, String> configurationMap )
    {
        this.configurationMap = configurationMap;
    }

    public Map<Integer, Node> getNodeMap()
    {
        return nodeMap;
    }

    public void setNodeMap( Map<Integer, Node> nodeMap )
    {
        this.nodeMap = nodeMap;
    }

    public Map<String,String> getVariableMap()
    {
        return variableMap;
    }

    public void setVariableMap( Map<String,String> map )
    {
        this.variableMap = map;
    }

    public Map<Integer, Region> getRegionMap()
    {
        return regionMap;
    }

    public void setRegionMap( Map<Integer, Region> regionMap )
    {
        this.regionMap = regionMap;
    }

//    IT WILL BE USED WHEN CLASS REGION NO ALLOWING SET NODES    //
//    
//    public List<Integer> getRegionNodeList( int regionId )
//    {
//        Region region = regionMap.get( regionId );
//        
//        List<Integer> list = new ArrayList<>();
//        for( Node node : getNodeMap().values() )
//        {
//            if( isInside( region , node ) )
//            {
//                list.add( node.getId() );
//            }
//        }
//        
//        return list;
//    }
//    
//    private boolean isInside( Region region , Node node )
//    {
//        int X = Integer.parseInt( node.getProperties().get( "X" ) );
//        int Y = Integer.parseInt( node.getProperties().get( "Y" ) );
//        
//        boolean insideX = region.getX() >= X 
//                          && X < ( region.getX() + region.getWidth()  );
//        
//        boolean insideY = region.getY() >= Y 
//                          && Y < ( region.getY() + region.getHeight() );
//        
//        return insideX && insideY;
//    }
    
    @Override
    public String toString()
    {
        String txt = "topology\n";

        txt += " configurations:\n";
        for( Entry<String,String> entry : getConfigurationMap().entrySet() )
        {
            txt += "  " + entry.getKey() + ": " + entry.getValue() + "\n";
        }

        txt += " Variable:\n";
        for( Entry<String,String> entry : getVariableMap().entrySet() )
        {
            txt += "  " + entry.getKey() + ": " + entry.getValue() + "\n";
        }

        txt += " Nodes:\n";
        for( Node node : getNodeMap().values() )
        {
            String sendTo = node.getProperties().get( "app_sendTo" );
            String x = node.getProperties().get( "X" );
            String y = node.getProperties().get( "Y" );
            
            txt += "  Node " + node.getId();
            txt += "( "+x+" , "+y+" )\n";
        }

        return txt;
    }
    
}
