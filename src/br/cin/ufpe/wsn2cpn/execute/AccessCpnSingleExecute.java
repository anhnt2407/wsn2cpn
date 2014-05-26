package br.cin.ufpe.wsn2cpn.execute;

import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.debug.Debug;
import br.cin.ufpe.wsn2cpn.translator.ConvertToTopology;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.checker.Checker;
import org.cpntools.accesscpn.model.PetriNet;
import org.cpntools.accesscpn.model.importer.DOMParser;

/**
 * Esta classe executa apenas uma única vez o modelo!
 * 
 * @author avld
 */
public class AccessCpnSingleExecute extends WsnCpnExecute
{
    private PetriNet petriNet;
    private Checker checker;
    private HighLevelSimulator simulator;
    
    private boolean run;
    private Debug debug;
    private boolean simulationManyTimes;
    
    private double globalTime;
    
    public AccessCpnSingleExecute( String filename , Debug debug )
    {
        super( filename );
        
        this.debug = debug;
        this.simulationManyTimes = false;
    }

    public boolean isSimulationManyTimes()
    {
        return simulationManyTimes;
    }

    public void setSimulationManyTimes( boolean simulationManyTimes )
    {
        this.simulationManyTimes = simulationManyTimes;
    }

    public double getGlobalTime()
    {
        return globalTime;
    }

    /**
     * Este método configura o ambiente e inicia o simulação do modelo.
     * 
     * @return
     * @throws Exception 
     */
    @Override
    public List<Topology> executar() throws Exception
    {
        File file = new File( filename );

        if( !file.exists() )
        {
            throw new Exception( "This file doesn't exist." );
        }

        debug.println("Carregando o arquivo... " + file.getAbsolutePath() );
        //petriNet = DOMParser.parse( new URL( "file://" + file.getAbsolutePath() ) );
        petriNet = DOMParser.parse( new FileInputStream( file ) , file.getName() );
        
        debug.println("Carregando o simulador...");
        HighLevelSimulator.DEBUG_SIMULATOR = true;
        simulator = HighLevelSimulator.getHighLevelSimulator();

        System.out.println("Verificando a sintaxe...");
        checker = new Checker( petriNet , null , simulator );
        checker.checkEntireModel();

        // ----------------------------------------------- //

        debug.println( "configurando..." );
        simulator.setSimulationReportOptions( false , false , "" );
        simulator.setModelNameModelDirOutputDir( file.getName()
                                               , file.getParent()
                                               , getOutputDir() );
        
        List<Topology> result = startSimulation();
        desfazerSequencia( result );

        return result;
    }

    /**
     * Este método avaliar o modelo apenas uma única vez.
     * 
     * @return
     * @throws Exception 
     */
    public List<Topology> startSimulation() throws Exception
    {
        debug.println( "executando..." );
        run = true;
        
        List<Topology> topologyList = new LinkedList<>();
        
        simulator.initialState();
        
        do
        {
            String returnStr = simulator.execute( 1000 );
            
            debug.clear();
            debug.println( returnStr );
            
            String timeGlobalStr = simulator.evaluate( "!timeGlobal" );
            debug.println( "time simulation: " + timeGlobalStr );
            
            String energy = simulator.evaluate( "!energyList" );
            debug.println( "energy: " + energy );
            
            if( returnStr.toLowerCase().indexOf( "colect_information" ) != -1 ||
                    returnStr.toLowerCase().indexOf( "colect information" ) != -1 )
            {
                debug.println( "Coletando as informações da simulação..." );
                
                Topology top = convertToTopology( timeGlobalStr );
                topologyList.add( top );
            }
            else if( returnStr.toLowerCase().indexOf( "criteria_stop" ) != -1
               || returnStr.equalsIgnoreCase("No more enabled transitions!" ) )
            {
                debug.println( "Coletando as informações e Parando a simulação..." );
                
                Topology top = convertToTopology( timeGlobalStr );
                topologyList.add( top );
                
                globalTime = getEvaluateDouble( timeGlobalStr );
                
                break ;
            }
        }
        while( run );

        // --------------------------------------------- //
        //System.out.println( simulator.evaluate( "CPN'placeID6666256.get( 1 )" ) );

        if( run )
        {
            debug.println( "--- FINISH --- FINISH --- FINISH --- FINISH --- FINISH ---" );
        }
        else
        {
            debug.println( "--- STOP --- STOP --- STOP --- STOP --- STOP ---" );
        }
        
        for( int i = 0; i < topologyList.size() ; i++ )
        {
            Topology top = topologyList.get( i );
            
            String timeGlobalStr = top.getConfigurationMap().get( "time_executed" );
            double timeGlobal    = Double.parseDouble( timeGlobalStr );
            
            SimulationControl control = new SimulationControl();
            control.add( timeGlobal , top );
            
            Topology top2 = control.process();
            topologyList.set( i , top2 );
        }
        
        return topologyList;
    }

    private Topology convertToTopology( String timeGlobalStr ) throws Exception
    {
        ConvertToTopology convert = new ConvertToTopology( simulator );
            
        Topology top = convert.topology();
        
        double timeGlobal = getEvaluateDouble( timeGlobalStr );
        top.getConfigurationMap().put( "time_executed" , timeGlobal + "" );

        return top;
    }
    
    private String getOutputDir()
    {
        File outputDirFile = new File( "./" );
        return outputDirFile.getAbsolutePath();
    }

    private double getEvaluateDouble( String value )
    {
        //val it = 0 : int
        String parts[] = value.split( " " );
        return Double.parseDouble( parts[3] );
    }
    
    public void setRun( boolean run )
    {
        this.run = run;
    }
    
    public boolean isRun()
    {
        return this.run;
    }
    
    public void endSimulation()
    {
        simulator.destroy();
        
        checker = null;
        petriNet = null;
        debug = null;
        simulator = null;
    }
}

