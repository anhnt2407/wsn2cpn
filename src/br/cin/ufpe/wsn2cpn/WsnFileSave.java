package br.cin.ufpe.wsn2cpn;

import java.io.FileWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class WsnFileSave
{
    private StringBuilder builder;

    public WsnFileSave()
    {
        builder = new StringBuilder();
    }

    public void process(Topology topology)
    {
        builder.append( "<topology>\n\n" );

        processConfiguration( topology.getConfigurationMap() );
        processVariable( topology.getVariableMap() );
        processRegion( topology.getRegionMap() );
        processNode( topology.getNodeMap().values() );

        builder.append( "</topology>" );
    }

    private void processConfiguration( Map<String,String> configurationMap )
    {
        builder.append( " <configurations>\n" );
        
        for( Entry<String,String> entry : configurationMap.entrySet() )
        {
            processProperty( entry );
        }

        builder.append( " </configurations>\n\n" );
    }

    private void processVariable( Map<String,String> variableMap )
    {
        builder.append( " <variables>\n" );
        
        for( Entry<String,String> entry : variableMap.entrySet() )
        {
            processProperty( entry );
        }

        builder.append( " </variables>\n\n" );
    }

    private void processRegion( Map<Integer,Region> regionMap )
    {
        builder.append( " <regions>\n" );
        
        for( Region region : regionMap.values() )
        {
            builder.append("  <region>\n" );
            
            builder.append( "    <id>" ).append( region.getX() ).append( "</id>\n" );
            builder.append( "    <x>" ).append( region.getX() ).append( "</x>\n" );
            builder.append( "    <y>" ).append( region.getX() ).append( "</y>\n" );
            builder.append( "    <width>" ) .append( region.getWidth() ) .append( "</width>\n" );
            builder.append( "    <height>" ).append( region.getHeight() ).append( "</height>\n" );
            builder.append( "    <description>" ).append( region.getDescrition()).append( "</description>\n" );
            builder.append( "    <nodes>" ) .append( region.getSensorNodes() ).append( "</nodes>\n" );
            
            builder.append( "  </region>\n\n" );
        }

        builder.append( " </regions>\n\n" );
    }
    
    private void processNode( Collection<Node> nodeCollection )
    {
        builder.append( " <nodes>\n\n" );

        for( Node node : nodeCollection )
        {
            builder.append("  <node id=\"" )
                    .append( node.getId() )
                    .append( "\">\n");

            for( Entry<String,String> entry : node.getProperties().entrySet() )
            {
                processProperty( entry );
            }

            builder.append( "  </node>\n\n" );
        }

        builder.append( " </nodes>\n\n" );
    }

    private void processProperty(Entry<String,String> entry)
    {
        builder.append( "   <property " );

        builder.append( "name=\"" )
                .append( entry.getKey() )
                .append( "\" " );

        builder.append( "value=\"" )
                .append( entry.getValue() )
                .append( "\" />\n" );
    }

    public void save(String filename) throws Exception
    {
        FileWriter writer = new FileWriter( filename );
        writer.write( "<?xml version=\"1.0\"?>\n" );
        writer.write( builder.toString() );
        writer.close();
    }
    
    public String getXML()
    {
        return builder.toString();
    }
}
