package br.cin.ufpe.wsn2cpn.execute;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.layer.LayerModelOpen;
import br.cin.ufpe.wsn2cpn.translator.WsnTranslatorToCpn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 *
 * @author avld
 */
public abstract class WsnCpnExecute
{
    private Map<Integer,Integer> relationMap;
    protected String filename;

    public WsnCpnExecute( String filename )
    {
        this.filename = filename;
    }

    public abstract List<Topology> executar() throws Exception;

    public void analyseSequencialNodes( Topology topology )
    {
        int total = topology.getNodeMap().size();

        Map<Integer,Node> nodeMap = new HashMap<>();
        List<Integer> idExtraList = new ArrayList<>();

        //identifica quais IDs estao acima do ID MAXIMO (que é o total);
        for( Node node : topology.getNodeMap().values() )
        {
            if( total < node.getId() )
            {
                idExtraList.add( node.getId() );
            }
            else
            {
                nodeMap.put( node.getId() , node );
            }
        }

        //Os nos sensores com IDs acima do ID MAXIMO terao outros IDs
        relationMap = new HashMap<>();
        for( int i = 1; !idExtraList.isEmpty() ; i++ )
        {
            if( !nodeMap.containsKey( i ) )
            {
                int node_id = idExtraList.remove( 0 );

                Node node = topology.getNodeMap().get( node_id );
                node.setId( i );

                nodeMap.put( i , node );

                mudar( topology , node_id , i );
                relationMap.put( i , node_id );
            }
        }

        topology.setNodeMap( nodeMap );
    }

    public void desfazerSequencia( List<Topology> topologyList )
    {
        for( Topology top : topologyList )
        {
            desfazerSequencia( top );
        }
    }
    
    public void desfazerSequencia( Topology topology )
    {
        if( relationMap == null )
        {
            return ;
        }
        
        for( Entry<Integer,Integer> entry : relationMap.entrySet() )
        {
            mudar( topology , entry.getKey() , entry.getValue() );
            
            Node node = topology.getNodeMap().remove( entry.getKey() );
            node.setId( entry.getValue() );

            topology.getNodeMap().put( node.getId() , node );
        }
    }

    private void mudar( Topology topology , int before , int after )
    {
        for( Node node : topology.getNodeMap().values() )
        {
            mudarProperty( node.getProperties() , "app_sendTo" , before , after );
        }
    }

    private void mudarProperty( Map<String,String> properties, String name , int before , int after )
    {
        String value_str = properties.get( name );
        int value = Integer.parseInt( value_str );

        if( value == before )
        {
            properties.put( name , after + "" );
        }
    }

    public void traduzir( Topology top0 ) throws Exception
    {
        WsnTranslatorToCpn transltor = new WsnTranslatorToCpn();
        transltor.buildModel( top0 );
        transltor.save( filename );
    }

    public void traduzir( Topology top0 , LayerModelOpen appLayerOpener ) throws Exception
    {
        WsnTranslatorToCpn transltor = new WsnTranslatorToCpn();
        transltor.buildModel( top0 , appLayerOpener );
        transltor.save( filename );
    }

//    protected DescriptiveStatistics getList(String filename) throws Exception
//    {
//        File file = new File( filename );
//
//        System.out.println( "openning ...: " + file.getAbsolutePath() );
//
//        BufferedReader in = new BufferedReader( new FileReader( file ) );
//        String str = "";
//
//        DescriptiveStatistics result = new DescriptiveStatistics();
//        while ( (str = in.readLine() ) != null)
//        {
//            str = str.replace( '~' , '-' );
//            str = str.replace( ',' , '.' );
//            result.addValue( Double.parseDouble( str ) );
//        }
//
//        return result;
//    }
}
