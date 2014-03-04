package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class ProfileControl
{
    private Map< Integer , Block   > profileMap      ;
    private Map< Integer , Boolean > createProfileMap;
    private Map< Integer , Boolean > willRouteMap    ;
    private Map< Integer , Boolean > processPacketMap;
    private Map< Integer , Boolean > clonePacketMap  ;
    private Map< Integer , Boolean > shouldSendMap   ;
    
    public ProfileControl()
    {
        profileMap          = new HashMap<Integer, Block>();
        createProfileMap    = new HashMap<Integer, Boolean>();
        willRouteMap        = new HashMap<Integer, Boolean>();
        processPacketMap    = new HashMap<Integer, Boolean>();
        clonePacketMap      = new HashMap<Integer, Boolean>();
        shouldSendMap       = new HashMap<Integer, Boolean>();
    }
    
    public void add( List<Block> blockList )
    {
        for( Block block : blockList )
        {
            add( block );
        }
    }
    
    public void add( Block block )
    {
        System.out.println( "Adicionando um profile..." );
        
        String numberStr = block.getNameId().split( " " )[1];
        int number = Integer.parseInt( numberStr );
        
        if( number != profileMap.size() + 1 )
        {
            number = profileMap.size() + 1;
        }
        
        createProfileMap.put( number , Boolean.FALSE );
        willRouteMap    .put( number , Boolean.FALSE );
        processPacketMap.put( number , Boolean.FALSE );
        clonePacketMap  .put( number , Boolean.FALSE );
        shouldSendMap   .put( number , Boolean.FALSE );
        
        profileMap.put( number , renameBlock( block , number ) );
    }
    
    private Block renameBlock( Block block , int number )
    {
        List<BlockItem> itemList = renameBlockItem( block.getItemList() , number);
        
        Block blockNew = new Block( "Profile " + number );
        blockNew.setItemList( itemList );
        
        return blockNew;
    }
    
    private List<BlockItem> renameBlockItem( List<BlockItem> l , int number )
    {
        List<BlockItem> itemList = new LinkedList<BlockItem>();
        
        for( BlockItem item : l )
        {
            if ( item.getLayout() == null )
            {
                itemList.add( item );
            }
            else if( item.getLayout().startsWith( "fun createProfile" ) )
            {
                createProfileMap.put( number , Boolean.TRUE );
                itemList.add( renameMl( (Ml) item 
                            , "createProfile" + number ) );
            }
            else if( item.getLayout().startsWith( "fun willRoute" ) )
            {
                willRouteMap.put( number , Boolean.TRUE );
                itemList.add( renameMl( (Ml) item 
                            , "willRoute" + number ) );
            }
            else if( item.getLayout().startsWith( "fun processPacket" ) )
            {
                processPacketMap.put( number , Boolean.TRUE );
                itemList.add( renameMl( (Ml) item 
                            , "processPacket" + number ) );
            }
            else if( item.getLayout().startsWith( "fun clonePacket" ) )
            {
                clonePacketMap.put( number , Boolean.TRUE );
                itemList.add( renameMl( (Ml) item 
                            , "clonePacket" + number ) );
            }
            else if( item.getLayout().startsWith( "fun shouldSend" ) )
            {
                shouldSendMap.put( number , Boolean.TRUE );
                itemList.add( renameMl( (Ml) item 
                            , "shouldSend" + number ) );
            }
            else if( item.getLayout().startsWith( "fun conditions" ) )
            {
                itemList.add( renameMl( (Ml) item 
                            , "conditions" + number ) );
            }
            else
            {
                itemList.add( item );
            }
        }
        
        return itemList;
    }
    
    private Ml renameMl( Ml ml , String newName )
    {
        int parentese = ml.getLayout().indexOf( "(" );
        String newLayout = "fun " + newName + ml.getLayout().substring( parentese );
        
        return new Ml( newLayout );
    }
    
    public List<Block> getBlockList()
    {
        List<Block> blockList = new LinkedList<Block>();
        
        for( int i = 1 ; i <= profileMap.size() ; i++ )
        {
            blockList.add( profileMap.get( i ) );
        }
        
        return blockList;
    }
    
    public Ml getCreateProfilesMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun createProfiles() =\n" );
        builder.append( "(\n" );
        builder.append( "  profileList := nil" );
        
        for( int i = 1 ; i <= profileMap.size() ; i++ )
        {
            boolean create = createProfileMap.get( i );
            
            if( !create )
            {
                continue ;
            }
                
            String functionName = "  addProfile( createProfile" + i + "( " + i + " ) )";
            
            builder.append( ";\n" );
            builder.append( functionName );
        }
        
        builder.append( "\n);" );
        
        return new Ml( builder.toString() );
    }
    
    public Ml getClonePacketMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun clonePacket( s : PROFILE , n : MOTE ) =\n" );
        builder.append( "let\n" );
        builder.append( "  val p = #packet s;\n" );
        builder.append( "in\n" );
        builder.append( getLayout( clonePacketMap , "clonePacket" ) );
        builder.append( "\nend;" );
        
        return new Ml( builder.toString() );
    }
    
    public Ml getWillRouteMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun willRoute( p : PACKET , n : MOTE ) =\n(\n" );
        builder.append( getLayout( willRouteMap , "willRoute" ) );
        builder.append( "\n);" );
        
        return new Ml( builder.toString() );
    }
    
    public Ml getProcessPacketMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun processPacket( p : PACKET , n : MOTE ) =\n(\n" );
        builder.append( getLayout( processPacketMap , "processPacket" ) );
        builder.append( "\n);" );
        
        return new Ml( builder.toString() );
    }
    
    public Ml getShouldSendMl()
    {
        String funDefault = shouldSendMap.get( 1 ) ? "shouldSend1" : "shouldSend000";
        
        StringBuilder builder = new StringBuilder();
        builder.append( "fun shouldSend( p : PROFILE , n : MOTE ) =\n(\n" );
        builder.append( getLayout( shouldSendMap , "shouldSend" , funDefault , "#id" ) );
        builder.append( "\n);" );
        
        return new Ml( builder.toString() );
    }
    
    private String getLayout( Map<Integer,Boolean> funMap , String funName )
    {
        return getLayout( funMap , funName , funName + "1" , "#profile_id" );
    }
    
    private String getLayout( Map<Integer,Boolean> funMap , String funName , String funDefault , String attribute )
    {
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        
        for( int i = 1 ; i <= profileMap.size() ; i++ )
        {
            boolean create = funMap.get( i );
            
            if( !create )
            {
                continue ;
            }
            else if( first )
            {
                builder.append( "  " );
                first = false;
            }
            else
            {
                builder.append( "  else " );
            }
            
            builder.append( "if( "+ attribute +" p = " ).append( i ).append( ") then\n" );
            builder.append( "    " ).append( funName ).append( i ).append( "( p , n )\n" );
        }
        
        if( !builder.toString().isEmpty() )
        {
            builder.append( "  else " );
        }
        else
        {
            builder.append( "  " );
        }
        
        builder.append( funDefault ).append("( p , n )");
        
        return builder.toString();
    }
    
}
