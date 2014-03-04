package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;

/**
 *
 * @author avld
 */
public abstract class SimulationMonitor implements Cloneable
{
    protected double value;
    protected int nodeSize;
    protected Page initNetworkPage;
    protected Trans ajusterTrans;
    
    public SimulationMonitor()
    {
        // do nothing
    }
    
    public void setValue( double value )
    {
        this.value = value;
    }
    
    public double getValue()
    {
        return this.value;
    }
    
    public void setNodeSize( int nodeSize )
    {
        this.nodeSize = nodeSize;
    }
    
    public void setNetworkPage( Page page )
    {
        this.initNetworkPage = page;
        
        for( Trans t : page.getTrans() )
        {
            if( "increment\ntime".equalsIgnoreCase( t.getText() ) )
            {
                this.ajusterTrans = t;
                break ;
            }
        }
    }
    
    public Monitor create( long id )
    {
        Monitor monitor = new Monitor();
        monitor.setName( "Criteria Stop" );
        monitor.setDisable( false );
        monitor.setType( 1 );
        monitor.setTypeDescription( "Breakpoint" );
        monitor.setDeclaration( getMl() );
        
        monitor.setNode( ajusterTrans.getId() , id );
        
        return monitor;
    }
    
    public abstract Ml getMl();
    
    @Override
    public SimulationMonitor clone() throws CloneNotSupportedException
    {
        return (SimulationMonitor) super.clone();
    }
}
