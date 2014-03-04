package br.cin.ufpe.wsn2cpn;

import java.util.Map;

/**
 *
 * @author avld
 */
public class TopologyTest extends Topology
{

    public TopologyTest()
    {
        super();

        getConfigurationMap().put( "mac_layer"         , "perfect_mac" );
        getConfigurationMap().put( "network_layer"     , "direct" );
        getConfigurationMap().put( "application_layer" , "periodic" );
        
        getConfigurationMap().put( "criteria_stop" , "fnd" );
        getConfigurationMap().put( "size" , "5" );

        addNode1();
        addNode2();
        addNode3();
        addNode4();
    }

    private void addNode1()
    {
        Node node = new Node( 1 );
        Map<String,String> properties = node.getProperties();

        properties.put( "X"      , "00" );
        properties.put( "Y"      , "30" );
        
        properties.put ( "X_list"    , "[ 01 , 31 , 61 , 91 , 121 ]" );
        properties.put ( "Y_list"    , "[ 121 , 31 , 91 , 61 , 01 ]" );
        properties.put ( "energy_list"   , "[ 0.01 , 0.01 , 0.01 , 0.01 , 0.01 ]" );
        properties.put ( "range_list"    , "[ 200 , 200 , 200 , 200 , 200 ]" );
        
        getNodeMap().put( node.getId() , node );
    }

    private void addNode2()
    {
        Node node = new Node( 2 );
        Map<String,String> properties = node.getProperties();

        properties.put( "X"      , "24" );
        properties.put( "Y"      , "00" );

        properties.put ( "X_list"    , "[ 02 , 32 , 62 , 92 , 122 ]" );
        properties.put ( "Y_list"    , "[ 122 , 32 , 92 , 62 , 02 ]" );
        properties.put ( "energy_list"   , "[ 0.01 , 0.01 , 0.01 , 0.01 , 0.01 ]" );
        properties.put ( "range_list"    , "[ 200 , 200 , 200 , 200, 200 ]" );
        
        getNodeMap().put( node.getId() , node );
    }

    private void addNode3()
    {
        Node node = new Node( 3 );
        Map<String,String> properties = node.getProperties();

        properties.put( "X"      , "40" );
        properties.put( "Y"      , "55" );

        properties.put ( "X_list"    , "[ 03 , 33 , 63 , 93 , 123 ]" );
        properties.put ( "Y_list"    , "[ 123 , 33 , 93 , 63 , 03 ]" );
        properties.put ( "energy_list"   , "[ 0.01 , 0.01 , 0.01 , 0.01 , 0.01 ]" );
        properties.put ( "range_list"    , "[ 200 , 200 , 200 , 200 , 200 ]" );
        
        getNodeMap().put( node.getId() , node );
    }

    private void addNode4()
    {
        Node node = new Node( 4 );
        Map<String,String> properties = node.getProperties();

        properties.put( "X"      , "65" );
        properties.put( "Y"      , "15" );

        properties.put ( "X_list"    , "[ 04 , 34 , 64 , 94 , 124 ]" );
        properties.put ( "Y_list"    , "[ 124 , 34 , 94 , 64 , 04 ]" );
        properties.put ( "energy_list"   , "[ 0.01 , 0.01 , 0.01 , 0.01 , 0.01 ]" );
        properties.put ( "range_list"    , "[ 200 , 200 , 200 , 200 , 200 ]" );
        
        getNodeMap().put( node.getId() , node );
    }
}
