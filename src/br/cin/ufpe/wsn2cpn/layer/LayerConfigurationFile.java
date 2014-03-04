package br.cin.ufpe.wsn2cpn.layer;

/**
 *
 * @author avld
 */
public class LayerConfigurationFile
{

    public static void save(String filename, LayerConfiguration layer) throws Exception
    {
        LayerConfigurationSave saver = new LayerConfigurationSave( filename );
        saver.save( layer );
    }

    public static LayerConfiguration open(String filename) throws Exception
    {
        LayerConfigurationOpen opener = new LayerConfigurationOpen( filename );
        return opener.open();
    }
    
    public static void main( String arg[] ) throws Exception
    {
        LayerConfiguration conf = open( "./layers/network/LEACH.xml" );
        System.out.println( "name: " + conf.getName() );
        
        System.out.println( "Variables" );
        for( LayerProperty p : conf.getVariableList() )
        {
            System.out.println( "  property name: " + p.getName() );
        }
        
        System.out.println( "Node Properties" );
        for( LayerProperty p : conf.getNodeProperties())
        {
            System.out.println( "  property name: " + p.getName() );
        }
    }
}
