package br.cin.ufpe.wsn2cpn.layer;

import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.load.CpnLoad;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author avld
 */
public class LayerModelOpen
{
    private CPN cpn;
    private LayerConfiguration configuration;
    private List<Block> profileList;

    public LayerModelOpen()
    {
        profileList = new LinkedList<Block>();
    }

    public void process( String filename , String layer ) throws Exception
    {
        CpnLoad loader = new CpnLoad( filename );
        cpn = loader.getCpn();

        findProfiles( cpn.getGlobbox() );
        
        openConfiguration( filename );
    }

    private void findProfiles( List<BlockItem> blockItemList )
    {
        for( BlockItem item : blockItemList )
        {
            if( item instanceof Block )
            {
                Block block = (Block) item;
                
                if( block.getNameId().startsWith( "Profile 0" ) )
                {
                    profileList.add( block );
                }
                else
                {
                    findProfiles( block.getItemList() );
                }
            }
        }
    }
    
    private void openConfiguration( String modelName ) throws Exception
    {
        int ext = modelName.lastIndexOf( ".cpn" );
        String filename = modelName.substring( 0 , ext ) + ".xml";
        
        configuration = LayerConfigurationFile.open( filename );
    }
    
    public CPN getCPN()
    {
        return cpn;
    }

    public void setConfiguration( LayerConfiguration conf )
    {
        this.configuration = conf;
    }
    
    public LayerConfiguration getConfiguration()
    {
        return configuration;
    }
    
    public List<Block> getProfilesList()
    {
        return profileList;
    }
}
