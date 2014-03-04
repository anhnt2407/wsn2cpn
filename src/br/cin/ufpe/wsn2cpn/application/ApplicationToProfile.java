package br.cin.ufpe.wsn2cpn.application;

import br.cin.ufpe.nesc2cpn.Nesc2CpnResult;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.wsn2cpn.layer.LayerConfiguration;
import br.cin.ufpe.wsn2cpn.layer.LayerModelOpen;
import br.cin.ufpe.wsn2cpn.layer.LayerProperty;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @deprecated foi criado um arquivo especifico a parte (application_model).
 * @author avld
 */
public class ApplicationToProfile
{
    private Nesc2CpnResult result;
    
    public ApplicationToProfile()
    {
        // do nothing
    }
    
    public LayerModelOpen process( String filename ) throws FileNotFoundException, IOException
    {
        result = new Nesc2CpnResult();
        result.load( filename );
        
        return process( result );
    }
    
    public LayerModelOpen process( Nesc2CpnResult result )
    {
        this.result = result;
        
        LayerModelOpen layerOpener = new LayerModelOpen();
        layerOpener.getProfilesList().add( getProfile01() );
        layerOpener.getProfilesList().add( getProfile02() );
        
        layerOpener.setConfiguration( getConfiguration() );
        
        return layerOpener;
    }
    
    /**
     * O primeiro Profile é usado para enviar o pacote periodicamente pela rede.
     * 
     * @return 
     */
    private Block getProfile01()
    {
        String createProfile = "fun createProfile001( id : INT ) =\n"
                             + "(\n"
                             + "  {"
                             + "    id = id ,\n"
                             + "    nodeType = NODE ,\n"
                             + "    maxTime = 9999 ,\n"
                             + "    seconds = "+ result.getTimeToSendMean() +" ,\n"
                             + "    start = 0 ,"
                             + "    packet = newPacket ( id , 0 , 0 )\n"
                             + "  }\n"
                             + ");";
        
        // --------- //
        
        String clonePacket   = "fun clonePacket001( p : PACKET , n : MOTE ) =\n"
                             + "let\n"
                             + "  val p  = clonePacket000( p , n );\n"
                             + "  val p1 = PACKET.set_to p (#sendTo n);\n"
                             + "in\n"
                             + "  PACKET.set_destination p1 (#sendTo n)\n"
                             + "end;";
        
        // --------- //
        
        Block block = new Block( "Profile 001" );
        block.add( new Ml( createProfile ) );
        block.add( new Ml( clonePacket   ) );
        
        return block;
    }
    
    /**
     * O segundo Profile é exclusivo para contabilizar o consumo
     * de energia da aplicação no nó sensor.
     * 
     * @return 
     */
    private Block getProfile02()
    {
        String createProfile = "fun createProfile002( id : INT ) =\n"
                             + "(\n"
                             + "  {"
                             + "    id = id ,\n"
                             + "    nodeType = ALL ,\n"
                             + "    maxTime = 9999 ,\n"
                             + "    seconds = 1 ,\n"
                             + "    start = 0 ,"
                             + "    packet = newPacket ( id , 0 , 0 )\n"
                             + "  }\n"
                             + ");";
        
        // --------- //
        
        String energy = ( result.getEnergyPerSecondMean() + "" ).replace( "-" , "~" );
        
        String shouldSend    = "fun shouldSend002(  p : PROFILE , n : MOTE ) =\n"
                             + "let\n"
                             + "  val nodeJ = getEnergy( #id n );\n"
                             + "  val appJ  = "+ energy +";\n"
                             + "in\n"
                             + "  if ( #nodeType n = BS orelse #nodeType n = CH ) then "
                             + "     ( false )\n" 
                             + "  else"
                             + "     ( setEnergy( #id n , nodeJ + appJ ); false )\n"
                             + "end;";
        
        // --------- //
        
        Block block = new Block( "Profile 002" );
        block.add( new Ml( createProfile ) );
        block.add( new Ml( shouldSend   ) );
        
        return block;
    }
    
    private LayerConfiguration getConfiguration()
    {
        LayerConfiguration conf = new LayerConfiguration();
        conf.setName        ( "profileCreatedByWsn2Cpn" );
        conf.setLayer       ( "application" );
        conf.setDescription ( "This model is created by wsn2cpn after evaluate a application model." );
        conf.setDeveloper   ( "wsn2cpn" );
        conf.getNodeProperties().add( new LayerProperty( "sendTo" , "int" , "1" ) );
        
        return conf;
    }
}
