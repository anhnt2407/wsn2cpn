package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Color;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.RecordField;
import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.layer.LayerProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class GlobboxAjust
{
    private CPN cpn;
    private List<LayerProperty> nodePropertyList;
    private Topology topology;
    private ProfileControl profileControl;
    
    public GlobboxAjust( CPN cpn )
    {
        this.cpn = cpn;
        this.nodePropertyList = new ArrayList<LayerProperty>();
    }
    
    public void setTopology( Topology top )
    {
        this.topology = top;
    }
    
    public void setProfileControl( ProfileControl control )
    {
        this.profileControl = control;
    }
    
    public void addNodeProperty( List<LayerProperty> nodePropertyMap )
    {
        this.nodePropertyList.addAll( nodePropertyMap );
    }
    
    public List<LayerProperty> getNodePropertyList()
    {
        return nodePropertyList;
    }
    
    /**
     * TODO:
     * [OK] - colocar os Profiles;
     * [OK] - trocar as funções createProfiles, clonePacket, willRoute e ProcessPacket;
     * [OK] - criar os nós;
     * - mudar os valores dos globrefs;
     * 
     */
    public void ajust() throws CloneNotSupportedException
    {
        changeCPN( cpn.getGlobbox() );
    }
    
    private void changeCPN( List<BlockItem> cpnItemList ) throws CloneNotSupportedException
    {
        for( BlockItem item : cpnItemList )
        {
            if( item instanceof Block )
            {
                Block block = (Block) item;
                
                if( block.getNameId().equalsIgnoreCase( "Generic Functions" ) )
                {
                    System.out.println( "Reeconfigurando o 'Generic Functions'..." );
                    block.getItemList().clear();
                    
                    block.getItemList().add( profileControl.getShouldSendMl()    );
                    block.getItemList().add( profileControl.getClonePacketMl()   );
                    block.getItemList().add( profileControl.getProcessPacketMl() );
                    block.getItemList().add( profileControl.getWillRouteMl()     );
                }
                else if( block.getNameId().equalsIgnoreCase( "Existing Profiles" ) )
                {
                    System.out.println( "Reeconfigurando o 'Existing Profiles'..." );
                    for( Block b : profileControl.getBlockList() )
                    {
                        block.getItemList().add( b );
                    }
                }
                if( block.getNameId().equalsIgnoreCase( "Many Topologies" ) )
                {
                    System.out.println( "Reeconfigurando o 'Many Topologies'..." );
                    block.getItemList().clear();
                    
                    ManyTopologies topologies = new ManyTopologies( topology );
                    for( Ml ml : topologies.getCreateTopologyList() )
                    {
                        block.getItemList().add( ml );
                    }
                    
                    block.getItemList().add( new Ml( topologies.getCreateNodes() ) );
                }
                else
                {
                    changeCPN( ((Block) item).getItemList() );
                }
            }
            else if( item instanceof Ml )
            {
                Ml mlItem = (Ml) item;
                
                if( mlItem.getLayout() == null )
                {
                    continue ;
                }
//                else if( mlItem.getLayout().startsWith("fun createNodes()" ) )
//                {
//                    System.out.println( "Reeconfigurando o 'createNodes()'..." );
//                    mlItem.getlistLayout().set( 0 , getCreateNodes() );
//                }
                else if( mlItem.getLayout().startsWith("fun createProfiles()" ) )
                {
                    System.out.println( "Reeconfigurando o 'createProfiles()'..." );
                    Ml createProfiles = profileControl.getCreateProfilesMl();
                    mlItem.getlistLayout().set( 0 , createProfiles.getLayout() );
                }
                else if( mlItem.getLayout().startsWith("fun initVariables()" ) )
                {
                    System.out.println( "Reeconfigurando o 'initVariables()'..." );
                    mlItem.getlistLayout().set( 0 , getInitVariables() );
                }
            }
            else if( item instanceof Color )
            {
                Color color = (Color) item;
                
                if( "MOTE".equals( color.getNameId() ) )
                {
                    changeNodeProperty( color );
                }
            }
        }
    }
    
    private void changeNodeProperty( Color color )
    {
        StringBuilder builder = new StringBuilder();
        
        for( LayerProperty prop : nodePropertyList )
        {
            RecordField field = new RecordField( prop.getName() , prop.getType() );
            
            if( !color.getRecord().contains( field ) )
            {
                color.getRecord().add( field );
            }
            
            builder.append( "\n   * " ).append( prop.getName() ).append( " : " );
            builder.append( prop.getType() );
        }
        
        if( nodePropertyList.size() + 4 > 10 )
        {
            builder.append( "\n   declare set;" );
        }
        else
        {
            builder.append( ";" );
        }
        
        if( !builder.toString().isEmpty() )
        {
            String layout = color.getLayout().replace( ";" , builder.toString() );
            color.setLayout( layout );
        }
    }
    
    
    
    private String getInitVariables()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun initVariables() =\n" );
        builder.append( "(\n" );
        
        for( Entry<String,String> entry : topology.getVariableMap().entrySet() )
        {
            builder.append( "  " ).append( entry.getKey() );
            builder.append( " := " ).append( entry.getValue() );
            builder.append( ";\n" );
        }
        
        builder.append( "\n" );
        builder.append( "  energyList := nil;\n" );
        builder.append( "  deadNodeList := nil\n" );
        
        builder.append( ");" );
        
        return builder.toString();
    }
}
