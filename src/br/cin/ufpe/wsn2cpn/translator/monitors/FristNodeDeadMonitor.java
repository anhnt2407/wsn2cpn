package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 * FND - First Node Dead
 * 
 * @author avld
 */
public class FristNodeDeadMonitor extends PercentageNodeDeadMonitor
{
 
    public FristNodeDeadMonitor()
    {
        
    }

    @Override
    public Ml getMl()
    {
        this.value = 0;
        return super.getMl();
    }
    
    
    
}
