package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class LastNodeDeadMonitor extends SimulationMonitor
{
    
    public LastNodeDeadMonitor()
    {
        super();
    }
    
    @Override
    public Ml getMl()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "fun pred ( bindelem ) = \n" );
        builder.append( "(\n" );
        builder.append( " allNodeIsDead()\n" );
        builder.append( ")" );
        
        return new Ml( builder.toString() );
    }
    
}
