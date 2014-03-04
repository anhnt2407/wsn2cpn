package br.cin.ufpe.wsn2cpn.report;

import br.cin.ufpe.wsn2cpn.Node;

/**
 *
 * @author avld
 */
public class EnergyMapReport extends Report
{

    public EnergyMapReport()
    {
        // do nothing
    }

    @Override
    public void make() throws Exception
    {
        System.out.println( "=========================================== ENERGY MAP\n" );

        for( Node node : getTopology().getNodeMap().values() )
        {
            System.out.println( "node id: " + node.getId() );
            System.out.println( "energy.: " + node.getProperties().get( "energy" ) );
            System.out.println( "battery level.: " + node.getProperties().get( "battery_level" ) );
//            System.out.println( "power .: " + node.getProperties().get( "power" ) );
            System.out.println( "time ..: " + node.getProperties().get( "performance" ) );
            System.out.println();
            //System.out.println( node.toString() );
            //System.out.println();
        }

        System.out.println( "===========================================" );
    }

}
