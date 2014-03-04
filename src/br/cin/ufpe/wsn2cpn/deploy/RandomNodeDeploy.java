package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;

/**
 *
 * @author avld
 */
public class RandomNodeDeploy extends NodeDeploy
{

    public RandomNodeDeploy()
    {
        this.name = "Random";
    }
    
    @Override
    public Topology create( Topology top ) throws Exception
    {
        for( int i = 0; i < nodeSize; i++ )
        {
            Node node = getNode( top , i + 1 );
            
            if( node.isFixed() )
            {
                continue ;
            }
            
            int pos_x = (int) ( x + Math.random() * width ); 
            node.getProperties().put( "X" , pos_x + "" );
            
            int pos_y = (int) ( y + Math.random() * heigth ); 
            node.getProperties().put( "Y" , pos_y + "" );
            
            //System.out.println( "  X: " + pos_x + " | Y: " + pos_y );
            
            top.getNodeMap().put( node.getId() , node );
        }
        
        return top;
    }
    
}
