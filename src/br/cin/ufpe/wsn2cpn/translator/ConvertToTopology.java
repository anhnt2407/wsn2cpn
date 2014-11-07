package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;

/**
 *
 * @author avld
 */
public class ConvertToTopology
{
    private HighLevelSimulator simulator;
    private Double battery;
    
    public ConvertToTopology( HighLevelSimulator simulator )
    {
        this.simulator = simulator;
    }

    public Topology topology() throws Exception
    {
        Topology top = new Topology();

        //------------------ energyList
        battery = threatVariableDouble( "!battery" );
        List<Double> energyList = threatGlobrefList( "!energyList" );
        
        List<Node> nodeList = threatNodeList( "!nodeList" );
        for( Node node : nodeList )
        {
            double energy = energyList.get( node.getId() - 1 );
            node.getProperties().put( "energy"     , energy + "" );
            top.getNodeMap()    .put( node.getId() , node        );
        }
        
        return top;
    }

    private Double threatVariableDouble( String name ) throws Exception
    {
        String crude = simulator.evaluate( name );
        String token = getTokenValue( crude ).replaceAll( "~" , "-" );
        
        return Double.parseDouble( token );
    }
    
    private List<Double> threatGlobrefList( String name ) throws Exception
    {
        List<String> tokenValueList = getTokenValueFromList( name );
        List<Double> list           = new ArrayList<>();
        for( String valueCrude : tokenValueList )
        {
            String value = valueCrude.replace( "~" , "-" );
            list.add( Double.parseDouble( value ) );
        }

        return list;
    }

    private List<Node> threatNodeList( String name ) throws Exception
    {
        List<String> tokenClearList = getTokenValueFromList( name );
        return node( tokenClearList );
    }
    
    public List<Node> node(List<String> nodeTokenList)
    {
        List<Node> list = new ArrayList<>();

        for( String token : nodeTokenList )
        {
            list.add( node( token ) );
        }

        return list;
    }

    public Node node( String nodeStr )
    {
        if( nodeStr == null )
        {
            return null;
        }

        nodeStr = clear( nodeStr , '{' , '}' );

        Node node = new Node();
        node.setProperties( getProprities( nodeStr ) );
        node.getProperties().put( "battery" , battery + "" );

        if( node.getProperties().containsKey( "id" ) )
         {
            String idStr = node.getProperties().get( "id" );
            int id = Integer.parseInt( idStr );
            node.setId( id );
        }

        //corrigirParametro( node );
        
        return node;
    }

    @Deprecated
    private void corrigirParametro( Node node )
    {
        corrigirParamentro( node , "pos.x" , "X" );
        corrigirParamentro( node , "pos.y" , "Y" );
        
        corrigirParamentro( node , "x" , "X" );
        corrigirParamentro( node , "y" , "Y" );
    }
    
    @Deprecated
    private void corrigirParamentro( Node node , String before , String after )
    {
        String value = node.getProperties().remove( before );
        node.getProperties().put( after , value );
    }
    
    // ------------------------------------ //
    // ------------------------------------ //
    // ------------------------------------ //

    public String getTokenValue(String tokenCrude)
    {
        String start = "val it =";
        String token = tokenCrude.substring( start.length() );
        token = token.split( ":" )[0].trim();
        token = token.replace( '\n' , ' ' );

        while( token.indexOf( ", " ) != -1 )
        {
            token = token.replaceAll( ", " , "," );
        }

        return token;
    }

    @Deprecated
    private List<String> getTokenValueList(String tokenStr)
    {
        tokenStr = clear( tokenStr , '[' , ']' );
        List<String> tokenList = new ArrayList<>();

        for( int i = 0 ; i < tokenStr.length() ;  )
        {
            String value = getValue( i , tokenStr );

            i += value.length() + 1;

            tokenList.add( value.trim() );
        }

        return tokenList;
    }

    public List<String> getTokenValueFromList( String tokenStr ) throws Exception
    {
        // ----------- size
        String sizeCrude = simulator.evaluate( "length (" + tokenStr + ")" );
        String sizeStr   = getTokenValue     ( sizeCrude );
        int    size      = Integer.parseInt  ( sizeStr   );
        
        // ----------- get node individually
        List<String> tokenList = new ArrayList<>();
        for( int i = 0 ; i <  size ; i++ )
        {
            String expression = "List.nth (" + tokenStr + "," + i + ")";
            
            String crude    = simulator.evaluate( expression );
            String valueStr = getTokenValue( crude );
            
            tokenList.add( valueStr );
        }
        
        return tokenList;
    }
    
    private Map<String,String> getProprities(String tokenStr)
    {
        Map<String,String> prop = new HashMap<>();

        for( int i = 0 ; i < tokenStr.length() ;  )
        {
            int equalPos = tokenStr.substring( i ).indexOf( "=" );

            String key = tokenStr.substring( i , i + equalPos );
            String value = getValue( i + equalPos + 1 , tokenStr );

            i += 2 + equalPos + value.length();

            prop.put( key.trim() , value.trim() );
        }

        return prop;
    }

    private String getValue( int pos , String tokenStr )
    {
        String value = "";
        int counter = 0;

        for( int i = pos ; i < tokenStr.length() ; i++ )
        {
            char c = tokenStr.charAt( i );
            
            if( c == '{' || c == '[' )
            {
                counter++;
            }
            else if( c == '}' || c == ']' )
            {
                counter--;
            }

            if( c == ',' && counter == 0 )
            {
                break ;
            }

            value += c;
        }

        return value;
    }

    private String clear(String text , char start , char end )
    {
        if( text == null )
        {
            return "";
        }

        int last = text.length() - 1;

        if( text.charAt( 0 ) == start
                && text.charAt( last ) == end )
        {
            return text.substring( 1 , last );
        }
        else
        {
            return text;
        }
    }

}
