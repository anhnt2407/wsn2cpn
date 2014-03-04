package br.cin.ufpe.wsn2cpn;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author avld
 */
public class Packet
{
    private int id;
    private Map<String,String> properties;

    public Packet()
    {
        addBasicProprities();
    }

    public Packet(int id)
    {
        this.id = id;
        addBasicProprities();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    private void addBasicProprities()
    {
        properties = new HashMap<String, String>();
        properties.put( "id" , "0" );
        properties.put( "cost" , "0" );
        properties.put( "source" , "0" );
        properties.put( "destination" , "0" );
        properties.put( "from" , "0" );
        properties.put( "to" , "0" );
        properties.put( "processBy" , "0" );
        properties.put( "performance" , "0" );
        properties.put( "routingType" , "APPLICATION" );
        properties.put( "lost" , "NOT_LOST" );
    }
}
