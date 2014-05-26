package br.cin.ufpe.wsn2cpn;

import br.cin.ufpe.wsn2cpn.debug.ConsoleDebug;
import br.cin.ufpe.wsn2cpn.execute.AccessCpnSingleExecute;
import br.cin.ufpe.wsn2cpn.report.ReportManager;
import java.util.List;
import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;

/**
 *
 * @author avld
 */
public class Wsn2CpnMain
{

    //wsn2cpn topology.wsn
//        arg = new String[1];
//        arg[0] = "topology.wsn";
    public static void main(String arg[]) throws Exception
    {
        System.setProperty( "wsn2cpn.output"     , "./"   );
        System.setProperty( "wsn2cpn.timeGlobal" , "1000.0" );
        
        arg = new String[]{ "./scenarios/scenario1A_PND_50-direct.ewsn" };
        
        String filename = null;
        Topology top0 = null;

        if( arg.length < 1 )
        {
            //throw new Exception( "It required a file. (*.WSN)." );
            filename = "./wsn_model.cpn";
            top0 = new TopologyTest();
        }
        else
        {
            filename = getCpnName( arg[0] );
            top0 = WsnFile.open( arg[0] );
        }

        System.setProperty( "wsn2cpn.filename" , filename );

        
        AccessCpnSingleExecute executor = new AccessCpnSingleExecute( filename , new ConsoleDebug() );
        executor.analyseSequencialNodes( top0 );
        executor.traduzir( top0 );
        
        //System.exit( 1 );
        
         List<Topology> list = executor.executar();
         Topology top1 = list.get( list.size() - 1 );
         
        //"radio.msg.sent"
        //"radio.msg.received"
        //"radio.msg.routed"

        ReportManager.getIntance().setTopology( top1 );
        ReportManager.getIntance().make();

        System.exit( 0 );
    }

    private static String getCpnName( String filename )
    {
        String cpnName = filename.substring( 0 , filename.lastIndexOf(".") );
        return cpnName + ".cpn";
    }
}
