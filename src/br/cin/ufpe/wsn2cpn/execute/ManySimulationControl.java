package br.cin.ufpe.wsn2cpn.execute;

import br.cin.ufpe.wsn2cpn.Topology;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class ManySimulationControl
{
    private List<SimulationControl> list;
    
    public ManySimulationControl()
    {
        list = new ArrayList<>();
    }
    
    public void addTopologyList( List<Topology> l )
    {
        // Diferença entre o numero de resultados e o numero de controladores
        int dif = l.size() - list.size();
        
        // Caso a diferença entre eles maior do que zero, 
        // é iguala esse numero criando mais controladores
        if( dif > 0 )
        {
            for( int i = 0 ; i < dif ; i++ )
            {
                list.add( new SimulationControl() );
            }
        }
        
        // É necessário colocar cada resultado no respectivo controlador
        for( int i = 0 ; i < l.size() ; i++ )
        {
            Topology top = l.get( i );
            
            String timeStr = top.getConfigurationMap().get( "time_executed" );
            double time = Double.parseDouble( timeStr );
            
            list.get( i ).add( time , top );
        }
    }
    
    public List<Topology> process() throws CloneNotSupportedException
    {
        List<Topology> l = new ArrayList<>();
        for( int i = 0 ; i < list.size() ; i++ )
        {
            Topology top = list.get( i ).process();
            l.add( top );
        }
        
        return l;
    }
}
