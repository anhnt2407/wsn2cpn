package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;

/**
 * HND - Half Node Dead
 * 
 * @author avld
 */
public class HalfNodeDeadMonitor extends PercentageNodeDeadMonitor
{
    
    public HalfNodeDeadMonitor()
    {
        super();
    }
    
    @Override
    public Monitor create( long id )
    {
        setValue( 50 );
        return super.create( id );
    }
}
