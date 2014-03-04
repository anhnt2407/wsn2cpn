package br.cin.ufpe.wsn2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNXML;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.instances.InstanceItens;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorBlock;
import br.cin.ufpe.wsn2cpn.Topology;
import br.cin.ufpe.wsn2cpn.layer.LayerContainer;
import br.cin.ufpe.wsn2cpn.layer.LayerModelOpen;
import br.cin.ufpe.wsn2cpn.translator.colectInformation.TimeBreakPoint;
import br.cin.ufpe.wsn2cpn.translator.monitors.MonitorFactory;
import br.cin.ufpe.wsn2cpn.translator.monitors.SimulationMonitor;
import java.io.FileWriter;

/**
 *
 * @author avld
 */
public class WsnTranslatorToCpn
{
    private CPN cpn;
    private String modelStr;
    private Topology topology;
    
    public WsnTranslatorToCpn()
    {
        // do nothing
    }

    private void selectMonitor() throws Exception
    {
        if( !topology.getConfigurationMap().containsKey( "criteria_stop" ) )
        {
            topology.getConfigurationMap().put( "criteria_stop" , "FND" );
        }
        
        if( !topology.getConfigurationMap().containsKey( "criteria_stop_value" ) )
        {
            topology.getConfigurationMap().put( "criteria_stop_value" , "0.0" );
        }
        
        String monitorName  = topology.getConfigurationMap().get( "criteria_stop" );
        String monitorValue = topology.getConfigurationMap().get( "criteria_stop_value" );
        
        SimulationMonitor m = MonitorFactory.getMonitor( monitorName );
        
        for( Page p : cpn.getPage() )
        {
            if( "Network".equalsIgnoreCase( p.getPageattrName() ) )
            {
                m.setNetworkPage( p );
                break ;
            }
        }
        
        long id = cpn.getInstances().get( 0 ).getInstanceId();
        
        if( monitorValue == null 
                ? false 
                : !monitorValue.trim().isEmpty() )
        {
            m.setValue( Double.parseDouble( monitorValue ) );
        }
        
        m.setNodeSize( topology.getNodeMap().size() );
        
        Monitor monitor = m.create( id );
        
        cpn.setMonitors( new MonitorBlock() );
        cpn.getMonitors().getMonitor().add( monitor );
        
        // Adiciona o 'Colect Information Break Point'
        if( topology.getVariableMap().containsKey( "timeInterval" ) )
        {
            Monitor ciBreakPoint = new TimeBreakPoint( m.getValue() , monitor.getNode() );
            cpn.getMonitors().getMonitor().add( ciBreakPoint );
        }
    }
    
    public String getModel()
    {
        return modelStr;
    }

    public void save(String filename) throws Exception
    {
        System.out.println("criando o arquivo... " + filename );

        FileWriter writer = new FileWriter( filename );
        writer.write( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n\n" );
        writer.write( modelStr );
        writer.close();
    }
    
    public void buildModel(Topology topology) throws Exception
    {
        String appLayer = topology.getConfigurationMap().get( "application_layer" );
        
        LayerModelOpen appOpener = LayerContainer.getInstance().openLayer( "application" , appLayer );
        
        ChangeID appChangeID = new ChangeID( 0 , appOpener.getCPN() );
        appChangeID.process();
        
        buildModel( topology , appOpener );
    }
            
    public void buildModel(Topology topology , LayerModelOpen appOpener ) throws Exception
    {
        this.topology = topology;
        String macLayer = topology.getConfigurationMap().get( "mac_layer"         );
        String netLayer = topology.getConfigurationMap().get( "network_layer"     );
        int startId   = 1000;
        
        System.out.println( "Abrindo os modelos..." );
        LayerModelOpen macOpener = LayerContainer.getInstance().openLayer( "mac"         , macLayer );
        LayerModelOpen netOpener = LayerContainer.getInstance().openLayer( "network"     , netLayer );
        
        ChangeID macChangeID = new ChangeID( startId , macOpener.getCPN() );
        cpn = macChangeID.process();
        
        ChangeID netChangeID = new ChangeID( macChangeID.getLastId() , netOpener.getCPN() );
        netChangeID.process();
        
        // ----------------------------- //
        
        long pageId = cpn.getPage().get( 0 ).getId();
        cpn.getInstances().clear();
        cpn.getInstances().add( new InstanceItens( pageId ) );
        
        selectMonitor();
        
        // ----------------------------- //
        
        System.out.println( "Verificando todos os perfies..." );
        ProfileControl profileControl = new ProfileControl();
        profileControl.add( netOpener.getProfilesList() );
        profileControl.add( appOpener.getProfilesList() );
        
        System.out.println( "Ajustando o Globbox..." );
        GlobboxAjust globboxAjust = new GlobboxAjust( cpn );
        globboxAjust.setProfileControl( profileControl );
        globboxAjust.addNodeProperty( macOpener.getConfiguration().getNodeProperties() );
        globboxAjust.addNodeProperty( netOpener.getConfiguration().getNodeProperties() );
        globboxAjust.addNodeProperty( appOpener.getConfiguration().getNodeProperties() );
        globboxAjust.setTopology( topology );
        globboxAjust.ajust();
        
        // ----------------------------- //
        
        System.out.println( "Convertendo o objeto em XML..." );
        modelStr = CPNXML.convertTo( cpn );
    }
    
}
