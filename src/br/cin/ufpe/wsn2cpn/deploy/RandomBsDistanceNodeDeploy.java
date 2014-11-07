package br.cin.ufpe.wsn2cpn.deploy;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author avld
 */
public class RandomBsDistanceNodeDeploy extends NodeDeploy
{
    private int min;
    private int max;
    
    public RandomBsDistanceNodeDeploy()
    {
        this.name = "Random - BS Distance";
    }

    @Override
    public Map<String,String> getParameterMap()
    {
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put( "min" , "10" );
        paramMap.put( "max" , "50" );
        
        return paramMap;
    }
    
    @Override
    public Topology create( Topology topology ) throws Exception
    {
        this.min = 10;
        this.min = 50;
        
        return execute( topology );
    }
    
    @Override
    public Topology create( Topology topology , Map<String,String> param ) throws Exception
    {
        this.min = 10;
        this.max = 50;
        
        if( param.containsKey( "min" ) )
        {
            this.min = Integer.parseInt( param.get( "min" ) );
        }
        
        if( param.containsKey( "max" ) )
        {
            this.max = Integer.parseInt( param.get( "max" ) );
        }
        
        if( max < min )
        {
            throw new Exception( "The 'min' value can not be less than 'max' value." );
        }
        
        return execute( topology );
    }
    
    private Topology execute( Topology top ) throws Exception
    {
        int bsX = width / 2;
        int bsY = heigth / 2;
        
        Node bs = getNodeDefault();
        bs.setId( 1 );
        bs.getProperties().put( "nodeType" , "BS" );
        bs.getProperties().put( "X" , bsX + "" );
        bs.getProperties().put( "Y" , bsY + "" );
        
        top.getNodeMap().put( bs.getId() , bs );
        
        for( int i = 2; i <= nodeSize; i++ )
        {
            Node node = getNode( top , i );
            
            if( node.isFixed() )
            {
                continue ;
            }
            
            int pos_x = 0;
            int pos_y = 0;
            
            double de = 0;
            
            do
            {
                pos_x = (int) ( x + Math.random() * width );
                pos_y = (int) ( y + Math.random() * heigth );
                
                de = distanceEuclidian( pos_x , pos_y , bsX , bsY );
            }
            while( min > de || de > max );
            
            node.getProperties().put( "X" , pos_x + "" );
            node.getProperties().put( "Y" , pos_y + "" );
            
            top.getNodeMap().put( node.getId() , node );
        }
        
        return top;
    }
    
    public double distanceEuclidian( int nX , int nY , int bsX , int bsY )
    {
        return Math.sqrt( Math.pow( nX - bsX , 2) + Math.pow( nY - bsY , 2) );
    }
    
}
