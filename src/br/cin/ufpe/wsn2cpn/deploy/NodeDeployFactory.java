package br.cin.ufpe.wsn2cpn.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author avld
 */
public class NodeDeployFactory
{
    private static NodeDeployFactory instance;
    private Map<String,NodeDeploy> deployMap;
    
    public NodeDeployFactory()
    {
        init();
    }
    
    private void init()
    {
        deployMap = new HashMap<String, NodeDeploy>();
        addNodeDeploy( new RandomNodeDeploy() );
        addNodeDeploy( new RandomUniformNodeDeploy() );
        addNodeDeploy( new RandomBsDistanceNodeDeploy() );
    }
    
    private void addNodeDeploy( NodeDeploy deploy )
    {
        deployMap.put( deploy.getName().toUpperCase() , deploy );
    }
    
    public static NodeDeploy getNodeDeploy( String name ) throws Exception
    {
        if( instance == null )
        {
            instance = new NodeDeployFactory();
        }
        
        if( !instance.deployMap.containsKey( name.toUpperCase() ) )
        {
            throw new Exception( "This deployment does not exist." );
        }
        
        return instance.deployMap.get( name.toUpperCase() ).clone();
    }
    
    public static Set<String> getDeployAvaliable()
    {
        if( instance == null )
        {
            instance = new NodeDeployFactory();
        }
        
        return instance.deployMap.keySet();
    }
}
