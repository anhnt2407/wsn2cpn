package br.cin.ufpe.wsn2cpn.translator.colectInformation;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;
import java.util.Map;

/**
 *
 * @author avld
 */
public class TimeBreakPoint extends Monitor
{
    
    public TimeBreakPoint( Double value , Map<Long,Long> node )
    {
        setName( "Colect Information" );
        setDisable( false );
        setType( 1 );
        setTypeDescription( "Breakpoint" );
        setDeclaration( getMl( value ) );
        setNode( node );
        
        //setNode( transId , pageId );
        //É necessário colocar os nodes!
    }
    
    private Ml getMl( Double value )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun pred ( bindelem ) = \n" );
        builder.append( "(\n" );
        builder.append( "  if ( " ).append( value ).append( " <= real (!timeGlobal) ) then false\n" );
        builder.append( "  else if( !timeNext <= !timeGlobal ) then\n" );
        builder.append( "  (\n" );
        builder.append( "    timeNext := !timeNext + !timeInterval;\n" );
        builder.append( "    true\n" );
        builder.append( "  )\n" );
        builder.append( "  else false\n" );
        builder.append( ")" );
        
        return new Ml( builder.toString() );
    }
    
}
