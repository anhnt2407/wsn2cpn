package br.cin.ufpe.wsn2cpn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author avld
 */
public class Node implements Serializable, Cloneable
{
    private int id;
    private boolean fixed;
    private Map<String,String> properties;
    
    // pos.x , pos.y
    // container, energy, power, time
    // message.sent, message.receive, message.forward
    // others

    public Node()
    {
        this.id = 0;
        this.fixed = false;
        this.properties = new HashMap<String,String>();
        
        this.addPropertiesBasic();
    }

    public Node(int id)
    {
        this.id = id;
        this.fixed = false;
        this.properties = new HashMap<String,String>();
        
        this.addPropertiesBasic();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Map<String,String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String,String> properties)
    {
        this.properties = properties;
    }

    public boolean isFixed()
    {
        return fixed;
    }

    public void setFixed( boolean fixed )
    {
        this.fixed = fixed;
    }

    private void addPropertiesBasic()
    {
        properties.put( "X" , "0" );                //meter
        properties.put( "Y" , "0" );                //meter
//        properties.put( "application" , "" );           //
//        properties.put( "type" , "" );                  //
//        properties.put( "battery" , "100" );            //millijoules (1E-3)
//        properties.put( "duty_cicle" , "0" );           //porcentagem
        properties.put( "range" , "200" );              //meter
        properties.put( "nodeType" , "NODE" );       //NORMAL , BS or CH

        // ------------------------ //

//        properties.put( "cpu" , "8000" );               //microAmpere (E-6)
//        properties.put( "radio_listen" , "8000" );      //microAmpere (E-6)
//        properties.put( "radio_tx" , "17000" );         //microAmpere (E-6)
//        properties.put( "radio_rx" , "16000" );         //microAmpere (E-6)
//        properties.put( "radio_amp" , "100" );          //pJ (E-12) / bit / m2
//        properties.put( "radio_bandwidth" , "250000" ); //bps

        // ------------------------ //

//        properties.put( "app_mode" , "0" );             // 0 - NOTHING ... 1 - ACTIVE
//        properties.put( "app_periodic" , "30000" );      //milli seconds (E-3)
//        properties.put( "app_nextTime" , "0" );         //milli seconds (E-3)
//        properties.put( "app_sendTo" , "1" );           //node id
    }

    @Override
    public String toString()
    {
        String txt = "NODE ID: " + getId() + "\n";

        for( Entry<String,String> entry : getProperties().entrySet() )
        {
            txt += " " + entry.getKey() + " = " + entry.getValue() + ";\n";
        }

        return txt;
    }
    
    @Override
    public Node clone() throws CloneNotSupportedException
    {
        Node node = new Node( getId() );
        node.getProperties().putAll( getProperties() );
        
        return node;
    }
}
