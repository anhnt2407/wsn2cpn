package br.cin.ufpe.wsn2cpn.layer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author avld
 */
public class LayerContainer
{
    public static String PATH = "./layers/";
    private static LayerContainer instance;
    private Map<String,List<String>> layerMap;

    private LayerContainer()
    {
        layerMap = new HashMap<String, List<String>>();
    }

    public static LayerContainer getInstance()
    {
        if( instance == null )
        {
            instance = new LayerContainer();
            instance.discoveryLayers();
        }

        return instance;
    }

    private void discoveryLayers()
    {
        layerMap.put( "mac"         , buildLayerList( PATH + "mac/" )         );
        layerMap.put( "network"     , buildLayerList( PATH + "network/" )     );
        layerMap.put( "application" , buildLayerList( PATH + "application/" ) );
    }

    private List<String> buildLayerList(String dirname)
    {
        List<String> layerList = new ArrayList<String>();
        File dir = new File( dirname );

        for( String subfile : dir.list() )
        {
            if( subfile.endsWith( ".cpn" ) )
            {
                int end = subfile.indexOf( ".cpn" );
                layerList.add( subfile.substring( 0 , end ) );
            }
        }

        return layerList;
    }

    public Map<String,List<String>> getLayerMap()
    {
        return layerMap;
    }

    public List<String> getLayerList( String layer_name )
    {
        return layerMap.get( layer_name );
    }

    public Set<String> getLayerNameList()
    {
        return layerMap.keySet();
    }

    public LayerModelOpen openLayer( String layer_name , String model_name ) throws Exception
    {
        String filename = PATH + layer_name + "/" + model_name + ".cpn";

        LayerModelOpen opener = new LayerModelOpen();
        opener.process( filename , layer_name + " layer" );

        return opener;
    }

    public LayerConfiguration getConfiguration( String layerName , String modelName ) throws Exception
    {
        String filename = PATH + layerName + "/" + modelName+ ".xml";
        return LayerConfigurationFile.open( filename );
    }
}
