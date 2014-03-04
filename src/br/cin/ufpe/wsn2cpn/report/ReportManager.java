package br.cin.ufpe.wsn2cpn.report;

import br.cin.ufpe.wsn2cpn.Topology;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class ReportManager extends Report
{
    private static ReportManager instance;
    
    private Map<String,Boolean> reportEnableMap;
    private Map<String,Report> reportMap;

    private ReportManager()
    {
        reportEnableMap = new HashMap<String, Boolean>();
        reportMap = new HashMap<String, Report>();
        
        init();
    }

    private void init()
    {
        addReport( "energy_map" , new EnergyMapReport() );
        addReport( "file_map" , new FileReport() );
    }

    private void addReport(String name, Report report)
    {
        reportMap.put( name , report );
        reportEnableMap.put( name , true );
    }

    public static ReportManager getIntance()
    {
        if( instance == null )
        {
            instance = new ReportManager();
        }

        return instance;
    }

    // ----------------------------------- //

    public Map<String, Boolean> getReportEnableMap()
    {
        return reportEnableMap;
    }

    public Map<String, Report> getReportMap()
    {
        return reportMap;
    }

    // ----------------------------------- //

    @Override
    public void setTime(long time)
    {
        for( Report report : reportMap.values() )
        {
            report.setTime( time );
        }
    }

    @Override
    public void setTopology(Topology topology)
    {
        for( Report report : reportMap.values() )
        {
            report.setTopology( topology );
        }
    }

    @Override
    public void make() throws Exception
    {
        for( Entry<String,Report> entry : reportMap.entrySet() )
        {
            if( reportEnableMap.get( entry.getKey() ) )
            {
                entry.getValue().make();
            }
        }
    }

    
}
