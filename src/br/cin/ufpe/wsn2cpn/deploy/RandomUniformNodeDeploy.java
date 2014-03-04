package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;

/**
 *
 * @author avld
 */
public class RandomUniformNodeDeploy extends NodeDeploy
{
    private int colSize = 3;
    private int rowSize = 3;
    
    public RandomUniformNodeDeploy()
    {
        this.name = "Random Uniform";
    }
    
    @Override
    public Topology create( Topology top ) throws Exception
    {
        int nodeId = 1;
        
        int cellWidth = width / colSize;
        int cellHeigth = heigth / rowSize;
        int cellNodeSize = nodeSize / ( colSize * rowSize );
        
//        System.out.println( "width: " + cellWidth + " | heigth: " + cellHeigth );
//        System.out.println( "node size: " + cellNodeSize );
        
        for( int row = 0; row < rowSize; row++ )
        {
            int cellY = y + ( cellHeigth * row );
            
            for( int col = 0; col < colSize; col++ )
            {
                int cellX = x + ( cellWidth * col );
                
                //System.out.println( "x: " + cellX + " | y: " + cellY );
                
                NodeDeploy random = new RandomNodeDeploy();
                random.setLocation( cellX , cellY );
                random.setSize    ( cellWidth , cellHeigth );
                random.setNodeSize( cellNodeSize );
                random.setNodeDefault( getNodeDefault() );
                
                top = random.create( top );
                
                random = null;
            }
        }
        
        if( nodeId < nodeSize )
        {
            return complete( nodeId , top );
        }
        else
        {
            return top;
        }
    }
    
    private Topology complete( int nodeId , Topology top ) throws Exception
    {
        //System.out.println( "Complentando..." );
        
        NodeDeploy random = new RandomNodeDeploy();
        random.setLocation( x , y );
        random.setSize    ( width , heigth );
        random.setNodeSize( nodeSize - nodeId + 1 );
        random.setNodeDefault( getNodeDefault() );

        return random.create( top );
//        for( Node node : random.create( top ).getNodeMap().values() )
//        {
//            node.setId( nodeId++ );
//            top.getNodeMap().put( node.getId() , node );
//        }
    }
    
}
