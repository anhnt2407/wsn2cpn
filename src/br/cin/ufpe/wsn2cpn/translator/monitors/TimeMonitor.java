package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 * Time - receive a timeout (in seconds).
 * 
 * @author avld
 */
public class TimeMonitor extends SimulationMonitor
{
    
    public TimeMonitor()
    {
        // do nothing
    }

    @Override
    public Ml getMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun pred ( bindelem ) = \n" );
        builder.append( "(\n" );
        builder.append( "  " ).append( value ).append( " <= real (!timeGlobal)\n" );
        builder.append( "  orelse (getNodeSize() - 1) = getNodeDead()\n" );
        builder.append( ")" );
        
        return new Ml( builder.toString() );
    }
    
}
