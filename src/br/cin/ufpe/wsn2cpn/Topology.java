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
    private Map<String,String> configurationMap;
    private Map<String,String> variableMap;
    private Map<Integer,Node> nodeMap;
    
    public Topology()
    {
        configurationMap = new HashMap<String, String>();
        configurationMap.put( "dependability_link"        , "1.0" );
        configurationMap.put( "dependability_application" , "1.0" );
        configurationMap.put( "dependability_hardware"    , "1.0" );
        configurationMap.put( "dependability_tinyos"      , "1.0" );
        configurationMap.put( "dependability_batteryMax"  , "100" );
        configurationMap.put( "dependability_batteryMin"  , "0.0" );
        
        variableMap = new HashMap<String, String>();
        
        nodeMap = new HashMap<Integer, Node>();
    }

    public Map<String, String> getConfigurationMap()
    {
        return configurationMap;
    }

    public void setConfigurationMap(Map<String, String> configurationMap)
    {
        this.configurationMap = configurationMap;
    }

    public Map<Integer, Node> getNodeMap()
    {
        return nodeMap;
    }

    public void setNodeMap(Map<Integer, Node> nodeMap)
    {
        this.nodeMap = nodeMap;
    }

    public Map<String,String> getVariableMap()
    {
        return variableMap;
    }

    public void setVariableMap(Map<String,String> map)
    {
        this.variableMap = map;
    }

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
