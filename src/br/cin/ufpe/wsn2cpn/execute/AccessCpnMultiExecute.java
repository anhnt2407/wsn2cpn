package br.cin.ufpe.wsn2cpn.execute;

import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.debug.Debug;
import java.util.List;

/**
 * Esta classe irá executar o modelo diversas vezes.
 * 
 * @author avld
 */
public class AccessCpnMultiExecute extends AccessCpnSingleExecute
{
    private int time;
    
    public AccessCpnMultiExecute( String filename , Debug debug )
    {
        super( filename , debug );
    }

    /**
     * Esse método avaliar o modelo diversas vezes!
     * No entanto, esse método precisa ser efeito!
     * 
     * @return
     * @throws Exception 
     */
    @Override
    public List<Topology> startSimulation() throws Exception
    {
        if( time < 1 )
        {
            time = 1;
        }
        
        // ---------- Armazena os resultados da avaliação
        ManySimulationControl manySC = new ManySimulationControl();
        for( int i = 0 ; i < time ; i++ )
        {
            List<Topology> list = super.startSimulation();
            manySC.addTopologyList( list );
        }
        
        return manySC.process();
    }

    public int getTime()
    {
        return time;
    }

    public void setTime( int time )
    {
        this.time = time;
    }
    
}

