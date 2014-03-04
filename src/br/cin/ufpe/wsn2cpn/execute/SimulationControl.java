package br.cin.ufpe.wsn2cpn.execute;

import br.cin.ufpe.wsn2cpn.Node;
import br.cin.ufpe.wsn2cpn.Topology;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class SimulationControl
{
    private List<Double> timeList;
    private List<Topology> topologyList;
    private Topology topology;
    private int nodeSize;
    
    public SimulationControl()
    {
        timeList = new ArrayList<>();
        topologyList = new ArrayList<>();
    }
    
    public List<Topology> getTopologyList()
    {
        return topologyList;
    }
    
    public void setTopology( Topology topology )
    {
        this.topology = topology;
        this.nodeSize = topology.getNodeMap().size();
    }
    
    public void add( double time , Topology top )
    {
        timeList.add( time );
        topologyList.add( top );
    }
    
    public Topology process() throws CloneNotSupportedException
    {
        if ( topology == null )
        {
            setTopology( topologyList.get( 0 ) );
        }
        
        topology.getConfigurationMap().put( "size" , topologyList.size() + "" );
        setTimeMean( topology );
        
        for( int i = 1 ; i <= nodeSize ; i++ )
        {
            Node node = topology.getNodeMap().get( i );
            topology.getNodeMap().put( i , setNodeMean( node ) );
        }
        
        return topology;
    }
    
    private void setTimeMean( Topology top )
    {
        double time = 0;
        StringBuilder builder = new StringBuilder();
        
        for( double t : timeList )
        {
            time += t;
            
            if( !builder.toString().isEmpty() )
            {
                builder.append( " , " );
            }
            
            builder.append( t );
        }
        
        double mean = time / timeList.size();
        
        top.getConfigurationMap().put( "time_executed" , mean + "" );
        top.getConfigurationMap().put( "time_list"     , "[ " + builder.toString() + " ]" );
    }
    
    private Node setNodeMean( Node node ) throws CloneNotSupportedException
    {
        double energy  = 0.0;
        double battery = 0.0;
        
        StringBuilder eBuilder = new StringBuilder(); //energy
        StringBuilder xBuilder = new StringBuilder(); //pos.x
        StringBuilder yBuilder = new StringBuilder(); //pos.y
        StringBuilder rBuilder = new StringBuilder(); //range
        
        for( Topology top : topologyList )
        {
            Node n  = top.getNodeMap().get( node.getId() );
            
            String energyStr = n.getProperties().get( "energy" );
            
            energy += Double.parseDouble( energyStr );
            battery = Double.parseDouble( n.getProperties().get( "battery" ) );
            
            if ( !eBuilder.toString().isEmpty() )
            {
                eBuilder.append( " , " );
                xBuilder.append( " , " );
                yBuilder.append( " , " );
                rBuilder.append( " , " );
            }
            
            eBuilder.append( energyStr );
            xBuilder.append( n.getProperties().get( "X" ) );
            yBuilder.append( n.getProperties().get( "Y" ) );
            rBuilder.append( n.getProperties().get( "range" ) );
        }
        
        energy = energy / topologyList.size();
        
        node.getProperties().put( "X_list"    , "[ " + xBuilder.toString() + " ]" );
        node.getProperties().put( "Y_list"    , "[ " + yBuilder.toString() + " ]" );
        node.getProperties().put( "energy_list"   , "[ " + eBuilder.toString() + " ]" );
        node.getProperties().put( "range_list"    , "[ " + rBuilder.toString() + " ]" );
        
        node.getProperties().put( "energy"        , energy + "" );
        node.getProperties().put( "battery"       , battery + "" );
        node.getProperties().put( "battery_level" , getBatteryLevel( energy , battery ) );
        
        return node;
    }
    
    private String getBatteryLevel( double energy , double battery )
    {
        if( energy == 0.0 )
        {
            return "100.00";
        }
        
        double level = 100 - (energy * 100) / battery;

        if( level < 0 )
        {
            level = 0;
        }

        DecimalFormat f = new DecimalFormat( "0.00" );
        f.setRoundingMode( RoundingMode.HALF_UP );
        return f.format( level ).replace( ',' , '.' );
    }
    
}
