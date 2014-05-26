package br.cin.ufpe.wsn2cpn;

import java.util.ArrayList;
import java.util.List;

/**
 * Especifica uma regiao na topologia
 * 
 * @author avld
 */
public class Region
{
    private int    regionId   ;
    private int    x          ;
    private int    y          ;
    private int    width      ;
    private int    height     ;
    private String descrition ;
    private List<Integer> sensorNodeList;
    
    public Region()
    {
        sensorNodeList = new ArrayList<>();
    }

    public int getRegionId()
    {
        return regionId;
    }

    public void setRegionId( int regionId )
    {
        this.regionId = regionId;
    }

    public int getX()
    {
        return x;
    }

    public void setX( int x )
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY( int y )
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }

    public String getDescrition()
    {
        return descrition;
    }

    public void setDescrition( String descrition )
    {
        this.descrition = descrition;
    }

    public List<Integer> getSensorNodeList()
    {
        return sensorNodeList;
    }

    public String getSensorNodes()
    {
        StringBuilder builder = new StringBuilder();
        
        for( Integer node : sensorNodeList )
        {
            builder.append( node ).append( "; " );
        }
        
        return builder.toString();
    }
    
    public void setSensorNodes( List<Integer> nodeList )
    {
        if( nodeList != null )
        {
            this.sensorNodeList = nodeList;
        }
        else
        {
            this.sensorNodeList.clear();
        }
    }
    
    public void setNodeList( String nodeStr ) throws Exception
    {
        if( nodeStr.indexOf( "," ) >= 0 )
        {
            throw new Exception( "Replace ',' to ';'" );
        }
        
        sensorNodeList.clear();
        String[] nodes = nodeStr.split( ";" );
        
        for( String n : nodes )
        {
            if( n == null 
                    ? true 
                    : n.trim().isEmpty() )
            {
                continue ;
            }
            
            if( n.indexOf( '-' ) != -1 )
            {
                String[] part = n.split( "-" );
                
                if( part.length < 2 )
                {
                    throw new Exception( "Invalid node, because someone putted '" + n
                                       + "'; when we expected '" + n + "NUMBER';" );
                }
                
                setNodeRange( part[0] , part[1] );
            }
            else
            {
                try
                {
                    sensorNodeList.add( Integer.parseInt( n.trim() ) );
                }
                catch( Exception err )
                {
                    throw new Exception( "We expected a number when someone "
                                       + "putted this " + n + "." );
                }
            }
        }
    }
    
    private void setNodeRange( String startStr , String endStr ) throws Exception
    {
        if( startStr == null 
                    ? true 
                    : startStr.trim().isEmpty() )
        {
            throw new Exception( "First number is empty "
                               + "when someone putted "+ startStr +"-"+ endStr +"." );
        }
        else if( endStr == null 
                    ? true 
                    : endStr.trim().isEmpty() )
        {
            throw new Exception( "Second number is empty "
                               + "when someone putted "+ startStr +"-"+ endStr +"." );
        }
        
        int start = Integer.parseInt( startStr.trim() );
        int end   = Integer.parseInt( endStr.trim()   );
        
        for( int i = start ; i <= end ; i++ )
        {
            sensorNodeList.add( i );
        }
    }
}
