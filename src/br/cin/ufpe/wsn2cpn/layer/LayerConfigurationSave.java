package br.cin.ufpe.wsn2cpn.layer;

import java.io.FileWriter;

/**
 *
 * @author avld
 */
public class LayerConfigurationSave
{
    private String filename;
    private FileWriter writer;
    
    public LayerConfigurationSave( String filename )
    {
        this.filename = filename;
    }

    public void save( LayerConfiguration layer ) throws Exception
    {
        this.writer = new FileWriter( filename );

        writer.write( "<layer>" );
        writer.write( " <name>" + layer.getName() + "</name>\n" );
        writer.write( " <layer>" + layer.getLayer()+ "</layer>\n" );
        writer.write( " <developer>" + layer.getDeveloper()+ "</developer>\n" );
        writer.write( " <description>" + layer.getDescription()+ "</description>\n" );
        
        writer.write( " <variable_list>\n" );
        
        for( LayerProperty prop : layer.getVariableList() )
        {
            properties( prop );
        }

        writer.write( " </variable_list>\n" );
        
        // --------------------------------------------------- //
        // --------------------------------------------------- //
        // --------------------------------------------------- //
        
        writer.write( " <node_properties>\n" );
        
        for( LayerProperty prop : layer.getNodeProperties() )
        {
            properties( prop );
        }

        writer.write( " </node_properties>\n" );
        
        writer.write( "</layer>" );

        this.writer.close();
    }

    private void properties(LayerProperty prop) throws Exception
    {
        writer.write( "  <property>\n" );
        writer.write( "    <name>" + prop.getName() + "</name>\n" );
        
        if( prop.getNick() == null 
                ? false 
                : !prop.getNick().isEmpty() )
        {
            writer.write( "    <nick>" + prop.getNick() + "</nick>\n" );
        }
        
        writer.write( "    <type>" + prop.getType() + "</type>\n" );
        writer.write( "    <default>" + prop.getDefaultValue() + "</default>\n" );

        if( prop.isHidden() )
        {
            writer.write( "    <mandatory />\n" );
        }
        
        writer.write( "    <description>" 
                      + prop.getDescription()
                      + "</description>\n" );

        writer.write( "  </property>\n" );
    }

}
