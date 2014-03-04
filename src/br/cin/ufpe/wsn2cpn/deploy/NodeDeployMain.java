package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.WsnFile;

/**
 *
 * @author avld
 */
public class NodeDeployMain
{
    
    public static void main(String args[]) throws Exception
    {
        NodeDeploy deploy = NodeDeployFactory.getNodeDeploy( "Random Uniform" );
        
        deploy.setLocation( 0 , 0 );
        deploy.setSize( 100 , 100 );
        deploy.setNodeSize( 5 );
        
        Topology top = getTopology();
        top = deploy.create( top );
        
        //WsnFile.save( "deploy_automatic.wsn" , top );
        System.out.println( top.toString() );
    }
    
    private static Topology getTopology()
    {
        Node node = new Node( 1 );
        node.setFixed( true );
        node.getProperties().put( "X" , "10" );
        node.getProperties().put( "Y" , "10" );
        
        Topology top = new Topology();
        top.getNodeMap().put( 1 , node );
        
        return top;
    }
    
}