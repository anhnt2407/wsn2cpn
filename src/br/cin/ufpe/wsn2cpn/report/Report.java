package br.cin.ufpe.wsn2cpn.report;

import br.cin.ufpe.wsn2cpn.Topology;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author avld
 */
public abstract class Report
{
    private Topology topology;
    private long time;
    private String title;
    private long periodicTime;

    public Report()
    {
        // do nothing
    }

    public Report(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public List<Connection> getConnectionList()
    {
        //TODO: como definir as conexoes?
        return null;
    }

    public Topology getTopology()
    {
        return topology;
    }

    public void setTopology(Topology topology)
    {
        this.topology = topology;
    }

    public String getDescription()
    {
        return "";
    }

    public long getPeriodicTime()
    {
        return periodicTime;
    }

    public void setPeriodicTime(long periodicTime)
    {
        this.periodicTime = periodicTime;
    }

    public abstract void make() throws Exception;

}
