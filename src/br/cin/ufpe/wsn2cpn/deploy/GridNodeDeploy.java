package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author avld
 */
public class GridNodeDeploy extends NodeDeploy
{
    private int    nodePerRow;
    
    public GridNodeDeploy()
    {
        this.name = "Grid";
    }
    
    @Override
    public Map<String,String> getParameterMap()
    {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put( "nodePerLine" , "10" );
        
        return paramMap;
    }
    
    @Override
    public Topology create( Topology topology , Map<String,String> param ) throws Exception
    {
        this.nodePerRow = 10;
        
        if( param.containsKey( "nodePerRow" ) )
        {
            this.nodePerRow = Integer.parseInt( param.get( "nodePerRow" ) );
        }
        
        if( nodePerRow < 0 )
        {
            throw new Exception( "The 'nodePerLine' value is less than zero." );
        }
        
        return execute( topology );
    }
    
    @Override
    public Topology create( Topology topology ) throws Exception
    {
        this.nodePerRow = 10;
        
        return execute( topology );
    }
    
    private Topology execute( Topology topology ) throws Exception
    {
        int rowSize   = getNodeSize() < nodePerRow 
                        ? 1                                                        // caso seja menor
                        : (int) Math.ceil( (double) getNodeSize() / nodePerRow );  // caso seja maior
        
        int celHeight = heigth / rowSize ;
        int celWidth  =  width / rowSize ;
        
        int bsPosX    = (int) Math.round( nodePerRow / 2.0 );
        int bsPosY    = (int) Math.ceil (    rowSize / 2.0 );
        
        int bsx       = (celWidth  * bsPosX );
        int bsy       = (celHeight * bsPosY );
        
        // ----------------
        
        if( celWidth * nodePerRow > width )
        {
            String txt = "The algorithm can not put all the sensor nodes "
                       + "within the WSN. WSN width is X1, but it should "
                       + "be X2 using node per row equal to X3.";
            
            throw new Exception( txt.replaceFirst( "X1" , width + "" )
                                    .replaceFirst( "X2" , (celWidth * nodePerRow) + "" ) 
                                    .replaceFirst( "X3" , nodePerRow + "" ) );
        }
        else if( celHeight * rowSize > heigth )
        {
            String txt = "The algorithm can not put all the sensor nodes "
                       + "within the WSN. WSN height is X1, but it should "
                       + "be X2 using node per row equal to X3.";
            
            throw new Exception( txt.replaceFirst( "X1" , heigth + "" )
                                    .replaceFirst( "X2" , (celHeight * rowSize) + "" ) 
                                    .replaceFirst( "X3" , nodePerRow + "" ) );
        }
        
        // ----------------
        
        Node bs = getNodeDefault();
        bs.setId( 1 );
        bs.getProperties().put( "nodeType" , "BS" );
        bs.getProperties().put( "X" , bsx + "" );
        bs.getProperties().put( "Y" , bsy + "" );
        
        topology.getNodeMap().put( bs.getId() , bs );
        
        int pos = 2;
        for( int row = 1 ; row <= rowSize ; row++ )
        {
            for( int col = 1 ; col <= nodePerRow && pos <= getNodeSize() ; col++ )
            {
                if( col == bsPosX && row == bsPosY )    // caso seja a posicao da BS
                {
                    continue ;
                }
                
                Node node = getNode( topology , pos++ );
                node.getProperties().put( "X" , ( celWidth  * col ) + "" );
                node.getProperties().put( "Y" , ( celHeight * row ) + "" );
                
                topology.getNodeMap().put( node.getId() , node );
            }
        }
        
        return topology;
    }
    
}
