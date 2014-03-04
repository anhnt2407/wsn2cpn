package br.cin.ufpe.wsn2cpn.translator.monitors;

import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorXML;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author avld
 */
public class MonitorFactory
{
    private static MonitorFactory instance;
    private Map<String,SimulationMonitor> monitorMap;
    
    private MonitorFactory()
    {
        init();
    }
    
    private void init()
    {
        monitorMap = new HashMap< String , SimulationMonitor >();
        
        monitorMap.put( "FND"       , new FristNodeDeadMonitor() );
        monitorMap.put( "PERCENTAGE" , new PercentageNodeDeadMonitor() );
        monitorMap.put( "HND"       , new HalfNodeDeadMonitor() );
        monitorMap.put( "LND"       , new LastNodeDeadMonitor() );
        monitorMap.put( "TIME"      , new TimeMonitor() );
    }
    
    public static SimulationMonitor getMonitor(String name) throws Exception
    {
        if( instance == null )
        {
            instance = new MonitorFactory();
        }
        
        String upper = name.toUpperCase();
        
        if( !instance.monitorMap.containsKey( upper ) )
        {
            throw new Exception( "This monitor does not exist." );
        }
        
        return instance.monitorMap.get( upper ).clone();
    }
    
    public static Set<String> getMonitorNameList()
    {
        if( instance == null )
        {
            instance = new MonitorFactory();
        }
        
        return instance.monitorMap.keySet();
    }
}
